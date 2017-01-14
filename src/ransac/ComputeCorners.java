package ransac;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import math_tools.Line;

public class ComputeCorners {
  private BufferedImage original_image;
  
  public ComputeCorners(BufferedImage original_image) {
	  this.original_image = original_image;
  }
  
	// Compute the corners of the sheet.
	public ArrayList<Point> compute_corners() {
		
		BufferedImage grayscale_image = Grayscale.to_grayscale(original_image);
		
        // Initialisation of the four sides of the sheet.
        Line vertical_1, vertical_2, horizontal_1, horizontal_2;
	 
        FittingInterface fitting = new FittingSimple();
        // Threshold calculated experimentally.
        double threshold = 5.0;
        
        /* We use the Ransac algorithm four times to compute the four sides of the sheet
         * by dividing the latter in four zones.
         */
        
        // Ransac's algorithm to compute upper horizontal.
        ArrayList<Point> data_horizontal_1 = horizontal_thresholding(grayscale_image, 
        		0, grayscale_image.getWidth(), 0, grayscale_image.getHeight()/8); //500
        Ransac ransac_horizontal_1 = new Ransac(fitting, data_horizontal_1, threshold);
        horizontal_1 = ransac_line(ransac_horizontal_1); 
        
        // Ransac's algorithm to compute lower horizontal.
        ArrayList<Point> data_horizontal_2 = horizontal_thresholding(grayscale_image, 
        		0, grayscale_image.getWidth(), 
        		grayscale_image.getHeight()*7/8, grayscale_image.getHeight()); //1200 1600
        Ransac ransac_horizontal_2 = new Ransac(fitting, data_horizontal_2, threshold);
        horizontal_2 = ransac_line(ransac_horizontal_2); 
        
        // Ransac's algorithm to compute left vertical.
        ArrayList<Point> data_vertical_1 = vertical_thresholding(grayscale_image, 
        		0, grayscale_image.getWidth()/5, 0, grayscale_image.getHeight()); //600
        Ransac ransac_vertical_1 = new Ransac(fitting, data_vertical_1, threshold);
        vertical_1 = ransac_line(ransac_vertical_1); 
        
        // Ransac's algorithm to compute right vertical.
        ArrayList<Point> data_vertical_2 = vertical_thresholding(grayscale_image, 
        		grayscale_image.getWidth()*4/5, grayscale_image.getWidth(), 
        		0, grayscale_image.getHeight()); //2000 2500
        Ransac ransac_vertical_2 = new Ransac(fitting, data_vertical_2, threshold);
        vertical_2 = ransac_line(ransac_vertical_2); 

        
        ArrayList<Point> corners = new ArrayList<Point>(4);
        
        corners.add(intersection(vertical_1,horizontal_1));
        corners.add(intersection(vertical_1,horizontal_2));
        corners.add(intersection(vertical_2,horizontal_1));
        corners.add(intersection(vertical_2,horizontal_2));
 
        return corners;
 
    }

	// Compute the horizontal array of points used in Ransac's algorithm thanks to 
	// gradient thresholding.
    private ArrayList<Point> horizontal_thresholding(BufferedImage grayscale_image, 
    		int x_min, int x_max, int y_min, int y_max) {
		
    	// Empirical threshold
		int threshold = 25;
		
		int upper_pixel, lower_pixel;
 
        ArrayList<Point> data = new ArrayList<Point>();
 
        for(int i=x_min; i<x_max; i++) {
            for(int j=y_min; j<y_max-1; j++) {
 
            	upper_pixel = new Color(grayscale_image.getRGB(i,j)).getRed();
            	lower_pixel = new Color(grayscale_image.getRGB(i,j+1)).getRed();
                
                if (Math.abs(upper_pixel - lower_pixel) > threshold) {
                	data.add(new Point(i,j));
                }
            }
        }
 
        return data;
		
	}
    
    // Compute the vertical array of points used in Ransac's algorithm thanks to
    // gradient thresholding.
    private static ArrayList<Point> vertical_thresholding(BufferedImage grayscale_image, 
    		int x_min, int x_max, int y_min, int y_max) {
		
    	// Empirical threshold
		int threshold = 25;
		
		int left_pixel, right_pixel;
 
        ArrayList<Point> data = new ArrayList<Point>();
 
        for(int i=x_min; i<x_max-1; i++) {
            for(int j=y_min; j<y_max; j++) {                    
            	left_pixel = new Color(grayscale_image.getRGB(i+1,j)).getRed();
            	right_pixel = new Color(grayscale_image.getRGB(i,j)).getRed();
            	
            	if (Math.abs(left_pixel - right_pixel) > threshold) {
                	data.add(new Point(i,j));
                }
            }
        }
        return data;
	}
   
    public Line ransac_line(Ransac ransac) {
    	
    	// Starting Ransac's algorithm.
    	while (!ransac.is_over()) {
        	ransac.next_iteration();
        }

        Line line = ransac.get_best_line();
       
        Line result;
        
        // Check special cases of horizontal and vertical lines.
        if (line.is_horizontal()) {
          double y = line.get_Y(0);
          Point p1 = new Point(0, (int)(y));
          Point p2 = new Point(original_image.getWidth(), (int)(y));
          result = new Line(p1,p2);
        }
        else if (line.is_vertical()) {
          double x = line.get_X(0);
          Point p1 = new Point((int)(x), 0);
          Point p2 = new Point((int)(x), original_image.getHeight());
          result = new Line(p1,p2);
        }
        else {
        	int x1 = (int)(line.get_X(0));
            int y1 = (int)(line.get_Y(x1));
            Point p1 = new Point(x1,y1);
            
            int x2 = (int)(line.get_X(original_image.getHeight()));
            int y2 = (int)(line.get_Y(x2));
            Point p2 = new Point(x2,y2);
            
            result = new Line(p1,p2);
        }
        
        return result;
    }
    
    // Return the intersection point between two lines.
    public Point intersection(Line line1, Line line2) {
    	double x1 = line1.get_X(0);
        double y1 = line1.get_Y(x1);
        double x2 = line1.get_X(original_image.getHeight());
        double y2 = line1.get_Y(x2);
        double x3 = line2.get_X(0);
        double y3 = line2.get_Y(x1);
        double x4 = line2.get_X(original_image.getHeight());
        double y4 = line2.get_Y(x2);
        
        if (!lines_intersect(x1,y1,x2,y2,x3,y3,x4,y4)) return null;
          double px = x1,
                 py = y1,
                 rx = x2-px,
                 ry = y2-py;
          double qx = x3,
                 qy = y3,
                 sx = x4-qx,
                 sy = y4-qy;

          double det = sx*ry - sy*rx;
          if (det == 0) {
            return null;
          }
          else {
            double z = (sx * (qy-py) + sy * (px-qx)) / det;
            if (z==0 ||  z==1) return null;  // Intersection at the end of the two lines.
            return new Point((int)(px + z*rx), (int)(py + z*ry));
          }
     }
    
    // Returns true if the line (x1,y1) and (x2,y2) intersects the line (x3,y3) and (x4,y4)
    public static boolean lines_intersect(double x1, double y1, double x2, double y2,
    		                               double x3, double y3, double x4, double y4) {
    	return ((relativeCCW(x1, y1, x2, y2, x3, y3) * relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
    		     && (relativeCCW(x3, y3, x4, y4, x1, y1) 
    		    		 * relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
    }
    
    public static int relativeCCW(double x1, double y1, double x2, double y2, 
    		double px, double py) {
    	
    	x2 -= x1;
    	y2 -= y1;
    	px -= x1;
    	py -= y1;
    	double ccw = px * y2 - py * x2;
    	
    	if (ccw == 0.0) {
    		ccw = px * x2 + py * y2;
    		if (ccw > 0.0) {
    			px -= x2;
    		    py -= y2;
    	        ccw = px * x2 + py * y2;
    		    if (ccw < 0.0) {
    		    	ccw = 0.0;
    		    }
    		 }
    	}
    	
    	
    	return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
    }
  
}