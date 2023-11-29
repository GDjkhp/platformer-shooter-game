package karakters;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import karakters.managers.*;

public class Game extends GameEngine implements KeyListener, MouseMotionListener {
    public static void main(String[] args) {
        new Game();
    }
    public static String text = "Hello, world!";
    GameManager gm = new GameManager(this);

    public Game() {

    }

    @Override
    public void tick() {
        gm.tick();
    }

    @Override
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        // init image
        BufferedImage image = ImageProcessing.processHandlerInit(720, 480);
        Graphics g = image.createGraphics();

        // you can put anything screen related here before the camera renders

        // main render
        gm.render(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 0, 16));
        g.drawString(text, 720/2-getTextWidth(g, text)/2, 480/2+8);

        // image processing codes
        image = ImageProcessing.postProcessing(image);

        // get image graphics
        g = bs.getDrawGraphics();

        // clear
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Window.REAL_WIDTH(), Window.REAL_HEIGHT());

        // draw everything
        g.drawImage(image,
                Window.REAL_WIDTH()/2-Window.SCALED_WIDTH()/2,
                Window.REAL_HEIGHT()/2-Window.SCALED_HEIGHT()/2,
                Window.SCALED_WIDTH(), Window.SCALED_HEIGHT(),null); // FIXME: not accurate? see Window.java dimension offset

        // center red pixel
        /*g.setColor(Color.red);
        g.drawRect(
                (Window.REAL_WIDTH()/2-Window.SCALED_WIDTH()/2)+Window.SCALED_WIDTH()/2-1,
                (Window.REAL_HEIGHT()/2-Window.SCALED_HEIGHT()/2)+Window.SCALED_HEIGHT()/2-1,
                1, 1);*/

        // you can put anything screen related here after the camera renders (GUI)
        BufferedImage imageLate = new BufferedImage(720, 480, BufferedImage.TYPE_INT_ARGB);
        g = imageLate.getGraphics();
        gm.renderLate(g);
        g = bs.getDrawGraphics();
        g.drawImage(imageLate,
                Window.REAL_WIDTH()/2-Window.SCALED_WIDTH()/2,
                Window.REAL_HEIGHT()/2-Window.SCALED_HEIGHT()/2,
                Window.SCALED_WIDTH(), Window.SCALED_HEIGHT(),null); // FIXME: not accurate? see Window.java dimension offset

        bs.show();
        g.dispose();
    }

    // the ultimate fix, for center
    public static int getTextWidth(Graphics g, String message){
        return g.getFontMetrics().stringWidth(message);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
