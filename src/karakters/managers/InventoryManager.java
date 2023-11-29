package karakters.managers;

import karakters.GameObject;
import karakters.entities.items.Plop_Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    public GameObject ent;
    public EntityManager em;
    List<GameObject> e;
    public ArrayList<GameObject> stack = new ArrayList<>(); // every plop item stored

    public InventoryManager(GameObject ent, EntityManager em) {
        this.e = em.objects;
        this.ent = ent;
        this.em = em;
    }

    public void burst() {
        for (GameObject o : stack) {
            e.add(new Plop_Item(ent.getX(), ent.getY(), 32, 32, em, o.item_id, o.sprite));
        }
    }
}
