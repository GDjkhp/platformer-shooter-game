package karakters.managers;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityManager {
    public List<GameObject> objects = new CopyOnWriteArrayList<>();
    public List<GameObject> culledObjects = new CopyOnWriteArrayList<>();
    public List<GameObject> particleObjects = new CopyOnWriteArrayList<>();
    public List<GameObject> wallObjects = new CopyOnWriteArrayList<>();

    public boolean enableParticles = true, staticParticles = false; // trail circles

    public void getCulledObjects() {
        culledObjects.clear();
        for (GameObject o : objects) {
            if (CameraManager.screen.intersects(o.getBounds().getBounds()) && o.type != ENTITY_TYPE.Wall ||
                    (o.type == ENTITY_TYPE.Player || o.type == ENTITY_TYPE.Bullet))
                culledObjects.add(o);
        }
    }

    // optimization :(
    public void addParticles(GameObject p) {
        if (enableParticles) particleObjects.add(p);
    }

    public void tick() {
//        for (GameObject o : wallObjects) if (CameraManager.screen.intersects(o.getBounds())) o.tick();

        for (GameObject o : particleObjects) {
            o.tick();
        }

        getCulledObjects();
        for (GameObject o : culledObjects) {
            if (!(StateManager.DEV_FREEZE_BOTS && o.type == ENTITY_TYPE.Enemy)) o.tick();
        }
    }
    public void render(Graphics g) {
        for (GameObject o : wallObjects)
            if (CameraManager.screen.intersects(o.getBounds().getBounds())) o.render(g);
        for (GameObject o : particleObjects) {
            o.render(g);
        }
        for (GameObject o : culledObjects) {
            o.render(g);
        }
    }
}
