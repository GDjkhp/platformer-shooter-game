package karakters.entities.items;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;

import java.awt.*;

public class Plop_Mana extends GameObject {
    public Plop_Mana(float x, float y, int width, int height) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Item;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
