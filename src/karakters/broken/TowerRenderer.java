package karakters.broken;

import karakters.GameObject;

import java.awt.*;
import java.util.List;

public class TowerRenderer extends GameObject {
    public int[][] tileData;
    Rectangle camera;
    List<GameObject> e;

    public TowerRenderer(float x, float y, int width, int height,
                         int[][] tileData, Rectangle camera, List e) {
        super(x, y, width, height);
        this.tileData = tileData;
        this.camera = camera;
        this.e = e;
    }
    public void tick() {}
    public void render(Graphics g) {}

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
