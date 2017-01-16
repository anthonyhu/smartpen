import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import homography.Homography_Application;
import ransac.ComputeCorners;

public class Main{
	
	public static void main(String[] args) throws Exception{
		
		// Read the original image.
        BufferedImage original_image = ImageIO.read(new File("./Results/without_hand.jpg"));
        
        ComputeCorners compute_corners = new ComputeCorners(original_image);
        
        // Compute the corners of the sheet in the original image.
        ArrayList<Point> sheet_corners = compute_corners.compute_corners();
        
        // Coordinates of the new corners: 1654x2339, A4 in 200dpi.
        Point p5 = new Point(0, 0);
        Point p6 = new Point(0, 2339);
        Point p7 = new Point(1654, 0);
        Point p8 = new Point(1654, 2339);
        ArrayList<Point> new_corners = new ArrayList<Point>(4);
        
        new_corners.add(p5);
        new_corners.add(p6);
        new_corners.add(p7);
        new_corners.add(p8);
        
        // Create the new image.
        BufferedImage transformed_image = Homography_Application.apply_homography(original_image,
        		sheet_corners, new_corners);
        
        ImageIO.write(transformed_image, "jpg", new File("./Results/transformed_image.jpg"));

	}
	
}