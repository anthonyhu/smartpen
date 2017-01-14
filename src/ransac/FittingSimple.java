package ransac;
import java.awt.Point;
import java.util.ArrayList;

import math_tools.Line;

// A simple implementation using only two points to estimate the line.
public class FittingSimple implements FittingInterface
{
  public int get_number_of_points() { return 2; }
  public int get_minimum_inliers() { return 50; }

  public Line estimate_model(ArrayList<Point> points)
  {
    assert points.size() == get_number_of_points() : "Please precise the number of points"
    		+ " to estimate the line.";
    Point p1 = points.get(0);
    Point p2 = points.get(1);
    return new Line(p1, p2);
  }

  public double estimate_error(Point point, Line model)
  {  
    // Distance of a point from a line.
    double y = model.a * point.getX() + model.b;
    return (Math.abs(point.getY() - y) / Math.sqrt(1 + model.a * model.a));
  }
}