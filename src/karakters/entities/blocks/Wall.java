package karakters.entities.blocks;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.AssetManager;
import karakters.managers.WorldManager;

import java.awt.*;

// should i even document this? ye i shouldn't
// anyways these are background blocks that doesn't require collisions
// aesthetic
public class Wall extends GameObject {
    WorldManager wm;
    private float startY, startX;
    public Wall(float x, float y, int width, int height, Color color, WorldManager wm) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Wall;
        this.color = color; this.wm = wm;
        startY = y;
        startX = x;
    }

    @Override
    public void tick() {
        if (wm.animate) {
            // Calculate the new y position using the sine function
            // rage: time + x (both)
            // rage speedrun: follow the comments but make time += 0.01

            if (wm.animateX) {
                float sineX = startX + wm.amplitude * (float) Math.sin(Math.PI * wm.frequency * wm.time + y * wm.spacing);
                if (!wm.pixel) x = sineX;
                else x = Math.round(sineX / 16) * 16;
            }

            if (wm.animateY) {
                float sineY = startY + wm.amplitude * (float) Math.sin(Math.PI * wm.frequency * wm.time + x * wm.spacing)/*+x*/;
                if (!wm.pixel) y = sineY;
                else y = Math.round(sineY / 16) * 16; // fun: snapped to 16x16 for pixel vibes
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (!AssetManager.renderAssets) {
            g.setColor(color);
            if (!wm.fill) g.drawRect((int) x, (int) y, width, height);
            else g.fillRect((int) x, (int) y, width, height);
        }
        else {
            g.drawImage(wm.brickWall, (int) x, (int) y, width, height, null);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
