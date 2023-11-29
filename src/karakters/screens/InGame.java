package karakters.screens;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.GUIManager;
import karakters.managers.GameManager;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class InGame implements MouseListener, MouseMotionListener {
    GameManager gm;
    GUIManager gui;
    GameObject player = null;
    public int mouseX, mouseY, lineX, lineY;

    public InGame(GameManager gm, GUIManager gui) {
        gm.engine.addMouseListener(this);
        gm.engine.addMouseMotionListener(this);
        this.gui = gui; this.gm = gm;
    }
    public void render(Graphics g) {
        int[] hackedMouse = gui.mouseHack(mouseX, mouseY);
        lineX = hackedMouse[0]; lineY = hackedMouse[1];

        g.setColor(Color.WHITE);
        // g.fillRect(lineX-8, lineY-8, 16, 16);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        // g.drawString("a quick brown fox jumps over the lazy dog.", 0, 16);

        for (GameObject o : gm.em.culledObjects) if (o.type == ENTITY_TYPE.Player) player = o;
        if (player != null) {
            g.drawString("Health: "+player.hp+"/"+player.baseHP, 0, 16);
            g.drawString("Gun: "+player.guns.currentLoadOut, 0, 16 * 2);
            g.drawString("Bullets: "+player.guns.totalBulletsShot, 0, 16 * 3);

            // pack inventory
            CopyOnWriteArrayList<GameObject> packedStack = new CopyOnWriteArrayList();
            for (GameObject o : player.im.stack) {
                if (packedStack.size() == 0) {
                    o.count++;
                    packedStack.add(o);
                } else {
                    boolean vibe_check = false;
                    for (GameObject r : packedStack) {
                        if (o.item_id == r.item_id) {
                            r.count++;
                            vibe_check = true;
                        }
                    }
                    if (!vibe_check) {
                        o.count++;
                        packedStack.add(o);
                    }
                }
            }

            int index = 0;
            for (GameObject o : packedStack) {
                g.setColor(o.color);
                g.fillOval(720-32, index, 32, 32);
                Color stack_count_color = o.color == Color.WHITE ? Color.BLACK : Color.WHITE;
                g.setColor(stack_count_color);
                g.drawString(String.valueOf(o.count), 720-32, index+16);
                index+=32;
            }

            for (GameObject r : packedStack) {
                r.count = 0;
            }
        }

        int totalEnemies = 0, culledTotalEnemies = 0;
        for (GameObject o : gm.em.objects) {
            if (o.type == ENTITY_TYPE.Enemy) totalEnemies++;
        }
        for (GameObject o : gm.em.culledObjects) {
            if (o.type == ENTITY_TYPE.Enemy) culledTotalEnemies++;
        }
        g.setColor(Color.WHITE);
        g.drawString("Enemies: "+culledTotalEnemies+"/"+totalEnemies, 0, 16*4);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
