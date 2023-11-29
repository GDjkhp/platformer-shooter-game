package karakters.managers;

import karakters.GameEngine;

import java.awt.event.*;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    public boolean[] move = {
            false, // left
            false, // right
            false, // jump
            false, // sit
    };
    public float mouseX, mouseY;

    public InputManager(GameEngine engine) {
        engine.addKeyListener(this);
        engine.addMouseListener(this);
        engine.addMouseMotionListener(this);
        engine.addMouseWheelListener(this);
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

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
