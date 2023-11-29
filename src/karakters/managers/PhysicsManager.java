package karakters.managers;

import karakters.GameObject;
import karakters.entities.items.Plop_Item;
import karakters.enums.ENTITY_TYPE;

import java.util.List;

public class PhysicsManager {
    // Define gravity and jump-related variables
    public float GRAVITY = .3f;
    public float JUMP_FORCE = 15f;
    public boolean isJumping = false;
    List<GameObject> ce, e;
    EntityManager em;
    public PhysicsManager(EntityManager em) {
        this.ce = em.culledObjects;
        this.e = em.objects;
        this.em = em;
    }

    public void tick() {
        // do optimized collision here, maybe use comparator or something
    }

    // todo: item collision
    boolean twinbrotherofretardcollisioncheck(ENTITY_TYPE type) {
        return type == ENTITY_TYPE.Block || type == ENTITY_TYPE.Enemy || type == ENTITY_TYPE.Player || type == ENTITY_TYPE.Item;
    }

    public GameObject[] collisionY(GameObject o) {
        GameObject hit[] = { null, null }; // bottom, top
        for (GameObject r : ce) {
            if (o != r && o.getBounds().intersects(r.getBounds().getBounds()) &&
                    twinbrotherofretardcollisioncheck(r.type)) {
                if (o.type == ENTITY_TYPE.Player && r.type == ENTITY_TYPE.Item) { // push the item to player inv
                    o.im.stack.add(new Plop_Item(0, 0, 0, 0, em, r.item_id, null));
                    e.remove(r);
                } else {
                    if (o.getY() - 1 < r.getY() || o.getY() + o.getHeight() > r.getY() + r.getHeight()) {
                        if (o.getY() - 1 < r.getY()) {
                            o.setY(r.getY() - o.getHeight());
                            hit[0] = r;
                            // y -= (int)velY; // fun: sinks
                        }
                        if (o.getY() + o.getHeight() > r.getY() + r.getHeight()) {
                            o.setY(r.getY() + r.getHeight());
                            hit[1] = r;
                            // y += (int)velY;
                        }
                        o.velY = 0;
                    }
                }
            }
        }
        return hit;
    }

    public GameObject[] collisionX(GameObject o) {
        GameObject hit[] = { null, null }; // left, right
        for (GameObject r : ce) {
            if (o != r && o.getBounds().intersects(r.getBounds().getBounds()) &&
                    twinbrotherofretardcollisioncheck(r.type)) {
                if (o.type == ENTITY_TYPE.Player && r.type == ENTITY_TYPE.Item) { // push the item to player inv
                    o.im.stack.add(new Plop_Item(0, 0, 0, 0, em, r.item_id, null));
                    e.remove(r);
                } else {
                    if (o.getX() < r.getX() || o.getX() + o.getWidth() > r.getX() + r.getWidth()) {
                        if (o.getX() < r.getX()) {
                            o.setX(r.getX() - o.getWidth());
                            hit[0] = r;
                            // x -= (int)velX;
                        }
                        if (o.getX() + o.getWidth() > r.getX() + r.getWidth()) {
                            o.setX(r.getX() + r.getWidth());
                            hit[1] = r;
                            //x += (int)velX;
                        }
                        o.velX = 0;
                    }
                }

            }
        }
        return hit;
    }

    public float velYGravity(float velY) {
        return velY + GRAVITY;
    }

    public float velYJump(float velY) {
        if (!isJumping && velY < 1) { // disable double jump on fall (fun: double jump)
            velY -= JUMP_FORCE;
            isJumping = true;
        }
        return velY;
    }
}
