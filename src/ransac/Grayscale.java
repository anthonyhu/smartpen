package ransac;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class Grayscale {
	
	// Convert an image to grayscale.
    public static BufferedImage to_grayscale(BufferedImage colored_image) {
 
        int red, green, blue, alpha;
        int new_pixel;
 
        BufferedImage grayscale_image = new BufferedImage(colored_image.getWidth(), 
        		colored_image.getHeight(), colored_image.getType());
 
        for(int i=0; i<colored_image.getWidth(); i++) {
            for(int j=0; j<colored_image.getHeight(); j++) {
            	
            	Color rgba_value = new Color(colored_image.getRGB(i, j));

                red = rgba_value.getRed();
                green = rgba_value.getGreen();
                blue = rgba_value.getBlue();
                alpha = rgba_value.getAlpha();
                
                // Use luminance to obtain the gray level.
                int gray = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                new_pixel = color_pixel(alpha, gray, gray, gray);
 
                // Create the new pixel.
                grayscale_image.setRGB(i, j, new_pixel);
 
            }
        }
 
        return grayscale_image;
 
    }
    
    // Color a pixel.
    private static int color_pixel(int alpha, int red, int green, int blue) {
 
        int new_pixel = 0;
        new_pixel += alpha;
        new_pixel = new_pixel << 8;
        new_pixel += red ;
        new_pixel = new_pixel << 8;
        new_pixel += green;
        new_pixel = new_pixel << 8;
        new_pixel += blue;
 
        return new_pixel;
 
    }

}