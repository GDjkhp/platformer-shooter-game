package karakters.managers;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class CameraManager {
    public static Rectangle screen = new Rectangle(0, 0, 720, 480);
    public double tx = 720/2, ty = 480/2, sx = .5, sy = .5;
    int fun = 0;
    boolean gridLines = false;

    public void cameraControls(double tx, double ty, double sx, double sy){
        this.tx = tx;
        this.sx = sx;
        this.ty = ty;
        this.sy = sy;
    }

    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, 720, 480);

        // you can put anything screen related here

        // camera codes
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform at = g2d.getTransform();

        // pre camera codes
        /*at.rotate(Math.toRadians(fun), 720/2, 480/2);
        fun++;*/
        at.scale(sx, sy);
        at.translate(tx, ty);

        // post camera codes
        g2d.setTransform(at);

//        g.setColor(Color.GREEN);
//        g.fillRect(720/2-32, 480/2-32, 64, 64);

        screen = new Rectangle((int)(-tx), (int)(-ty), (int)(720/sx), (int)(480/sy));
//        screen = new Rectangle((int)(-tx + 720*sx), (int)(-ty + 480*sy), 720, 480);

        g.setColor(Color.red);
        g.drawRect(screen.x, screen.y, screen.width, screen.height);

        // grid
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 16, 16);
        if (gridLines){
            int tile_size = 32;
            int width = 128, height = 512;
            int yOffset = height * tile_size - tile_size; // Calculate the y offset once
            g.setColor(Color.BLACK);
            for (int xAxis = 0; xAxis < width; xAxis++) {
                for (int yAxis = 0; yAxis < height; yAxis++) {
                    int x = xAxis * tile_size;
                    int y = yAxis * tile_size - yOffset;
                    if (screen.intersects(new Rectangle(x, y, tile_size, tile_size)))
                        g.drawRect(x, y, tile_size, tile_size);
                }
            }

            tile_size = 128;
            yOffset = height/4 * tile_size - tile_size; // Calculate the y offset once
            g.setColor(Color.ORANGE);
            for (int xAxis = 0; xAxis < width/4; xAxis++) {
                for (int yAxis = 0; yAxis < height/4; yAxis++) {
                    int x = xAxis * tile_size;
                    int y = yAxis * tile_size - yOffset;
                    if (screen.intersects(new Rectangle(x, y-tile_size+32, tile_size, tile_size)))
                        g.drawRect(x, y-tile_size+32, tile_size, tile_size);
                }
            }

            tile_size = 1024;
            yOffset = height/32 * tile_size - tile_size; // Calculate the y offset once
            g.setColor(Color.RED);
            for (int xAxis = 0; xAxis < width/32; xAxis++) {
                for (int yAxis = 0; yAxis < height/32; yAxis++) {
                    int x = xAxis * tile_size;
                    int y = yAxis * tile_size - yOffset;
                    if (screen.intersects(new Rectangle(x, y-tile_size+32, tile_size, tile_size)))
                        g.drawRect(x, y-tile_size+32, tile_size, tile_size);
                }
            }
        }
    }
}
