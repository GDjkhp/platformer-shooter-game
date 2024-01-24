package karakters.entities.players;

import karakters.*;
import karakters.Window;
import karakters.entities.particles.Particle_Trail_Sprite;
import karakters.enums.ENTITY_TYPE;
import karakters.entities.particles.Particle_Trail_Rect;
import karakters.managers.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class EntityPlayer extends GameObject implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    List<GameObject> e, ce, pe;
    GameEngine engine;
    CameraManager cm;
    EntityManager em;
    PhysicsManager pm;

    // Define gravity and jump-related variables (move this to PhysicsManager)
    private boolean isGrounded = false; // ???

    float SPEED_X = 8f;
    boolean[] move = {
            false, // left
            false, // right
            false, // jump
            false, // sit
            false, // teleport
    };
    GameObject platform;
    float previousPlatformX = 0;

    int crouchHeight, realHeight;

    float lineX, lineY, mouseX, mouseY;
    boolean rodOfDiscord = true;

    // player abilities
    boolean stickyCeilings = false;
    int gunCurrent = 1;

    // animation
    int frame = 0;
    BufferedImage states[], walk[];
    boolean isRight = true;

    public EntityPlayer(float x, float y, int width, int height, GameEngine engine, EntityManager em, CameraManager cm) {
        super(x, y, width, height);

        engine.addKeyListener(this);
        engine.addMouseListener(this);
        engine.addMouseMotionListener(this);
        engine.addMouseWheelListener(this);

        this.e = em.objects; this.ce = em.culledObjects; this.pe = em.particleObjects;
        this.engine = engine; this.cm = cm; this.em = em; pm = new PhysicsManager(em);
        guns = new GunManager(em); im = new InventoryManager(this, em);

        realHeight = height;
        crouchHeight = height/2;
        velX = 2;
        velY = 0;
        type = ENTITY_TYPE.Player;
        color = Color.CYAN;
        hp = baseHP = 1000;

        // assets
        states = AssetManager.playerStatesFinal;
        walk = AssetManager.playerWalkFinal;
    }

    @Override
    public void tick() {
        Game.text = String.format("x: %f, y: %f, velX: %f, velY: %f", x, y, velX, velY);

        // very accurate target mouse cursor, use this on tick for inaccuracy while moving
        // FIXED: becomes inaccurate on resolution change
        // STATUS: 7 FUCKING DAYS, THIS SHIT TOOK ME 7 FUCKING DAYS, I'M DONE.
        double scalingFactorX = (double) Window.SCALED_WIDTH() / 720;
        double scalingFactorY = (double) Window.SCALED_HEIGHT() / 480;

        int translationX = (Window.REAL_WIDTH() - Window.SCALED_WIDTH()) / 2;
        int translationY = (Window.REAL_HEIGHT() - Window.SCALED_HEIGHT()) / 2;

        lineX = (int) (((mouseX - translationX) / scalingFactorX) / cm.sx - (-x - width / 2 + (Window.SCALED_WIDTH() / (scalingFactorX * cm.sx)) / 2));
        lineY = (int) (((mouseY - translationY) / scalingFactorY) / cm.sy - (-y - height / 2 + (Window.SCALED_HEIGHT() / (scalingFactorY * cm.sy)) / 2));

        // just jump
        if (move[2]) {
            velY = pm.velYJump(velY);
        }

        velY = pm.velYGravity(velY);
        y += (int)velY;

        if (move[3]) height = crouchHeight;
        else height = realHeight;

        // fixed: buggy collision on moving blocks
        // fixme: shrinking bug
        GameObject tempPlatform = null;
        GameObject[] hits = pm.collisionY(this);
        // fun: sticky ceilings
        if (stickyCeilings && (hits[0] != null || hits[1] != null))
            pm.isJumping = false;
        if (hits[0] != null) {
            pm.isJumping = false;
            if (!move[2]) tempPlatform = hits[0];
        }

        // move x
        if (move[0] || move[1]) {
            if (move[0]) velX = SPEED_X;
            if (move[1]) velX = -SPEED_X;
            x += (int)velX;
        }
        pm.collisionX(this);

        // platform galilean transform
        // fixme: be realistic physics
        if (move[2] || velY > 2) // idk why but it works
            platform = null;
        else if (tempPlatform != null)
            platform = tempPlatform;

        if (platform != null) {
            if (platform.getX() + platform.getWidth() > x && platform.getX() < x + width)
                y = platform.getY() - height;
            // TODO: calculate platform x
            /*if (previousPlatformX == 0) previousPlatformX = x - platform.getX();
            if (!move[0] && !move[1]) x = platform.getX() + previousPlatformX;
            else previousPlatformX = 0;*/
        } else previousPlatformX = 0;

        // shooting codes
        if (guns.isShooting && guns.cooldown < 0) {
            if (!(move[0] || move[1])) isRight = x + width / 2 < lineX;
            guns.cooldown = guns.defaultCooldown;
            guns.shootCodes(this, lineX, lineY, ENTITY_TYPE.Enemy);
        }
        guns.cooldown--;

        // FIXME: move this somewhere else, must be called after everything is done
        cm.cameraControls((-x - width/2 + (720/cm.sx)/2), (-y - height/2 + (480/cm.sy)/2), cm.sx, cm.sy);

        if ((move[0] || move[1] || move[2] || pm.isJumping)) {
            if (!AssetManager.renderAssets) em.addParticles(new Particle_Trail_Rect(x, y, width, height, color, em));
            else em.addParticles(new Particle_Trail_Sprite(x, y, width, height, em, sprite));
        }
    }

    @Override
    public void render(Graphics g) {
        // hp indicator
        g.setColor(Color.GREEN);
        g.fillRect((int)x-width, (int)y-width, (int)(hp*(width/2*6)/baseHP), width/2);
        g.setColor(Color.BLACK);
        for (int i = 0; i < 6; i++)
            g.drawRect((int)x-width+(width/2)*i, (int)y-width, width/2, width/2);

        // player
        g.setColor(color);
        if (!AssetManager.renderAssets) {
            g.fillRect((int) x, (int) y, width, height);
        }
        else {
            int face = isRight ? 1 : 0; // isRight
            // jump
            if (velY < 0) {
                sprite = renderSprite(states, 2 + face, g);
            }
            // fall
            else if (velY > 2) {
                sprite = renderSprite(states, 4 + face, g);
            }
            // idle
            else if (!(move[0] || move[1])) {
                sprite = renderSprite(states, face, g);
            } else {
                // walk
                sprite = renderSprite(walk, frame + face, g);
                frame += 2;
                if (frame >= walk.length) frame = 0;
            }
        }

        if (StateManager.DEV_HITBOX_ESP_LINES) {
            g.drawRect((int) x, (int) y, width, height);
            Graphics2D g2d = (Graphics2D) g;
            float cx = x + width/2, cy = y + height/2, r = 200, theta = (float)Math.toRadians(22);

            // Calculate the angle between the center and point (x2, y2)
            double angle = Math.atan2(lineY - cy, lineX - cx);

            // Draw the cone slice
            double x1 = cx + r * Math.cos(angle - theta);
            double y1 = cy + r * Math.sin(angle - theta);
            double x3 = cx + r * Math.cos(angle + theta);
            double y3 = cy + r * Math.sin(angle + theta);

            GeneralPath coneSlice = new GeneralPath();
            coneSlice.moveTo(cx, cy);
            coneSlice.lineTo(x1, y1);
            coneSlice.curveTo(x1, y1,cx + r * Math.cos(angle) * 1.1, cy + r * Math.sin(angle) * 1.1, x3, y3); // FIXME: * 1.1
            coneSlice.lineTo(x3, y3);
            coneSlice.closePath();
            g2d.setColor(Color.RED);
            for (GameObject o : ce) {
                if (this != o && coneSlice.intersects(o.getBounds().getBounds()) &&
                        !(o.type == ENTITY_TYPE.Bullet || o.type == ENTITY_TYPE.Area)) {
                    g2d.setColor(Color.GREEN);
                }
            }
            g2d.draw(coneSlice);
            // Draw the circle
            g2d.draw(new Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r));

            // Define the dash pattern (in this case, a 5-pixel dashed line)
            g2d.setColor(Color.GREEN);
            float[] dashPattern = { 10f };
            // Create a dashed stroke with the specified dash pattern
            BasicStroke dashedStroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0f);
            // Set the custom stroke in the Graphics2D object
            Stroke old = g2d.getStroke();
            g2d.setStroke(dashedStroke);
            g2d.drawLine((int) this.x + width/2, (int) this.y + height/2, (int) lineX, (int) lineY);
            g2d.setStroke(old);
        }
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D: {
                isRight = true;
                move[0] = true;
                break;
            }
            case KeyEvent.VK_A: {
                isRight = false;
                move[1] = true;
                break;
            }
            case KeyEvent.VK_S: {
                move[3] = true;
                break;
            }
            case KeyEvent.VK_SPACE: {
                move[2] = true;
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D: {
                frame = 0;
                move[0] = false;
                break;
            }
            case KeyEvent.VK_A: {
                frame = 0;
                move[1] = false;
                break;
            }
            case KeyEvent.VK_S: {
                move[3] = false;
                break;
            }
            case KeyEvent.VK_SPACE: {
                if (velY < 0) velY = pm.GRAVITY;
                // fun: glide when spam
                // velY = 0;
                move[2] = false;
                // fun: double jump? (see PhysicsManager velYJump())
                // pm.isJumping = false;
                break;
            }
            case KeyEvent.VK_Z: {
                guns.currentLoadOut = guns.loadOut[gunCurrent++%guns.loadOut.length];
                break;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            guns.isShooting = true;
            // this.e.add(new Arkhalis(lineX-50, lineY-50, 100, 100, em, type, ENTITY_TYPE.Enemy));
        }
        if (e.getButton() == MouseEvent.BUTTON3 && rodOfDiscord) {
            platform = null;
            x = lineX - width / 2;
            y = lineY - height / 2;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            guns.isShooting = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!(move[0] || move[1])) isRight = x + width / 2 < lineX;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!(move[0] || move[1])) isRight = x + width / 2 < lineX;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
