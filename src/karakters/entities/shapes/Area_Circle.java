package karakters.entities.shapes;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.StateManager;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Area_Circle extends GameObject {
    public Area_Circle(float x, float y, int width, int height) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Area;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        if (StateManager.DEV_HITBOX_ESP_LINES) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.draw(getBounds());
        }
    }

    @Override
    public Shape getBounds() {
        return new Ellipse2D.Double(x, y, width, height);
    }
}
