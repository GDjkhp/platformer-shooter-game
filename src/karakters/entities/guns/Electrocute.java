package karakters.entities.guns;

import karakters.GameObject;
import karakters.entities.shapes.Area_Circle;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.ITEM_ID;
import karakters.managers.EntityManager;
import karakters.managers.StateManager;

import java.awt.*;
import java.util.List;

public class Electrocute extends GameObject {
    int bulletRange = 50, area, iteration; // nerf this
    List<GameObject> e, ce, pe;
    EntityManager em;
    GameObject targetObject, playerRange;
    ENTITY_TYPE shooter, target;
    public Electrocute(float x, float y, int width, int height, EntityManager em, int area, ENTITY_TYPE shooter, ENTITY_TYPE target,
                       float velX, float velY, GameObject ignore, GameObject rangeArea) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Bullet;
        this.em = em;
        this.ce = em.culledObjects; this.e = em.objects; this.pe = em.particleObjects;
        this.shooter = shooter; this.target = target;
        this.velX = velX; this.velY = velY;
        this.area = area;
        color = Color.cyan;
        item_id = ITEM_ID.Electrocute;

        // Init Player Range (this is so wrong, becomes electropyte)
        /*playerRange = rangeArea == null ? new Area_Rect((x+width/2)-area/2, (y+height/2)-area/2, area, area) : rangeArea;
        e.add(playerRange);*/

        // here's how you do it properly (realized this is also wrong smh)
        // TODO: once you hit something, burst instead of multiply, by checking if playerRange exists
        if (rangeArea != null) {
            // Init Player Range
            playerRange = rangeArea;
            e.add(playerRange);

            // Init Target
            // List<GameObject> everyPossibleTargets = new ArrayList<>();

            // fun: multiply by ignoring supplemented GameObject
            /*for (GameObject o : ce) {
                if (o != playerRange && o.type == target && o != ignore && playerRange.getBounds().intersects(o.getBounds().getBounds())) {
                    if (targetObject == null)
                        targetObject = o;
                    else {
                        // path find player, bad (improved)
                        float centerX = x + width / 2, centerY = y + height / 2,
                                pCenterX = targetObject.getX() + targetObject.getWidth() / 2,
                                pCenterY = targetObject.getY() + targetObject.getHeight() / 2,
                                oCenterX = o.getX() + o.getWidth() / 2,
                                oCenterY = o.getY() + o.getHeight() / 2;

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
            }*/
        }

        // to future me: don't get confused, being lazy as usual (hint: fun)
        if (ignore != null) targetObject = ignore;

        // fun: random
        /*if (everyPossibleTargets.size() != 0)
            targetObject = everyPossibleTargets.get(new Random().nextInt(everyPossibleTargets.size()));*/
    }

    @Override
    public void tick() {
        // dynamic
        if (targetObject != null) {
            // blunder
            blockCollision();
            // burst
            if (targetObject.getBounds().intersects(getBounds())) {
                // fun: die and multiply when hit (this is wrong, nerf this by bursting)
                // e.add(new Electrocute(x, y, width, height, em, playerRange.getWidth(), shooter, target, velX, velY, targetObject, playerRange));
                iGotShotLogic(targetObject, em, shooter);

                // targetObject.color = Color.red; // hit ya!
                e.remove(playerRange);
                e.remove(this);
            }

            // remove when entity out of bounds (this can be a problem when you remove playerRange while others are still using it?)
            if (!playerRange.getBounds().intersects(getBounds())) {
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
            collision(); // safety
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

    // requires target to kill
    void collision() {
        for (GameObject o : ce) {
            if (o != this && o.getBounds().intersects(getBounds()) && (o.type == ENTITY_TYPE.Block || o.type == target)) {
                if (o.type == target) {
                    // fun: use this for multiply
                    /*e.add(new Electrocute(x, y, width, height, em, area, shooter, target, velX, velY, o,
                            playerRange == null ? new Area_Circle((x+width/2)-area/2, (y+height/2)-area/2, area, area) : playerRange));*/

                    // burst
                    Area_Circle a = new Area_Circle((x+width/2)-area/2, (y+height/2)-area/2, area, area);
                    for (GameObject r : ce) {
                        if (r != o && r.type == target && a.getBounds().intersects(r.getBounds().getBounds()))
                            e.add(new Electrocute(x, y, width, height, em, area, shooter, target, velX, velY, r, a));
                    }
                    iGotShotLogic(o, em, shooter);
                }
                e.remove(this);
            }
        }
    }

    // i'm so tired
    void blockCollision() {
        for (GameObject o : ce) {
            if (o != this && o.getBounds().intersects(getBounds()) && o.type == ENTITY_TYPE.Block) {
                e.remove(playerRange);
                e.remove(this);
            }
        }
    }
}
