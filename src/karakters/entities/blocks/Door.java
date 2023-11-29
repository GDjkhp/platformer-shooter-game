package karakters.entities.blocks;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;

import java.awt.*;

public class Door extends GameObject {
    public Door(float x, float y, int width, int height) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Area;
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
