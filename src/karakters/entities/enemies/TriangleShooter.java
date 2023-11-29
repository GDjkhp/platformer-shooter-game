package karakters.entities.enemies;

import karakters.GameObject;
import karakters.entities.particles.Particle_Trail_Rect;
import karakters.entities.particles.Particle_Trail_Sprite;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

public class TriangleShooter extends GameObject {
    List<GameObject> e, ce, pe;
    EntityManager em;
    PhysicsManager pm;
    Random r = new Random();

    // AI
    GameObject player;
    float initVelX, initVelY;

    // animation
    int frame = 0, face = 0;
    BufferedImage states[], walk[];

    public TriangleShooter(float x, float y, int width, int height, EntityManager em) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Enemy;
        hp = baseHP = 5;
        color = Color.MAGENTA;
        this.em = em; e = em.objects; ce = em.culledObjects;
        initVelX = velX = 3; initVelY = velY = 3;
        guns = new GunManager(em); pm = new PhysicsManager(em);

        guns.currentLoadOut = guns.loadOut[r.nextInt(guns.loadOut.length)];
        guns.tripleBullets = false;

        im = new InventoryManager(this, em);
        dm = new DropManager(im);
        // assets
        walk = AssetManager.ghostFinal;
    }

    @Override
    public void tick() {
        // get player
        for (GameObject o : ce) {
            if (o.type == ENTITY_TYPE.Player) {
                player = o;
                break;
            }
        }

        // eye of cthulhu ai
        velX = initVelX; velY = initVelY;
        if (player != null) {
            // path find player, bad (improved)
            float centerX = x + width/2, centerY = y + height/2,
                    pCenterX = player.getX() + player.getWidth()/2,
                    pCenterY = player.getY() + player.getHeight()/2;

            float diffX = centerX - pCenterX;
            float diffY = centerY - pCenterY;
            float distance = (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

            // default is 3
            float pathX = (-velX / distance) * diffX;
            float pathY = (-velY / distance) * diffY;

            x += pathX;
            pm.collisionX(this);

            y += pathY;
            pm.collisionY(this);

            // TODO: path find player using A*


            if (!AssetManager.renderAssets) em.addParticles(new Particle_Trail_Rect(x, y, width, height, color, em));
            else em.addParticles(new Particle_Trail_Sprite(x, y, width, height, em, sprite));

            // shooting codes
            if (GunManager.enemiesUseGuns) {
                if (guns.cooldown < 0) {
                    guns.cooldown = guns.defaultCooldown;
                    guns.shootCodes(this,
                            player.getX()+player.getWidth()/2, player.getY()+player.getHeight()/2,
                            ENTITY_TYPE.Player);
                }
                guns.cooldown--;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        // hp indicator
        g.setColor(Color.RED);
        g.fillRect((int)x-width, (int)y-width, (int)(hp*(width/2*6)/baseHP), width/2);
        g.setColor(Color.BLACK);
        for (int i = 0; i < 6; i++)
            g.drawRect((int)x-width+(width/2)*i, (int)y-width, width/2, width/2);

        g.setColor(color);
        if (!AssetManager.renderAssets) {
            g.fillRect((int) x, (int) y, width, height);
        }
        // walk
        else {
            if (player != null)
                face = x + width / 2 < player.getX() ? 1 : 0; // isRight
            sprite = renderSprite(walk, frame+face, g);
            frame+=2;
            if (frame >= walk.length) frame = 0;
        }

        if (player != null && StateManager.DEV_HITBOX_ESP_LINES) {
            g.drawLine((int) x + width / 2, (int) y + height / 2,
                    (int) player.getX() + player.getWidth() / 2, (int) player.getY() + player.getHeight() / 2);
        }
        if (StateManager.DEV_HITBOX_ESP_LINES) g.drawRect((int) x, (int) y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
