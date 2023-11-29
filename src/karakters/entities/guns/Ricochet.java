package karakters.entities.guns;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.ITEM_ID;
import karakters.managers.CameraManager;
import karakters.managers.EntityManager;

import java.awt.*;
import java.util.List;

public class Ricochet extends GameObject {
    List<GameObject> e, ce, pe;
    EntityManager em;
    ENTITY_TYPE shooter, target;
    public int bulletRange = 300;
    public Ricochet(float x, float y, int width, int height, EntityManager em, ENTITY_TYPE shooter, ENTITY_TYPE target, float velX, float velY) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Bullet;
        e = em.objects; ce = em.culledObjects; pe = em.particleObjects;
        this.shooter = shooter; this.target = target;
        this.em = em;
        this.velX = velX; this.velY = velY;
        color = Color.orange;
        item_id = ITEM_ID.Ricochet;
    }

    @Override
    public void tick() {
        if (!CameraManager.screen.getBounds().intersects(getBounds())) e.remove(this);

        // collision
        for (GameObject o : ce) {
            if (o != this && getBounds().intersects(o.getBounds().getBounds()) && o.type == target) {
                iGotShotLogic(o, em, shooter);
                e.remove(this);
            }
        }
        for (GameObject o : ce) {
            if (o != this && getBounds().intersects(o.getBounds().getBounds()) && o.type == ENTITY_TYPE.Block) {
                if (x < o.getX() || x + width > o.getX() + o.getWidth()) {
                    if (x < o.getX()) x = o.getX() - width;
                    if (x + width > o.getX() + o.getWidth()) x = o.getX() + o.getWidth();
                    velX *= -1;
                }
                if (y < o.getY() || y + height > o.getY() + o.getHeight()) {
                    if (y < o.getY()) y = o.getY() - height;
                    if (y + height > o.getY() + o.getHeight()) y = o.getY() + o.getHeight();
                    velY *= -1;
                }
            }
        }

        // life
        if (bulletRange != 0) {
            x += velX;
            y += velY;
            bulletRange--;
        } else e.remove(this);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval((int)x, (int)y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
