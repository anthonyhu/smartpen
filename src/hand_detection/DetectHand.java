package hand_detection;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class DetectHand {
	
	// Compare two images to assess whether the hand has disappeared or not.
	public static void main(String[] args) throws IOException{
		File file_1 = new File("with_hand.jpg");
		File file_2 = new File("without_hand.jpg");
        BufferedImage image_1 = ImageIO.read(file_1);
        BufferedImage image_2 = ImageIO.read(file_2);
        
        System.out.println(histogram_difference(image_1, image_2));

	}
	
	
	// Compute the color histograms' difference between two images.
	public static boolean histogram_difference(BufferedImage image_1, BufferedImage image_2) {
		// Experimental threshold that worked the best with the pictures taken.
		double threshold = 0.3;
		
		ArrayList<int[]> histograms_1 = compute_histograms(image_1);
		ArrayList<int[]> histograms_2 = compute_histograms(image_2);
		
		double total_difference = 0;
		
		for (int k=0; k<histograms_1.size(); k++) {
			
			int[] current_histogram_1 = histograms_1.get(k);
			int[] current_histogram_2 = histograms_2.get(k);
			
			for (int i=0; i<256; i++) {
				total_difference += Math.abs(current_histogram_1[i] - current_histogram_2[i]);
			}
		}
		
		// Normalise the difference
		total_difference /= (3 * Math.pow(256, 3));

		if (total_difference>threshold)
			return true;    // The hand is no longer here.
		else 
			return false;   // The hand is still here.
	}
	
	
	// Return the color histograms of an image.
    public static ArrayList<int[]> compute_histograms(BufferedImage image) {
 
        int[] red_histogram = new int[256];
        int[] green_histogram = new int[256];
        int[] blue_histogram = new int[256];
 
        for(int i=0; i<image.getWidth(); i++) {
            for(int j=0; j<image.getHeight(); j++) {
            	
            	// Add one to the histogram for each RGB value of the image.
                red_histogram[new Color(image.getRGB(i, j)).getRed()]++;
                green_histogram[new Color(image.getRGB(i, j)).getGreen()]++;
                blue_histogram[new Color(image.getRGB(i, j)).getBlue()]++;
            }
        }
        
        ArrayList<int[]> histograms = new ArrayList<int[]>();
        histograms.add(red_histogram);
        histograms.add(green_histogram);
        histograms.add(blue_histogram);
        
        return histograms;
 
    }
    
}