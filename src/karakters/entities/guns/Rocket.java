package karakters.entities.guns;

import karakters.GameObject;
import karakters.entities.particles.Particle_Trail_Circle;
import karakters.entities.shapes.Area_Circle;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.ITEM_ID;
import karakters.managers.EntityManager;

import java.awt.*;
import java.util.List;

public class Rocket extends GameObject {
    List<GameObject> ce, e;
    EntityManager em;
    ENTITY_TYPE shooter, target;

    public int range = 50;
    int frame = 0, area;

    public Rocket(float x, float y, int width, int height, float velX, float velY,
                  EntityManager em, ENTITY_TYPE shooter, ENTITY_TYPE target, int area) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Bullet;
        this.velX = velX; this.velY = velY;
        ce = em.culledObjects; this.em = em; e = em.objects;
        this.shooter = shooter; this.target = target; this.area = area;
        item_id = ITEM_ID.Rocket;
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
        if (frame % 2 == 0) g.setColor(Color.RED);
        else g.setColor(Color.ORANGE);
        g.fillOval((int) x, (int) y, width, height);
        frame++;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public void collision() {
        for (GameObject r : ce) {
            if (this != r && getBounds().intersects(r.getBounds().getBounds()) &&
                    (r.type == ENTITY_TYPE.Block || r.type == target)) {
                /* && target != ENTITY_TYPE.Player*/ // god mode?

                GameObject realCircleArea = new Area_Circle((x+width/2)-area/2, (y+height/2)-area/2, area, area);
                e.add(realCircleArea);

                for (GameObject o : ce) {
                    if (o != realCircleArea && realCircleArea.getBounds().intersects(o.getBounds().getBounds())) {
                        if (o.type == target) iGotShotLogic(o, em, shooter);
                        if (o.type == ENTITY_TYPE.Block) e.remove(o);
                    }
                }
                e.remove(realCircleArea);
                e.remove(this); // suicide
            }
        }
    }
}
