package karakters.broken;

import karakters.Game;
import karakters.GameEngine;
import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.EntityManager;
import karakters.managers.GunManager;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class EntityPlayer2 extends GameObject implements KeyListener, MouseListener, MouseMotionListener {
    List<GameObject> e;
    GameEngine engine;
    // Define gravity and jump-related variables
    private static final float GRAVITY = .25f;
    private static final float JUMP_FORCE = 12f;
    private boolean isJumping = false;
    private boolean isGrounded = false; // ???
    boolean[] move = {false, false, false, false};
    GameObject platform;
    int crouchHeight, realHeight;
    float s = .5f;
    float lineX, lineY, mouseX, mouseY;
    public GunManager guns;
    public EntityPlayer2(float x, float y, int width, int height, GameEngine engine, List e) {
        super(x, y, width, height);
        engine.addKeyListener(this);
        engine.addMouseListener(this);
        engine.addMouseMotionListener(this);
        this.e = e;
        this.engine = engine;
        realHeight = height;
        crouchHeight = height/2;
        velX = 2;
        velY = 0;
        type = ENTITY_TYPE.Player;
        guns = new GunManager(new EntityManager());
    }

    @Override
    public void tick() {
        Game.text = String.format("x: %f, y: %f, velX: %f, velY: %f", x, y, velX, velY);

        // very accurate target mouse cursor
        lineX = mouseX/s - (-x - width/2 + 720);
        lineY = mouseY/s - (-y - height/2 + 480);

        if (move[2] && !isJumping) {
            velY -= JUMP_FORCE;
            isJumping = true;
        }

        velY += GRAVITY;
        y += (int)velY;

        if (move[3]) height = crouchHeight;
        else height = realHeight;

        for (GameObject r : e) {
            if (this != r && getBounds().intersects(r.getBounds().getBounds()) &&
                    !(r.type == ENTITY_TYPE.Bullet || r.type == ENTITY_TYPE.Area)) {
                if (velY > 0) {
                    isJumping = false;
                    y = r.getY() - height;
                    platform = r;
                }
                if (velY < 0) y = r.getY() + r.getHeight();
                velY = 0;
            }
        }

        if (move[0]) velX = 8;
        if (move[1]) velX = -8;
        if (move[0] || move[1]) x += (int)velX;
        if (platform != null) x += (int)platform.velX;

        for (GameObject r : e) {
            if (this != r && getBounds().intersects(r.getBounds().getBounds()) &&
                    !(r.type == ENTITY_TYPE.Bullet || r.type == ENTITY_TYPE.Area)) {
                if (velX > 0)
                    x = r.getX() - width;
                if (velX < 0)
                    x = r.getX() + r.getWidth();
                velX = 0;
            }
        }

        // shooting codes
        if (guns.isShooting) {
            if (guns.cooldown == 0) {
                guns.cooldown = guns.defaultCooldown;
//                guns.shootCodes(x, y, lineX, lineY);
            } else guns.cooldown--;
        }

//        engine.cameraControls((-x - width/2 + (720/s)/2), (-y - height/2 + (480/s)/2), s, s);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect((int) x, (int) y, width, height);
        g.setColor(Color.GREEN);
        g.drawLine((int) x, (int) y, (int) lineX, (int) lineY);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_D) move[0] = true;
        if (key == KeyEvent.VK_A) move[1] = true;
        if (key == KeyEvent.VK_S) move[3] = true;
        if (key == KeyEvent.VK_SPACE) move[2] = true;
        if (key == KeyEvent.VK_ENTER) {
            velY = 0;
            x = 720/2-32;
            y = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_D) move[0] = false;
        if (key == KeyEvent.VK_A) move[1] = false;
        if (key == KeyEvent.VK_S) move[3] = false;
        if (key == KeyEvent.VK_SPACE) {
            if (velY < 0) velY = GRAVITY;
            move[2] = false;
//            isJumping = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) guns.isShooting = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
//            System.out.println("RIGHT CLICK at " + e.getPoint());
            x = lineX; y = lineY;
//            System.out.println("x: "+x+", y: "+y);
        }
        if (e.getButton() == MouseEvent.BUTTON1) guns.isShooting = false;
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
