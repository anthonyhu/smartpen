package ransac;
import java.awt.Point;
import java.util.ArrayList;

import math_tools.Line;

public class Ransac {
	
	  // Array of points to construct the model.
	  private ArrayList<Point> data;
	  // Current line of the model.
	  private Line current_line = new Line(0,1);
	  // Maximum number of iterations
	  private int max_nb_iterations;
	  // Threshold to accept a point in the model (inlier).
	  private double threshold;
	  // Minimum number of inliers to consider a line as a model.
	  private int min_nb_inliers;
	  
	  // Array of the current set of points.
	  private ArrayList<Point> current_points_sample;
	  // Array of current inliers.
	  private ArrayList<Point> current_inliers = new ArrayList<Point>();
	  // Current number of iterations.
	  private int current_nb_iterations;
	  // Current score.
	  private double current_score;

	  // Line representing the best model.
	  private Line best_line = new Line(0,0);
	  // Array of inliers of the best model.
	  private ArrayList<Point> inliers_of_best_model = new ArrayList<Point>();
	  // Score of the best model.
	  private double best_score = Integer.MAX_VALUE;
	  
	  // Ransac's parameters.
	  private FittingInterface fitting;
	  

    public Ransac(FittingInterface fitting, ArrayList<Point> points, double seuil) {
      this.fitting = fitting;
      this.data = points;
      this.threshold = seuil;
      min_nb_inliers = this.fitting.get_minimum_inliers();
      max_nb_iterations =  compute_nb_iterations(0.99, 0.5, this.fitting.get_number_of_points());
    }
    
    // Implementation of the getters.
    public Line get_current_line() { return current_line; }
    public ArrayList<Point> get_current_points_sample() { return current_points_sample; }
    public double get_current_score() { return current_score; }
    public int get_current_nb_iterations() { return current_nb_iterations; }
    public int get_max_nb_iterations() { return max_nb_iterations; }
    public ArrayList<Point> get_inliers_of_best_model() { return inliers_of_best_model; }
    public Line get_best_line() { return best_line; }
    public double get_best_score() { return best_score; }

    private int compute_nb_iterations(double ransac_probability, double outliers_ratio, 
    		int nb_points_to_construct_line) {
    	
      return (int) Math.ceil(Math.log(1 - ransac_probability) /
          Math.log(1 - Math.pow(1 - outliers_ratio, nb_points_to_construct_line)));
    }
    
    // Select nb_points randomly in the data.
    private ArrayList<Point> random_sample(int nb_points) {
      ArrayList<Point> points = new ArrayList<Point>();
      
      for (int i=0; i<nb_points; ) { 
    	// Random number between 0 and data size - 1.
        int t = (int) (Math.random()*(data.size()-1));
        Point pt = data.get(t);
        
        if (!points.contains(pt)) { // If the point has not been chosen yet.
          points.add(pt);
          i++;
        }
      }
      return points;
    }
    
    public boolean is_over() {
        return current_nb_iterations > max_nb_iterations;
      }
    
    // Next iteration of the Ransac algorithm.
    public double next_iteration() {
      if (is_over())
      {
        current_points_sample.clear();
        current_inliers.clear();
        current_line = null;
        return 0;
      }
      else {
	      check_sample(random_sample(fitting.get_number_of_points()));
	      current_nb_iterations++;
      }

      return current_score;
    }

    public void check_sample(ArrayList<Point> points) {
      current_points_sample = points;
      current_line = fitting.estimate_model(current_points_sample);
      current_inliers.clear();
      current_score = 0;
      
      // Computation of the distance of the line to each point of the data.
      for (Point point : data) {
        // Ignore the points already in the sample.
        if (current_points_sample.contains(point)) {
          continue;
        }

        double error = fitting.estimate_error(point, current_line);
        if (error > threshold) {
          current_score += threshold; // Add the value of the threshold to the current score.
        }
        else {
          current_score += error; 
          // This point is an inlier.
          current_inliers.add(point);
        }
      }
      
      // Check whether the current model is better than the one stored.
      if (current_inliers.size() > min_nb_inliers && current_score < best_score) {
        best_line = current_line;
        inliers_of_best_model = current_inliers;
        best_score = current_score;
      }
    }

}