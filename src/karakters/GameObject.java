package karakters;

import karakters.entities.particles.Particle_Basic_Circle;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.ENTITY_ID;
import karakters.enums.ITEM_ID;
import karakters.managers.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class GameObject {
    // transform
    protected float x, y;
    protected int width, height;
    public float velX, velY;
    // things/status
    public Color color;
    public ENTITY_TYPE type;
    public ENTITY_ID id;
    public float hp, baseHP, def, baseDef, mana, baseMana, moveSpeedX, jumpSpeedY;
    public boolean isTeleporting = false;
    public BufferedImage sprite;
    // klass
    public GunManager guns;
    public InventoryManager im;
    public DropManager dm;
    // item properties
    public ITEM_ID item_id;
    public int count;
    public String name;
    public float attack, defense, range, speed, durability, cost;

    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Shape getBounds(); // chatgpt says this is slow

    // try not to use new Rectangle???
    // spoiler: it's the same as getBounds
    public boolean isColliding(GameObject other) {
        // Check for collision between this object and another object
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }

    public BufferedImage renderSprite(BufferedImage[] img, int i, Graphics g) {
        if (i >= img.length) {
            System.out.println(img+"["+i+"]");
            return img[0];
        }
        double[] aspectRatio = AssetManager.calculateScaleFactors(
                img[i].getWidth(), img[i].getHeight(), width, height);

        g.drawImage(img[i],
                (int)(x+width/2)-(int)aspectRatio[1]*2/2,
                (int)(y+height/2)-(int)aspectRatio[2]*2/2,
                (int)aspectRatio[1]*2, (int)aspectRatio[2]*2, null);

        return img[i];
    }

    public void renderSprite(BufferedImage img, Graphics g) {
        double[] aspectRatio = AssetManager.calculateScaleFactors(
                img.getWidth(), img.getHeight(), width, height);

        g.drawImage(img,
                (int)(x+width/2)-(int)aspectRatio[1]*2/2,
                (int)(y+height/2)-(int)aspectRatio[2]*2/2,
                (int)aspectRatio[1]*2, (int)aspectRatio[2]*2, null);
    }

    public void particleDeath(EntityManager em, GameObject r) {
        // particle code
        float amount = 64;
        double angle = 0;
        double angleStep = 360 / amount;
        double projectileSpeed = 10;
        double radius = 1;
        for (int i = 0; i <= amount; i++) {
            double projectileDirXPosition = (((double) r.getX() + r.getWidth()/2) + Math.sin((angle * Math.PI) / 180));
            double projectileDirYPosition = (((double) r.getY() + r.getHeight()/2) + Math.cos((angle * Math.PI) / 180));

            double projectileMoveDirectionX = (projectileDirXPosition - ((double) r.getX() + r.getWidth()/2)) * projectileSpeed;
            double projectileMoveDirectionY = (projectileDirYPosition - ((double) r.getY() + r.getHeight()/2)) * projectileSpeed;

            // spawn bullets
            em.addParticles(new Particle_Basic_Circle((int) projectileDirXPosition, (int) projectileDirYPosition, 16, 16, // TODO: be consistent
                    (float)projectileMoveDirectionX, (float)projectileMoveDirectionY, em, r.color));
            angle += angleStep;
        }
    }

    // retard check
    public boolean dontfuckwithmycollisionchecks(GameObject r, ENTITY_TYPE shooter) {
        return !(r.type == shooter || r.type == ENTITY_TYPE.Bullet || r.type == ENTITY_TYPE.Area);
    }

    // requires shooter to ignore
    public void iGotShotLogic(GameObject o, EntityManager em, ENTITY_TYPE shooter) {
        List<GameObject> ce = em.culledObjects, e = em.objects;

        // blade code
        float mul = 2f;
        if (id == ENTITY_ID.Arkhalis) {
            // melee knockback y?
            if (o.getY() < y) {
                o.setY(y - o.getHeight()*mul);
            }
            if (o.getY() + o.getHeight() > y + height) {
                o.setY(y + height*mul);
            }
        }
        // boolets
        else {
            // fixme: y knockback, target must be thrown off mid air
            o.setY(o.getY()+velY-height);
        }

        // prevents you from getting cornered (blocks)
        for (GameObject r : ce) {
            if (o != r && o.getBounds().intersects(r.getBounds().getBounds()) && dontfuckwithmycollisionchecks(r, shooter)) {
                if (o.getY()-1 < r.getY()) {
                    o.setY(r.getY() - o.getHeight());
                }
                else if (o.getY() + o.getHeight() > r.getY() + r.getHeight()) {
                    o.setY(r.getY() + r.getHeight());
                }
                o.velY = 0;
            }
        }

        // blade code
        if (id == ENTITY_ID.Arkhalis) {
            // melee knockback x?
            if (o.getX() < x) {
                o.setX(x - o.getWidth()*mul);
            }
            if (o.getX() + o.getWidth() > x + width) {
                o.setX(x+width*mul);
            }
        }
        // boolets
        else {
            o.setX(o.getX()+velX);
        }

        // prevents you from getting cornered (blocks)
        for (GameObject r : ce) {
            if (o != r && o.getBounds().intersects(r.getBounds().getBounds()) && dontfuckwithmycollisionchecks(r, shooter)) {
                if (o.getX() < r.getX()) {
                    o.setX(r.getX() - o.getWidth());
                }
                else if (o.getX() + o.getWidth() > r.getX() + r.getWidth()) {
                    o.setX(r.getX() + r.getWidth());
                }
                o.velX = 0;
            }
        }

        o.hp--;
        if (o.hp < 1) {
            System.out.println(o+": "+o.hp);
            o.im.burst();
            particleDeath(em, o);
            e.remove(o); // dead
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
