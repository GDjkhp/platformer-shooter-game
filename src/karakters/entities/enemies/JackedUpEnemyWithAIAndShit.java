package karakters.entities.enemies;

import karakters.GameObject;
import karakters.entities.particles.Particle_Trail_Sprite;
import karakters.enums.ENTITY_TYPE;
import karakters.entities.particles.Particle_Trail_Rect;
import karakters.managers.*;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class JackedUpEnemyWithAIAndShit extends GameObject {
    List<GameObject> e, ce, pe;
    EntityManager em;
    PhysicsManager pm;

    // Define gravity and jump-related variables (move this to PhysicsManager)
    private boolean jump = false;

    Random r = new Random();
    Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));

    // AI
    GameObject player;
    float initVelX, initVelY;

    // animation
    int frame = 0, face = 0;
    BufferedImage states[], walk[];

    public JackedUpEnemyWithAIAndShit(float x, float y, int width, int height, EntityManager em) {
        super(x, y, width, height);
        this.e = em.objects; this.ce = em.culledObjects; this.pe = em.particleObjects; this.em = em;
        type = ENTITY_TYPE.Enemy;
        color = Color.MAGENTA;
        initVelX = 3; initVelY = 0;
        velX = initVelX; velY = initVelY;
        guns = new GunManager(em); pm = new PhysicsManager(em);
        hp = baseHP = 5;

        guns.currentLoadOut = guns.loadOut[r.nextInt(guns.loadOut.length)];
        guns.tripleBullets = false;

        im = new InventoryManager(this, em);
        dm = new DropManager(im);
        // assets
        walk = AssetManager.enemyWalkFinal;
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

        // jump
        if (player != null && (player.getY() < y || jump)) {
            velY = pm.velYJump(velY);
            jump = false;
        }

        velY = pm.velYGravity(velY);
        y += (int)velY;

        // collision y
        GameObject[] hits = pm.collisionY(this);
        if (hits[0] != null) {
            pm.isJumping = false;
        }

        // pathfinder
        velX = initVelX;
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
//            y += pathY;

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

        // collision x
        hits = pm.collisionX(this);
        if (hits[0] != null || hits[1] != null) jump = true;
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
