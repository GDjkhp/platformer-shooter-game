package karakters.entities.particles;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.EntityManager;

import java.awt.*;
import java.util.List;

public class Particle_Trail_Circle extends GameObject {
    private float alpha = 1;
    private List<GameObject> e;
    private float life = 0.1f;

    public Particle_Trail_Circle(float x, float y, int width, int height, Color color, EntityManager em) {
        super(x, y, width, height);
        this.color = color;
        this.e = em.particleObjects;
        type = ENTITY_TYPE.Particle;
    }

    @Override
    public void tick() {
        if (alpha > life) {
            alpha -= (life - 0.0001f);
        } else e.remove(this);
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(makeTransparent(alpha));
        g.setColor(color);
        g.fillOval((int) x, (int) y, width, height);
        g2d.setComposite(makeTransparent(1));
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    private AlphaComposite makeTransparent(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return AlphaComposite.getInstance(type, alpha);
    }
}
