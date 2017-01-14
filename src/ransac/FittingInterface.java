package ransac;
import java.awt.Point;
import java.util.ArrayList;

import math_tools.Line;

// Interface to fit Ransac's line.
public interface FittingInterface
{ 
  // Return the number of points necessary to compute the line.
  int get_number_of_points();
  
  // Return the minimum number of inliers to validate the line.
  int get_minimum_inliers();
  
  // Return a line that corresponds to the current model.
  Line estimate_model(ArrayList<Point> points);
  
  // Compute the error between a point and the current line.
  double estimate_error(Point point, Line model);
}