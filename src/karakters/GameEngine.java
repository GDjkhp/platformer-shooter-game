package karakters;

import javax.swing.*;
import java.awt.*;

public abstract class GameEngine extends Canvas implements Runnable {
    // Game loop variables
    public Thread thread;
    public boolean running = false;
    public static boolean paused = false;
    public static int throwFrames, throwTick;
    public static boolean smoothFix = false;
    public static boolean multiThreaded = false;

    // main game loop vars fix
    public long lastTime = System.nanoTime();
    public double delta = 0;
    static double amountOfTicks = 100.0;
    public static double ns = 1000000000 / amountOfTicks;

    public void run() {
        this.requestFocus();
        if (multiThreaded) multiThread();
        else singleThread();
        stop();
    }

    // thread utils
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // single thread hell
    private void singleThread() {
        long timer = System.currentTimeMillis();
        int frames = 0;

        int tick = 0;
        long timerTick = System.currentTimeMillis();

        // Introduce a slow-motion factor
        // 1.0 for normal speed, ~~lower~~ higher values for slow motion (1 = 100 ticks, 2 = 50 ticks, 3 = 33 ticks)
        double slowMotionFactor = 1;

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / (ns * slowMotionFactor);
            lastTime = now;
            try {
                if (!smoothFix) {
                    while (delta >= 1) {
                        tick();
                        delta--;
                        tick++;
                    }
                }
                else {
                    tick();
                    tick++;
                }
                if (System.currentTimeMillis() - timerTick > 1000) {
                    timerTick += 1000;
                    throwTick = tick;
                    tick = 0;
                }

                render();
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    throwFrames = frames;
                    frames = 0;
                }
            } catch (Exception e) {
                handleError(e);
            }
        }
    }

    // multi thread hell
    private void multiThread() {
        // Initialize logic and rendering threads
        Thread logicThread = new Thread(this::logicLoop);
        Thread renderingThread = new Thread(this::renderLoop);

        logicThread.start();
        renderingThread.start();

        running = true;

        try {
            logicThread.join();
            renderingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void logicLoop() {
        int tick = 0;
        long timerTick = System.currentTimeMillis();
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            try {
                if (!smoothFix) {
                    while (delta >= 1) {
                        tick();
                        delta--;
                        tick++;
                    }
                }
                else {
                    tick();
                    tick++;
                }
                if (System.currentTimeMillis() - timerTick > 1000) {
                    timerTick += 1000;
                    throwTick = tick;
                    tick = 0;
                }
            } catch (Exception e) {
                handleError(e);
            }
        }
    }
    private void renderLoop() {
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            try {
                render();
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    throwFrames = frames;
                    frames = 0;
                }
            } catch (Exception e) {
                handleError(e);
            }
        }
    }

    // TODO: MERGE EVERY ERROR HANDLER HERE
    public void handleError(Exception e) {
        e.printStackTrace();
        int a = JOptionPane.showConfirmDialog(null, "An error occurred: " + e + ", " +
                "\ndo you still wish to continue?", "weird shit happened!", JOptionPane.YES_NO_OPTION);
        if (a == JOptionPane.NO_OPTION) System.exit(0);
        // FIXME: if error caught but continued, still work in progress
    }

    public abstract void tick();
    public abstract void render();
}