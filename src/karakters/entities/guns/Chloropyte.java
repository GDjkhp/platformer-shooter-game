package karakters.entities.guns;

import karakters.GameObject;
import karakters.entities.shapes.Area_Circle;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.ITEM_ID;
import karakters.managers.EntityManager;
import karakters.managers.StateManager;

import java.awt.*;
import java.util.List;

public class Chloropyte extends GameObject {
    List<GameObject> e, ce, pe;
    GameObject targetObject, playerRange;
    int bulletRange, baseRange;
    ENTITY_TYPE shooter, target;
    EntityManager em;

    public Chloropyte(float x, float y, int width, int height, EntityManager em, int area, float velX, float velY, ENTITY_TYPE shooter, ENTITY_TYPE target) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Bullet;
        e = em.objects; ce = em.culledObjects; pe = em.particleObjects;
        this.velX = velX; this.velY = velY;
        this.shooter = shooter; this.target = target;
        this.em = em;
        color = Color.green;
        bulletRange = baseRange = 50;
        item_id = ITEM_ID.Chlorophyte;

        // Init Player Range
        playerRange = new Area_Circle((x+width/2)-area/2, (y+height/2)-area/2, area, area);
        e.add(playerRange);

        // Init Target
        // List<GameObject> everyPossibleTargets = new ArrayList<>();
        for (GameObject o : ce) {
            if (o != playerRange && o.type == target && playerRange.getBounds().intersects(o.getBounds().getBounds())) {
                if (targetObject == null)
                    targetObject = o;
                else {
                    // path find player, bad (improved)
                    float centerX = x + width/2, centerY = y + height/2,
                            pCenterX = targetObject.getX() + targetObject.getWidth()/2,
                            pCenterY = targetObject.getY() + targetObject.getHeight()/2,
                            oCenterX = o.getX() + o.getWidth()/2,
                            oCenterY = o.getY() + o.getHeight()/2;

                    float pDiffX = centerX - pCenterX;
                    float pDiffY = centerY - pCenterY;
                    float oDiffX = centerX - oCenterX;
                    float oDiffY = centerY - oCenterY;

                    float distanceP = (float) Math.sqrt(Math.pow(pDiffX, 2) + Math.pow(pDiffY, 2));
                    float distanceO = (float) Math.sqrt(Math.pow(oDiffX, 2) + Math.pow(oDiffY, 2));

                    if (distanceP > distanceO) {
                        targetObject = o;
                    }
                }
                // everyPossibleTargets.add(o);
            }
        }

        // fun: random
        /*if (everyPossibleTargets.size() != 0)
            targetObject = everyPossibleTargets.get(new Random().nextInt(everyPossibleTargets.size()));*/
    }

    @Override
    public void tick() {
        blockCollision();
        // dynamic
        if (bulletRange < baseRange-10 && targetObject != null) {
            // die when hit
            if (targetObject.getBounds().intersects(getBounds())) {
                iGotShotLogic(targetObject, em, shooter);
                e.remove(playerRange);
                e.remove(this);
            }

            // path find player, bad (improved)
            float centerX = x + width/2, centerY = y + height/2,
                    pCenterX = targetObject.getX() + targetObject.getWidth()/2,
                    pCenterY = targetObject.getY() + targetObject.getHeight()/2;

            float diffX = centerX - pCenterX;
            float diffY = centerY - pCenterY;
            float distance = (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

            float pathX = ((-/*(speed)*/15 / distance) * diffX);
            float pathY = ((-/*(speed)*/15 / distance) * diffY);

            x += pathX;
            y += pathY;

        }
        // static
        else {
            x += velX;
            y += velY;
        }
        // die peacefully
        if (bulletRange == 0) {
            e.remove(playerRange);
            e.remove(this);
        }
        bulletRange--;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval((int)x, (int)y, width, height);

        if (targetObject != null && StateManager.DEV_HITBOX_ESP_LINES)
            g.drawLine((int)x+width/2, (int)y+height/2,
                    (int)targetObject.getX()+targetObject.getWidth()/2, (int)targetObject.getY()+targetObject.getHeight()/2);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    // die if bloke
    void blockCollision() {
        for (GameObject o : ce) {
            if (o != this && o.type == ENTITY_TYPE.Block && o.getBounds().intersects(getBounds())) {
                e.remove(playerRange);
                e.remove(this);
            }
        }
    }
}
