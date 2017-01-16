package math_tools;
import java.awt.Point;

public class Line
{
  // The line is defined by the equation y = a*x + b.
  public double a, b;
  
  
  // Constructor using a and b.
  public Line(double a, double b) {
	  this.a = a;
	  this.b = b;
  }
  
  
  // Constructor using two points.
  public Line(Point p1, Point p2) {
	  define(p1, p2);
  }

  public void define(Point p1, Point p2) {
    // a can be infinite (vertical line).
    a = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());

    if (is_vertical()) {
      // Vertical line in b
      b = p1.getX();
    }
    else {
      // y = a*x + b  =>  b = y - a*x
      b = p1.getY() - a * p1.getX();
    }
  }

  public double get_X(double y) {
    if (is_vertical()) {
      return b;
    }
    return (y - b) / a;
  }
  public double get_Y(double x) {
    return a * x + b;
  }
  
  public boolean is_horizontal() {
    return a == 0;
  }
  
  // Test whether the line is vertical.
  public boolean is_vertical() {
    return Float.isInfinite((float) a);
  }
  
}