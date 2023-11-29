package karakters.managers;

import karakters.entities.items.Plop_Item;
import karakters.enums.ITEM_ID;

public class DropManager {
    public DropManager(InventoryManager im) {
        bulletPopulate(im);
    }
    // test
    public void bulletPopulate(InventoryManager im) {
        ITEM_ID item = ITEM_ID.Bullet;
        switch (im.ent.guns.currentLoadOut) {
            case Default:
                item = ITEM_ID.Bullet;
                break;
            case Chlorophyte:
                item = ITEM_ID.Chlorophyte;
                break;
            case Electrocute:
                item = ITEM_ID.Electrocute;
                break;
            case Ricochet:
                item = ITEM_ID.Ricochet;
                break;
            case Arkhalis:
                item = ITEM_ID.Arkhalis;
                break;
            case Rocket:
                item = ITEM_ID.Rocket;
                break;
            case Whip:
                item = ITEM_ID.Whip;
                break;
            case Arrow:
                item = ITEM_ID.Arrow;
                break;
        }
        im.stack.add(new Plop_Item(0, 0, 0, 0, im.em, item, null));
    }
}
