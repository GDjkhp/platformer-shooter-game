package karakters.managers;

import karakters.GameObject;
import karakters.entities.guns.*;
import karakters.enums.ENTITY_TYPE;
import karakters.enums.GUN_ID;

import java.util.List;

import static karakters.enums.GUN_ID.*;

public class GunManager {
    public GUN_ID[] loadOut = new GUN_ID[] {
            Default,
            Chlorophyte,
            Electrocute,
            Ricochet,
            // Arkhalis,
            // Rocket,
            // Whip,
            Arrow,
    };
    public GUN_ID currentLoadOut = GUN_ID.Default;
    public int cooldown = 0, defaultCooldown = 10, reloadCooldown = 0, reloadDefaultTime = 100, // rate of fire, reload obv
            currentAmmo = 30, maxAmmo = 30, currentMag = 90, maxMag = 90, totalBulletsShot = 0; // magazine cap, reserve ammo limit, statistics
    public boolean isShooting = false, isReloading = false, tripleBullets = true;

    public static boolean enemiesUseGuns = true;
    List<GameObject> e, ce, pe;
    EntityManager em;
    public GunManager(EntityManager em) {
        this.e = em.objects; this.ce = em.culledObjects; this.pe = em.particleObjects;
        this.em = em;
    }

    // what
    public GUN_ID switchWeapon(GUN_ID current) {
        System.out.println(GUN_ID.values()[0]);
        return GUN_ID.Default;
    }

    public void shootCodes(GameObject entity, float pointX, float pointY, ENTITY_TYPE target) {
        totalBulletsShot++;
        int width = 16, height = 16;
        int area = 750;

        float x = entity.getX() + entity.getWidth()/2 - width/2, y = entity.getY() + entity.getHeight()/2 - height/2;
        ENTITY_TYPE shooter = entity.type;

        // Calculate direction vector between player and mouse
        float dirX = pointX - x - width/2;
        float dirY = pointY - y - height/2;

        // Calculate the magnitude of the direction vector
        float magnitude = (float)Math.sqrt(dirX * dirX + dirY * dirY);

        // Normalize the direction vector to get a unit direction
        float unitDirX = dirX / magnitude;
        float unitDirY = dirY / magnitude;

        // Scale factor for bullet velocities
        float bulletSpeed = 15; // Adjust this to control bullet speed
        float spreadAngle = (float)Math.toRadians(22); // Adjust this to control bullet spread angle

        // collect calculations to array, i'm tired
        float[] velocities = new float[]{
                // Middle
                unitDirX * bulletSpeed, unitDirY * bulletSpeed,
                // Right
                (float)(unitDirX * Math.cos(spreadAngle) - unitDirY * Math.sin(spreadAngle)) * bulletSpeed,
                (float)(unitDirX * Math.sin(spreadAngle) + unitDirY * Math.cos(spreadAngle)) * bulletSpeed,
                // Left
                (float)(unitDirX * Math.cos(-spreadAngle) - unitDirY * Math.sin(-spreadAngle)) * bulletSpeed,
                (float)(unitDirX * Math.sin(-spreadAngle) + unitDirY * Math.cos(-spreadAngle)) * bulletSpeed
        };

        // FIXED: concurrent modification error, use CopyOnWriteArrayList (thread safe collections)
//        e.add(new Bullet(x, y, width, height, 0, -5, e));
//        e.add(new Bullet(x, y, width, height, 5, -5, e));
//        e.add(new Bullet(x , y, width, height, -5, -5, e));

        switch (currentLoadOut) {
            case Default: {
                // Create bullets with different velocities
                e.add(new Bullet(x, y, width, height, velocities[0], velocities[1], em,
                        shooter, target));
                if (tripleBullets) {
                    e.add(new Bullet(x, y, width, height, velocities[2], velocities[3], em,
                            shooter, target));
                    e.add(new Bullet(x, y, width, height, velocities[4], velocities[5], em,
                            shooter, target));
                }
                break;
            }
            case Chlorophyte: {
                e.add(new Chloropyte(x, y, width, height, em, area, velocities[0], velocities[1],
                        shooter, target));
                if (tripleBullets) {
                    e.add(new Chloropyte(x, y, width, height, em, area, velocities[2], velocities[3],
                            shooter, target));
                    e.add(new Chloropyte(x, y, width, height, em, area, velocities[4], velocities[5],
                            shooter, target));
                }
                break;
            }
            case Electrocute: {
                e.add(new Electrocute(x, y, width, height, em, area, shooter, target,
                        velocities[0], velocities[1], null, null));
                if (tripleBullets) {
                    e.add(new Electrocute(x, y, width, height, em, area, shooter, target,
                            velocities[2], velocities[3],
                            null, null));
                    e.add(new Electrocute(x, y, width, height, em, area, shooter, target,
                            velocities[4], velocities[5],
                            null, null));
                }
                break;
            }
            case Ricochet: {
                e.add(new Ricochet(x, y, width, height, em, shooter, target,
                        velocities[0], velocities[1]));
                if (tripleBullets) {
                    e.add(new Ricochet(x, y, width, height, em, shooter, target,
                            velocities[2], velocities[3]));
                    e.add(new Ricochet(x, y, width, height, em, shooter, target,
                            velocities[4], velocities[5]));
                }
                break;
            }
            case Arkhalis: {
                e.add(new Arkhalis(x+entity.getWidth()/2-50, y+entity.getHeight()/2-50, 100, 100, em, shooter, target));
                break;
            }
            case Rocket: {
                e.add(new Rocket(x, y, width, height, velocities[0], velocities[1], em, shooter, target, area));
                break;
            }
            case Whip: {
                e.add(new Whip(pointX, pointY, 64, 64, shooter, target, em, entity));
                break;
            }
            case Arrow: {
                // Create bullets with different velocities
                e.add(new Arrow(x, y, width, height, velocities[0], velocities[1], em,
                        shooter, target));
                if (tripleBullets) {
                    e.add(new Arrow(x, y, width, height, velocities[2], velocities[3], em,
                            shooter, target));
                    e.add(new Arrow(x, y, width, height, velocities[4], velocities[5], em,
                            shooter, target));
                }
                break;
            }
        }
    }
}
