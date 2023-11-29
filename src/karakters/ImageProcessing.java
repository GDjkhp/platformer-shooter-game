package karakters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.HashMap;

public class ImageProcessing {
    public static BufferedImage negative(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x,y);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int gR = (p>>8)&0xff;
                int b = p&0xff;
                r = 255 - r;
                gR = 255 - gR;
                b = 255 - b;
                p = (a<<24) | (r<<16) | (gR<<8) | b;
                image.setRGB(x, y, p);
            }
        }
        return image;
    }
    public static BufferedImage sepia(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int p = image.getRGB(x,y);
                int a = (p>>24)&0xff;
                int R = (p>>16)&0xff;
                int G = (p>>8)&0xff;
                int B = p&0xff;
                int newRed = (int)(0.393*R + 0.769*G + 0.189*B);
                int newGreen = (int)(0.349*R + 0.686*G + 0.168*B);
                int newBlue = (int)(0.272*R + 0.534*G + 0.131*B);
                if (newRed > 255)
                    R = 255;
                else
                    R = newRed;
                if (newGreen > 255)
                    G = 255;
                else
                    G = newGreen;
                if (newBlue > 255)
                    B = 255;
                else
                    B = newBlue;
                p = (a<<24) | (R<<16) | (G<<8) | B;
                image.setRGB(x, y, p);
            }
        }
        return image;
    }
    public static BufferedImage oneColorChannel(BufferedImage image, Color rgb) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 16) & 0xff;
                int b = (p >> 16) & 0xff;

                image.setRGB(x, y, (a << 24) | (r & rgb.getRed()) << 16 | (g & rgb.getGreen()) << 8 | (b & rgb.getBlue()));
            }
        }

        return image;
    }

    public static BufferedImage swapColors( BufferedImage img, Color ... mapping ) {
        int[] pixels = img.getRGB( 0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth() );
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for( int i = 0; i < mapping.length/2; i++ ){
            map.put( mapping[2*i].getRGB(), mapping[2*i+1].getRGB() );
        }


        for( int i = 0; i < pixels.length; i++ ){
            if( map.containsKey( pixels[i] ) )
                pixels[i] = map.get( pixels[i] );
        }

        img.setRGB( 0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth() );
        return img;
    }

    public static void trigger() {
        if (number == 7)
            number = 0;
        else number++;
    }

    public static void trigger(int i) {
        number = i;
    }

    public static String[] shaders = {"0. RGBA", "1. GRAY", "2. BINARY", "3. NEGATIVE", "4. SEPIA",
            "5. ONE COLOR CHANNEL", "6. SWAP", "7. CUSTOM"};
    public static int number = 0;

    public static BufferedImage processHandlerInit(int WIDTH, int HEIGHT) {
        switch (number) {
            case 1:
                return new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
            case 2:
                return new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_BINARY);
            case 7:
                // custom palette
                IndexColorModel cm = new IndexColorModel(
                        3, // 3 bits can store up to 8 colors
                        6, // here I use only 6
                        //          RED  GREEN1 GREEN2  BLUE  WHITE BLACK
                        new byte[]{-100,     0,     0,    0,    -1,     0},
                        new byte[]{   0,  -100,    60,    0,    -1,     0},
                        new byte[]{   0,     0,     0, -100,    -1,     0});
                return new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_INDEXED, cm);
            default:
                return new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public static BufferedImage postProcessing(BufferedImage image) {
        switch (number){
            case 3:
                return negative(image);
            case 4:
                return sepia(image);
            case 5:
                return oneColorChannel(image, Color.WHITE);
            case 6:
                Color[] mapping = new Color[]{
                        // Color.black, Color.white, // replace black with white
                        // Color.white, Color.green // and white with green

                        Color.RED,      Color.MAGENTA,
                        Color.ORANGE,   Color.BLUE,
                        Color.YELLOW,   Color.CYAN,
                        Color.GREEN,    Color.GREEN,
                        Color.CYAN,     Color.YELLOW,
                        Color.BLUE,     Color.ORANGE,
                        Color.MAGENTA,  Color.RED,

                        Color.BLACK, Color.WHITE,
                        Color.WHITE, Color.BLACK,

                        Color.LIGHT_GRAY, Color.DARK_GRAY,
                        Color.DARK_GRAY, Color.LIGHT_GRAY

                        // unused, pink and gray
                };
                return swapColors(image, mapping);
            default:
                return image;
        }
    }
}