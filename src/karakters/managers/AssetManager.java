package karakters.managers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AssetManager {
    private BufferedImage amongus, old, another_one, slash_sprites, tile_sprites;

    public static BufferedImage[] playerStates, playerWalk, enemyWalk, enemyIdle, slash, blocks, ghost,
            playerStatesFinal, playerWalkFinal, enemyWalkFinal, blocksFinal, ghostFinal;
    public static boolean renderAssets = true;

    public AssetManager() {
        amongus = loadImage("res/sprites.png");
        old = loadImage("res/gamespritesheet.png");
        slash_sprites = loadImage("res/slash sprites tiger.png");
        tile_sprites = loadImage("res/tile textures.png");
        switchMasksSlash(slash_sprites, Color.WHITE); // sneaky

        // image
        blocks = new BufferedImage[] {
                grabImage(tile_sprites, 0, 0, 18, 18),
                grabImage(tile_sprites, 0, 18, 18, 18),
                grabImage(tile_sprites, 0, 18*2, 18, 18),
                grabImage(tile_sprites, 0, 18*3, 18, 18),
                grabImage(tile_sprites, 18, 0, 18, 18),
                grabImage(tile_sprites, 18, 18, 18, 18),
                grabImage(tile_sprites, 18, 18*2, 18, 18),
                grabImage(tile_sprites, 18, 18*3, 18, 18),
                grabImage(tile_sprites, 18*2, 0, 18, 18),
        };
        // brickMask(blocks[8], Color.GREEN);

        playerStates = new BufferedImage[] {
                grabImage(amongus, 2000 - 150, (186+168+191)*2, 150, 198), // stand
                grabImage(amongus, 2000 - 154, (186+168)*2, 154, 191), // jump
                grabImage(amongus, 2000 - 157, (186)*2, 157, 168), // fall
                grabImage(amongus, 2000 - 178, 0, 178, 186), // sit
        };
        playerWalk = new BufferedImage[] {
                grabImage(amongus, 0, 0, 167, 231),
                grabImage(amongus, 0, 231, 167, 231),
                grabImage(amongus, 0, 231*2, 167, 231),
                grabImage(amongus, 0, 231*3, 167, 231),
                grabImage(amongus, 0, 231*4, 167, 231),
                grabImage(amongus, 0, 231*5, 167, 231),
                grabImage(amongus, 0, 231*6, 167, 231),
                grabImage(amongus, 0, 231*7, 167, 231),
                grabImage(amongus, 167, 0, 167, 231),
                grabImage(amongus, 167, 231, 167, 231),
                grabImage(amongus, 167, 231*2, 167, 231),
                grabImage(amongus, 167, 231*3, 167, 231),
        };
        enemyWalk = new BufferedImage[] {
                grabImage(amongus, 167*2, 0, 221, 292),
                grabImage(amongus, 167*2, 292, 221, 292),
                grabImage(amongus, 167*2, 292*2, 221, 292),
                grabImage(amongus, 167*2, 292*3, 221, 292),
                grabImage(amongus, 167*2, 292*4, 221, 292),
                grabImage(amongus, 167*2, 292*5, 221, 292),
                grabImage(amongus, 167*2+221, 0, 221, 292),
                grabImage(amongus, 167*2+221, 292, 221, 292),
                grabImage(amongus, 167*2+221, 292*2, 221, 292),
                grabImage(amongus, 167*2+221, 292*3, 221, 292),
                grabImage(amongus, 167*2+221, 292*4, 221, 292),
                grabImage(amongus, 167*2+221, 292*5, 221, 292),
                grabImage(amongus, 167*2+221*2, 0, 221, 292),
        };
        ghost = new BufferedImage[] {
                grabImage(amongus, 167*2+221*3+166*2, 0, 188, 221),
                grabImage(amongus, 167*2+221*3+166*2, 221, 188, 221),
                grabImage(amongus, 167*2+221*3+166*2, 221*2, 188, 221),
                grabImage(amongus, 167*2+221*3+166*2, 221*3, 188, 221),
                grabImage(amongus, 167*2+221*3+166*2+188, 0, 188, 221),
                grabImage(amongus, 167*2+221*3+166*2+188, 221, 188, 221),
                grabImage(amongus, 167*2+221*3+166*2+188, 221*2, 188, 221),
                grabImage(amongus, 167*2+221*3+166*2+188, 221*3, 188, 221),
        };

        // compile/gather everything here (java.lang.OutOfMemoryError: Java heap space)
        ghostFinal = new BufferedImage[] {
                flipHorizontal(ghost[0]), ghost[0],
                flipHorizontal(ghost[1]), ghost[1],
                flipHorizontal(ghost[2]), ghost[2],
                flipHorizontal(ghost[3]), ghost[3],
                flipHorizontal(ghost[4]), ghost[4],
                flipHorizontal(ghost[5]), ghost[5],
                flipHorizontal(ghost[6]), ghost[6],
                flipHorizontal(ghost[7]), ghost[7],
        };

        enemyWalkFinal = new BufferedImage[] {
                flipHorizontal(enemyWalk[0]), enemyWalk[0],
                flipHorizontal(enemyWalk[1]), enemyWalk[1],
                flipHorizontal(enemyWalk[2]), enemyWalk[2],
                flipHorizontal(enemyWalk[3]), enemyWalk[3],
                flipHorizontal(enemyWalk[4]), enemyWalk[4],
                flipHorizontal(enemyWalk[5]), enemyWalk[5],
                flipHorizontal(enemyWalk[6]), enemyWalk[6],
                flipHorizontal(enemyWalk[7]), enemyWalk[7],
                flipHorizontal(enemyWalk[8]), enemyWalk[8],
                flipHorizontal(enemyWalk[9]), enemyWalk[9],
                flipHorizontal(enemyWalk[10]), enemyWalk[10],
                flipHorizontal(enemyWalk[11]), enemyWalk[11],
                flipHorizontal(enemyWalk[12]), enemyWalk[12],
        };
        playerStatesFinal = new BufferedImage[] {
                flipHorizontal(playerStates[0]), playerStates[0], // idle
                flipHorizontal(playerStates[1]), playerStates[1], // jump
                flipHorizontal(playerStates[2]), playerStates[2], // fall
                flipHorizontal(playerStates[3]), playerStates[3], // sit
        };
        playerWalkFinal = new BufferedImage[] {
                flipHorizontal(playerWalk[0]), playerWalk[0],
                flipHorizontal(playerWalk[1]), playerWalk[1],
                flipHorizontal(playerWalk[2]), playerWalk[2],
                flipHorizontal(playerWalk[3]), playerWalk[3],
                flipHorizontal(playerWalk[4]), playerWalk[4],
                flipHorizontal(playerWalk[5]), playerWalk[5],
                flipHorizontal(playerWalk[6]), playerWalk[6],
                flipHorizontal(playerWalk[7]), playerWalk[7],
                flipHorizontal(playerWalk[8]), playerWalk[8],
                flipHorizontal(playerWalk[9]), playerWalk[9],
                flipHorizontal(playerWalk[10]), playerWalk[10],
                flipHorizontal(playerWalk[11]), playerWalk[11],
        };

        for (BufferedImage i : playerWalkFinal) {
            switchMasks(i, Color.CYAN);
        }
        for (BufferedImage i : playerStatesFinal) {
            switchMasks(i, Color.CYAN);
        }
        for (BufferedImage i : enemyWalkFinal) {
            switchMasks(i, Color.MAGENTA);
        }
        for (BufferedImage i : ghostFinal) {
            switchMasks(i, Color.YELLOW);
        }

        slash = new BufferedImage[] {
                grabImage(slash_sprites, 0, 0, 268, 175),
                grabImage(slash_sprites, 0, 175, 268, 175),
                grabImage(slash_sprites, 0, 175*2, 268, 175),
                grabImage(slash_sprites, 0, 175*3, 268, 175),
                grabImage(slash_sprites, 0, 175*4, 268, 175),
                grabImage(slash_sprites, 0, 175*5, 268, 175),
                grabImage(slash_sprites, 0, 175*6, 268, 175),
                grabImage(slash_sprites, 0, 175*7, 268, 175),
        };
    }

    public BufferedImage loadImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public BufferedImage grabImage(BufferedImage image, int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    // Unfortunately the performance of getScaledInstance() is very poor if not problematic.
    public static Image scaleImage(BufferedImage img, float width, float height) {
        return img.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
    }

    public static Image scaleImageByPercent(BufferedImage img, float widthPercent, float heightPercent) {
        float width = img.getWidth(null) * (widthPercent * 0.01f);
        float height = img.getHeight(null) * (heightPercent * 0.01f);
        return img.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
    }

    public static double[] calculateScaleFactors(int originalWidth, int originalHeight, int targetWidth, int targetHeight) {
        double scaleX = (double) targetWidth / originalWidth;
        double scaleY = (double) targetHeight / originalHeight;

        // Choose the minimum scale factor to maintain the aspect ratio
        double scaleFactor = Math.min(scaleX, scaleY);

        // Calculate the new width and height
        int newWidth = (int) (originalWidth * scaleFactor);
        int newHeight = (int) (originalHeight * scaleFactor);

        return new double[]{scaleFactor, newWidth, newHeight};
    }

    public static BufferedImage flipHorizontal(BufferedImage i) {
        // Flip the image horizontally
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-i.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(i, null);
    }

    public static void switchMasks(BufferedImage image, Color rgb) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // Change the red mask to the specific color
                if (r > 128 && r != b) {
                    r = rgb.getRed();
                    g = rgb.getGreen();
                    b = rgb.getBlue();
                }
                else if (b > 128 && r != b) {
                    r = Math.max(0, rgb.getRed()  -75);
                    g = Math.max(0, rgb.getGreen()-75);
                    b = Math.max(0, rgb.getBlue() -75);
                }

                int newPixel = (a << 24) | (r << 16) | (g << 8) | b;

                // fun: blue comic shader?
                /*a = (p >> 24) & 0xff;
                r = (p >> 16) & 0xff;
                g = (p >> 16) & 0xff;
                b = (p >> 16) & 0xff;
                newPixel = (a << 24) | (r & rgb.getRed()) << 16 | (g & rgb.getGreen()) << 8 | rgb.getBlue()/2;*/

                // Set the modified pixel in the result image
                image.setRGB(x, y, newPixel);
            }
        }
    }

    public static void switchMasksSlash(BufferedImage image, Color rgb) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // Change the red mask to the specific color
                if (r > 128) {
                    r = rgb.getRed();
                    g = rgb.getGreen();
                    b = rgb.getBlue();
                }

                int newPixel = (a << 24) | (r << 16) | (g << 8) | b;

                // fun: blue comic shader?
                /*a = (p >> 24) & 0xff;
                r = (p >> 16) & 0xff;
                g = (p >> 16) & 0xff;
                b = (p >> 16) & 0xff;
                newPixel = (a << 24) | (r & rgb.getRed()) << 16 | (g & rgb.getGreen()) << 8 | rgb.getBlue()/2;*/

                // Set the modified pixel in the result image
                image.setRGB(x, y, newPixel);
            }
        }
    }

    public static void brickMask(BufferedImage image, Color rgb) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r, pr;
                r = pr = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // white
                if (r == g && r == b) {
                    r = Math.max(0, rgb.getRed()   - 75) & pr;
                    g = Math.max(0, rgb.getGreen() - 75) & pr;
                    b = Math.max(0, rgb.getBlue()  - 75) & pr;
                }
                // color
                else {
                    r = rgb.getRed() & pr;
                    g = rgb.getGreen() & pr;
                    b = rgb.getBlue() & pr;
                }

                int newPixel = (a << 24) | (r << 16) | (g << 8) | b;

                // Set the modified pixel in the result image
                image.setRGB(x, y, newPixel);
            }
        }
    }

    public static BufferedImage switchMasksCopy(BufferedImage image, Color rgb) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // Change the red mask to the specific color
                if (r > 128 && r != b) {
                    r = rgb.getRed();
                    g = rgb.getGreen();
                    b = rgb.getBlue();
                }
                else if (b > 128 && r != b) {
                    r = Math.max(0, rgb.getRed()  -75);
                    g = Math.max(0, rgb.getGreen()-75);
                    b = Math.max(0, rgb.getBlue() -75);
                }

                int newPixel = (a << 24) | (r << 16) | (g << 8) | b;

                // fun: blue comic shader?
                /*a = (p >> 24) & 0xff;
                r = (p >> 16) & 0xff;
                g = (p >> 16) & 0xff;
                b = (p >> 16) & 0xff;
                newPixel = (a << 24) | (r & rgb.getRed()) << 16 | (g & rgb.getGreen()) << 8 | rgb.getBlue()/2;*/

                // Set the modified pixel in the result image
                img.setRGB(x, y, newPixel);
            }
        }

        return img;
    }

    public static BufferedImage brickMaskCopy(BufferedImage image, Color rgb) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r, pr;
                r = pr = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // white
                if (r == g && r == b) {
                    r = Math.max(0, rgb.getRed()   - 75) & pr;
                    g = Math.max(0, rgb.getGreen() - 75) & pr;
                    b = Math.max(0, rgb.getBlue()  - 75) & pr;
                }
                // color
                else {
                    r = rgb.getRed() & pr;
                    g = rgb.getGreen() & pr;
                    b = rgb.getBlue() & pr;
                }

                int newPixel = (a << 24) | (r << 16) | (g << 8) | b;

                // Set the modified pixel in the result image
                img.setRGB(x, y, newPixel);
            }
        }

        return img;
    }
}
