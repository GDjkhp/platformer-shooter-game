package karakters.entities.guns;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.ENTITY_ID;
import karakters.enums.ITEM_ID;
import karakters.managers.EntityManager;

import java.awt.*;
import java.util.List;

public class Whip extends GameObject {
    ENTITY_TYPE shooter, target;
    EntityManager em;
    List<GameObject> e, ce, pe;
    GameObject sadist;
    boolean hit = false;
    float lineX, lineY;
    float speed = 20;
    float initialX, initialY;
    Rectangle initialRect;
    public Whip(float x, float y, int width, int height, ENTITY_TYPE shooter, ENTITY_TYPE target,
                EntityManager em, GameObject sadist) {
        super(x-width/2, y-height/2, width, height);
        type = ENTITY_TYPE.Bullet; id = ENTITY_ID.Arkhalis;
        this.target = target; this.shooter = shooter;
        this.e = em.objects; this.ce = em.culledObjects; this.pe = em.particleObjects; this.em = em;
        color = Color.PINK;
        this.sadist = sadist;
        lineX = sadist.getX() + sadist.getWidth()/2; lineY = sadist.getY() + sadist.getHeight()/2;
        item_id = ITEM_ID.Whip;

        initialX = (int)sadist.getX() + sadist.getWidth()/2; initialY = (int)sadist.getY() + sadist.getHeight()/2;
        initialRect = new Rectangle((int)initialX-width/2, (int)initialY-height/2, width, height);
    }

    @Override
    public void tick() {
        // calculate sadist x, y to x, y
        if (sadist != null) {
            float sCenterX = lineX, sCenterY = lineY,
                    centerX = x+width/2, centerY = y+height/2;

            float diffX = sCenterX - centerX;
            float diffY = sCenterY - centerY;

            float distance = (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

            float speedModifier = hit ? speed : -speed;

            float pathX = ((speedModifier / distance) * diffX);
            float pathY = ((speedModifier / distance) * diffY);

            lineX += pathX;
            lineY += pathY;

            if (getBounds().intersects(lineX, lineY, 1, 1)) {
                hit = true;
                hitScan();
            }
            if (hit && initialRect.intersects(lineX, lineY, 1, 1)) {
                e.remove(this);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.drawLine((int)sadist.getX() + sadist.getWidth()/2, (int)sadist.getY() + sadist.getHeight()/2, (int)lineX, (int)lineY);
        g.drawRect((int)x, (int)y, width, height);

        g.setColor(Color.red);
        g.drawLine(initialRect.x+width/2, initialRect.y+height/2, (int)x+width/2, (int)y+height/2);
        g.drawRect(initialRect.x, initialRect.y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    void hitScan() {
        for (GameObject o : ce) {
            if (o != this && o.type == target && o.getBounds().intersects(getBounds())) {
                iGotShotLogic(o, em, shooter);
            }
        }
    }
}
