package karakters.managers;

import karakters.GameEngine;
import karakters.ImageProcessing;
import karakters.Window;
import karakters.entities.players.EntityPlayer;
import karakters.entities.blocks.Block;
import karakters.enums.ENTITY_TYPE;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import static karakters.GameEngine.throwFrames;
import static karakters.GameEngine.throwTick;

public class GameManager implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {
    public GameEngine engine;
    public Window w;
    public AssetManager as;
    public EntityManager em;
    public WorldManager wm;
    public CameraManager cm;
    public StateManager sm;
    public GUIManager gui;
    int spawnX = 720/2-32, spawnY = -128; // (720/2-32, 0)

    public GameManager(GameEngine engine) {
        this.engine = engine;
        as = new AssetManager();
        em = new EntityManager();
        wm = new WorldManager(em, engine);
        cm = new CameraManager();
        sm = new StateManager();
        gui = new GUIManager(this);

        engine.addKeyListener(this);
        engine.addMouseMotionListener(this);
        engine.addMouseListener(this);
        engine.addMouseWheelListener(this);

        // for (int i = 0; i < 20; i++) em.objects.add(new Entity(100, 100, 32, 16, em.objects));
        em.objects.add(new Block(480/4, 480/2-32, 480, 64, Color.RED, wm));
        em.objects.add(new Block(720/2-32, 480/4, 64, 480/2, Color.RED, wm));
        em.objects.add(new EntityPlayer(spawnX, spawnY, 16, 32, engine, em, cm));

        w = new Window(engine, 720, 480, "The Karakters Kompany");
    }
    public void tick() {
        w.setTitle(String.format(
                "The Karakters Kompany [F:%d T:%d] [A:%d C:%d P:%d W: %d]",
                throwFrames, throwTick,
                em.objects.size(), em.culledObjects.size(), em.particleObjects.size(), em.wallObjects.size()));

        wm.tick();
        em.tick();
    }
    public void render(Graphics g) {
        cm.render(g);
        wm.render(g);
        em.render(g);
    }
    public void renderLate(Graphics g) {
        gui.render(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // utils
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER: {
                /*velY = 0;
                x = 720/2-32;
                y = 0;*/
                em.objects.removeIf(o -> o.type == ENTITY_TYPE.Player);
                em.objects.add(new EntityPlayer(spawnX, spawnY, 16, 32, engine, em, cm));
                break;
            }
            case KeyEvent.VK_F11: {
                // change
                StateManager.GAME_RESOLUTION = (StateManager.GAME_RESOLUTION+1)%StateManager.GAME_RESOLUTIONS.length;
                StateManager.GAME_WIDTH = StateManager.GAME_RESOLUTIONS[StateManager.GAME_RESOLUTION][0];
                StateManager.GAME_HEIGHT = StateManager.GAME_RESOLUTIONS[StateManager.GAME_RESOLUTION][1];

                // log
                /*System.out.println("GAME: "+ StateManager.GAME_WIDTH +"x"+ StateManager.GAME_HEIGHT);
                System.out.println("RESIZE: "+Window.SCALED_WIDTH()+"x"+Window.SCALED_HEIGHT());
                System.out.println("WINDOW: "+ Window.REAL_WIDTH()+"x"+ Window.REAL_HEIGHT()+"\n");
                System.out.println(String.format("Width+scale: %f\nHeight+scale: %f", Window.SCALED_WIDTH()/s, Window.SCALED_HEIGHT()/s));*/
                break;
            }
            case KeyEvent.VK_0: {
                ImageProcessing.trigger();
                break;
            }
            case KeyEvent.VK_9: {
                em.enableParticles = !em.enableParticles;
                break;
            }
            case KeyEvent.VK_8: {
                AssetManager.renderAssets = !AssetManager.renderAssets;
                break;
            }
            case KeyEvent.VK_7: {
                Random r = new Random();
                Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
                Color cb = wm.wallColor(c);
                wm.brick = AssetManager.brickMaskCopy(AssetManager.blocks[8], c);
                wm.brickWall = AssetManager.brickMaskCopy(AssetManager.blocks[8], cb);
                break;
            }
            case KeyEvent.VK_6: {
                GunManager.enemiesUseGuns = !GunManager.enemiesUseGuns;
                break;
            }
            case KeyEvent.VK_5: {
                StateManager.DEV_HITBOX_ESP_LINES = !StateManager.DEV_HITBOX_ESP_LINES;
                break;
            }
            case KeyEvent.VK_4: {
                StateManager.DEV_FREEZE_BOTS = !StateManager.DEV_FREEZE_BOTS;
                StateManager.DEV_FREEZE_WORLD = !StateManager.DEV_FREEZE_WORLD;
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() == 1) cm.sx = cm.sy *= .5f;
        if (e.getWheelRotation() == -1) cm.sx = cm.sy /= .5f;
    }
}
