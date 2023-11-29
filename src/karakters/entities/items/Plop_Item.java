package karakters.entities.items;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.ITEM_ID;
import karakters.managers.EntityManager;
import karakters.managers.PhysicsManager;
import karakters.managers.StateManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Plop_Item extends GameObject {
    List<GameObject> e, ce, pe;
    EntityManager em;
    PhysicsManager pm;
    public Plop_Item(float x, float y, int width, int height, EntityManager em, ITEM_ID item_id, BufferedImage sprite) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Item;
        this.em = em;
        e = em.objects; ce = em.culledObjects; pe = em.particleObjects;
        pm = new PhysicsManager(em);
        this.sprite = sprite;
        if (sprite == null) color = getBulletColor(item_id);
        this.item_id = item_id;
    }

    @Override
    public void tick() {
        velY = pm.velYGravity(velY);
        y += (int)velY;
        pm.collisionY(this);
    }

    @Override
    public void render(Graphics g) {
        if (sprite == null) {
            g.setColor(color);
            g.fillOval((int) x, (int) y, width, height);
        } else g.drawImage(sprite, (int) x, (int) y, width, height, null);

        if (StateManager.DEV_HITBOX_ESP_LINES) {
            g.setColor(Color.GREEN);
            g.drawRect((int) x, (int) y, width, height);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    Color getBulletColor(ITEM_ID id) {
        switch (id) {
            case Bullet:
                return Color.BLUE;
            case Chlorophyte:
                return Color.GREEN;
            case Electrocute:
                return Color.CYAN;
            case Ricochet:
                return Color.ORANGE;
            case Rocket:
                return Color.RED;
            case Arkhalis:
                return Color.WHITE;
            case Whip:
                return Color.BLACK;
            case Arrow:
                return new Color(100, 50, 0);
            default:
                return Color.YELLOW;
        }
    }
}
