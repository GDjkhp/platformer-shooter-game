package karakters;

import karakters.managers.StateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends WindowAdapter {
    public static JFrame frame = new JFrame();
    public Window (GameEngine game, int width, int height, String title) {
        width += 16; height += 39; // hack
        frame.addWindowListener(this);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);

        // frame.setLocation(1360 - width*2, 0);
        frame.setLocationRelativeTo(null);

        frame.setTitle(title);

        // System.out.println(frame.getWidth()+"x"+frame.getHeight());
        game.start();
    }
    public void setTitle(String title) {
        frame.setTitle(title);
    }
    public void windowClosing(WindowEvent e) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static int REAL_WIDTH() {
        return frame.getWidth()-16;
    }
    public static int REAL_HEIGHT() {
        return frame.getHeight()-39;
    }
    public static int SCALED_FACTOR() {
        return (int)calculateScaleFactors(720, 480, StateManager.GAME_WIDTH, StateManager.GAME_HEIGHT)[0];
    }
    public static int SCALED_WIDTH() {
        return (int)calculateScaleFactors(720, 480, StateManager.GAME_WIDTH, StateManager.GAME_HEIGHT)[1];
    }
    public static int SCALED_HEIGHT() {
        return (int)calculateScaleFactors(720, 480, StateManager.GAME_WIDTH, StateManager.GAME_HEIGHT)[2];
    }

    public static double[] calculateScaleFactors(int originalWidth, int originalHeight, int targetWidth, int targetHeight) {
        double scaleX = (double) targetWidth / originalWidth;
        double scaleY = (double) targetHeight / originalHeight;

        // Choose the minimum scale factor to maintain the aspect ratio
        double scaleFactor = Math.min(scaleX, scaleY);

        // Calculate the new width and height
        int newWidth = (int) (originalWidth * scaleFactor);
        int newHeight = (int) (originalHeight * scaleFactor);

        return new double[]{scaleFactor, newWidth, newHeight};
    }
}
