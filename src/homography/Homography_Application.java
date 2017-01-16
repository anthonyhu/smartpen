package homography;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import math_tools.Matrix;

public class Homography_Application{
	
	// Apply the transformation to the original sheet.
	public static BufferedImage apply_homography(BufferedImage original_image,
			ArrayList<Point> original_corners, ArrayList<Point> new_corners){
	
	    Point p1 = original_corners.get(0);
	    Point p2 = original_corners.get(1);
	    Point p3 = original_corners.get(2);
	    Point p4 = original_corners.get(3);
	
	    Point p5 = new_corners.get(0);
	    Point p6 = new_corners.get(1);
	    Point p7 = new_corners.get(2);
	    Point p8 = new_corners.get(3);
		
	    // Homography matrix.
		Matrix H = Homography_Coefficients.homography_coefs(p1, p2, p3, p4, p5, p6, p7, p8);
		
		/* The new image is blank at first, we then loop through each pixel and apply
		 * the function phi^(-1) and copy the corresponding pixel.
		 */
	    BufferedImage new_image = new BufferedImage(p8.x, p8.y, original_image.getType());
	    
	    // u and v correspond to the coordinates in the original image.
	    int u, v;
	    int red, green, blue, alpha, new_pixel;
	    
		for (int i=0; i<new_image.getWidth(); i++) {
	            for (int j=0; j<new_image.getHeight(); j++) {
	            	double[] p = Homography_Coefficients.inverse_phi(new Point(i, j), H);
	            	 
	            	// Retrieving each pixel with the floor method.
	            	u = (int)(p[0] + 0.5);
	            	v = (int)(p[1] + 0.5);
	            	
	            	// Check is the point is on the original image.
	            	if ((u > 0) && (u < original_image.getWidth()) && 
	            			(v > 0) && (v < original_image.getHeight())) {

	            		red = new Color(original_image.getRGB(u, v)).getRed();
	            		green = new Color(original_image.getRGB(u, v)).getGreen();
	            		blue = new Color(original_image.getRGB(u, v)).getBlue();
	            		alpha = new Color(original_image.getRGB(u, v)).getAlpha();

	            		new_pixel = color_pixel(alpha, red, green, blue);
	            		new_image.setRGB(i, j, new_pixel);
	            	}
	            }
		 }       
	     
		return new_image;
		
	}
	
	// Color a pixel.
    public static int color_pixel(int alpha, int red, int green, int blue) {
 
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