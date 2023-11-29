package karakters.entities.blocks;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.EntityManager;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Portal extends GameObject {
    EntityManager em;
    Portal destination;
    List<GameObject> hold = new CopyOnWriteArrayList<>();
    public Portal(float x, float y, int width, int height, EntityManager em, Color c) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Area;
        this.em = em; this.color = c;
    }

    @Override
    public void tick() {
        collision();
        holdEntities();
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.drawRect((int) x, (int) y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public void setEndPortal(Portal p) {
        destination = p;
    }

    void collision() {
        for (GameObject o : em.culledObjects) {
            if (this != o && destination != o && getBounds().contains(o.getBounds().getBounds())) {
                if (!o.isTeleporting) {
                    o.isTeleporting = true;
                    o.setX(destination.getX() + o.getX() - x);
                    o.setY(destination.getY() + o.getY() - y);
                } else if (!hold.contains(o)) hold.add(o);
            }
        }
    }
    void holdEntities() {
        for (GameObject o : hold) {
            if (!getBounds().intersects(o.getBounds().getBounds())) {
                o.isTeleporting = false;
                hold.remove(o);
            }
        }
    }
}
