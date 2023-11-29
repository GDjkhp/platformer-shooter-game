package karakters.entities.shapes;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.StateManager;

import java.awt.*;

public class Area_Rect extends GameObject {
    public Area_Rect(float x, float y, int width, int height) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Area;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        if (StateManager.DEV_HITBOX_ESP_LINES) g.drawRect((int)x, (int)y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
