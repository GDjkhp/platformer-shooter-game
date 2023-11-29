package karakters.entities.particles;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.CameraManager;
import karakters.managers.EntityManager;

import java.awt.*;
import java.util.List;

public class Particle_Basic_Circle extends GameObject {
    List<GameObject> e;
    EntityManager em;
    public Particle_Basic_Circle(float x, float y, int width, int height, float velX, float velY, EntityManager em, Color c) {
        super(x, y, width, height);
        type = ENTITY_TYPE.Particle;
        this.velX = velX;
        this.velY = velY;
        this.em = em;
        color = c;
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
        if (em.staticParticles) em.addParticles(new Particle_Trail_Circle(x, y, width, height, color, em));
        if (!CameraManager.screen.intersects(getBounds())) em.particleObjects.remove(this);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval((int) x, (int) y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
