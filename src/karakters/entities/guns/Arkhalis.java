package karakters.entities.guns;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.ENTITY_ID;
import karakters.enums.ITEM_ID;
import karakters.managers.AssetManager;
import karakters.managers.EntityManager;
import karakters.managers.StateManager;

import java.awt.*;
import java.util.List;

public class Arkhalis extends GameObject {
    List<GameObject> e, ce, pe;
    int frame = 0;
    ENTITY_TYPE shooter, target;
    EntityManager em;
    public Arkhalis(float x, float y, int width, int height, EntityManager em, ENTITY_TYPE shooter, ENTITY_TYPE target) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Bullet; id = ENTITY_ID.Arkhalis;
        this.e = em.objects; this.ce = em.culledObjects; this.pe = em.particleObjects; this.em = em;
        this.target = target; this.shooter = shooter;
        item_id = ITEM_ID.Arkhalis;

        // collision
        for (GameObject o : ce) {
            if (o != this && getBounds().intersects(o.getBounds().getBounds()) && o.type == target) {
                iGotShotLogic(o, em, shooter);
            }
        }

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        if (StateManager.DEV_HITBOX_ESP_LINES) g.drawRect((int)x, (int)y, width, height);
        renderSprite(AssetManager.slash, frame, g);
        frame++;
        if (frame >= 8) e.remove(this);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
