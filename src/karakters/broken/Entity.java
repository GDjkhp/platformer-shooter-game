package karakters.broken;

import karakters.GameObject;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Entity extends GameObject {
    List<GameObject> e;
    Random r = new Random();
    Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
    public Entity(float x, float y, int width, int height, List<GameObject> e) {
        super(x, y, width, height);
        this.e = e;
        velX = 3;
        velY = 1;
    }

    @Override
    public void tick() {
        // change velocity within screen
        if (x+width > 720 || x < 0) {
            velX *= -1;
            if (x+width > 720) x = 720-width;
            if (x < 0) x = 0;
        }
        if (y+height > 480 || y < 0) {
            velY *= -1;
            if (y+height > 480) y = 480-height;
            if (y < 0) y = 0;
        }

        y += velY;
        for (GameObject r : e) {
            if (this != r && getBounds().intersects(r.getBounds().getBounds())) {
                // sticky
                if (velY > 0)
                    y = r.getY() - height;
                if (velY < 0)
                    y = r.getY() + r.getHeight();
                /*if (velX > 0)
                    x = r.x - width;
                if (velX < 0)
                    x = r.x + r.width;*/
                velY *= -1;
            }
        }
        x += velX;
        for (GameObject r : e) {
            if (this != r && getBounds().intersects(r.getBounds().getBounds())) {
                if (velX > 0)
                    x = r.getX() - width;
                if (velX < 0)
                    x = r.getX() + r.getWidth();
                velX *= -1;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(c);
        g.fillRect((int) x, (int) y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
