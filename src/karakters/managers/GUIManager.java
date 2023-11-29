package karakters.managers;

import karakters.GameEngine;
import karakters.Window;
import karakters.screens.InGame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUIManager {
    InGame gaming;
    public GUIManager(GameManager gm) {
        gaming = new InGame(gm, this);
    }

    // adapted from EntityManager.java very accurate target mouse cursor, ain't no way this shit again (took me hours)
    public int[] mouseHack(int mouseX, int mouseY) {
        double scalingFactorX = (double) Window.SCALED_WIDTH() / 720;
        double scalingFactorY = (double) Window.SCALED_HEIGHT() / 480;
        return new int[] {
                (int) ((mouseX - (Window.REAL_WIDTH()/2-Window.SCALED_WIDTH()/2))/scalingFactorX),
                (int) ((mouseY - (Window.REAL_HEIGHT()/2-Window.SCALED_HEIGHT()/2))/scalingFactorY)
        };
    }

    public void render(Graphics g) {
        gaming.render(g);
    }
}
