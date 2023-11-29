package karakters.entities.guns;

import karakters.GameObject;
import karakters.entities.particles.Particle_Trail_Circle;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.ITEM_ID;
import karakters.managers.EntityManager;

import java.awt.*;
import java.util.List;

public class Bullet extends GameObject {
    public int range = 50;
    List<GameObject> e, ce, pe;
    ENTITY_TYPE shooter, target;
    EntityManager em;
    public Bullet(float x, float y, int width, int height, float velX, float velY, EntityManager em, ENTITY_TYPE shooter, ENTITY_TYPE target) {
        super(x, y, width, height);
        this.e = em.objects; this.ce = em.culledObjects; this.pe = em.particleObjects; this.em = em;
        this.velX = velX;
        this.velY = velY;
        color = Color.BLUE;
        type = ENTITY_TYPE.Bullet;
        this.target = target;
        this.shooter = shooter;
        item_id = ITEM_ID.Bullet;
    }

    @Override
    public void tick() {
        collision();
        if (range != 0) {
            x += velX;
            y += velY;
            range--;
            if (em.staticParticles) em.addParticles(new Particle_Trail_Circle(x, y, width, height, color, em));
        } else e.remove(this);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval((int) x, (int) y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public void collision() {
        for (GameObject r : ce) {
            if (this != r && getBounds().intersects(r.getBounds().getBounds()) &&
                    (r.type == ENTITY_TYPE.Block || r.type == target)) {
                if (/*r.type == ENTITY_TYPE.Block || */r.type == target/* && target != ENTITY_TYPE.Player*/) // kaboom! god mode?
                    iGotShotLogic(r, em, shooter);
                e.remove(this); // suicide
            }
        }
    }

    /*public void iGotShotLogic(GameObject o) {
        o.setY(o.getY()+velY);
        for (GameObject r : ce) {
            if (o != r && o.getBounds().intersects(r.getBounds()) &&
                    !(r.type == shooter || r.type == ENTITY_TYPE.Bullet || r.type == ENTITY_TYPE.Area || r.type == ENTITY_TYPE.Particle)) { // fixme: retard check
                if (o.getY()-1 < r.getY()) {
                    o.setY(r.getY() - o.getHeight());
                }
                if (o.getY() + o.getHeight() > r.getY() + r.getHeight()) {
                    o.setY(r.getY() + r.getHeight());
                }
                o.velY = 0;
            }
        }

        o.setX(o.getX()+velX);
        for (GameObject r : ce) {
            if (o != r && o.getBounds().intersects(r.getBounds()) &&
                    !(r.type == shooter || r.type == ENTITY_TYPE.Bullet || r.type == ENTITY_TYPE.Area || r.type == ENTITY_TYPE.Particle)) { // fixme: retard check
                if (o.getX() < r.getX()) {
                    o.setX(r.getX() - o.getWidth());
                }
                if (o.getX() + o.getWidth() > r.getX() + r.getWidth()) {
                    o.setX(r.getX() + r.getWidth());
                }
                o.velX = 0;
            }
        }

        o.hp--;
        if (o.hp < 1) {
            System.out.println(o+": "+o.hp);
            particleDeath(em, o);
            e.remove(o); // dead
        }
    }*/
}
