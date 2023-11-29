package karakters.entities.particles;

import karakters.GameObject;
import karakters.enums.ENTITY_TYPE;
import karakters.managers.EntityManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Particle_Trail_Sprite extends GameObject {
    private float alpha = 1;
    private List<GameObject> e;
    private float life = 0.1f;

    public Particle_Trail_Sprite(float x, float y, int width, int height, EntityManager em, BufferedImage sprite) {
        super(x, y, width, height);
        this.e = em.particleObjects;
        type = ENTITY_TYPE.Particle;
        this.sprite = sprite;
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
        if (sprite != null) renderSprite(sprite, g);
        g2d.setComposite(makeTransparent(1));
    }

    private AlphaComposite makeTransparent(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return AlphaComposite.getInstance(type, alpha);
    }

    @Override
    public Shape getBounds() {
        return null;
    }
}
