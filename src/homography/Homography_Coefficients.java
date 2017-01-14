package homography;
import java.awt.Point;

import math_tools.Matrix;

public class Homography_Coefficients {
	
	// Returns the homography coefficients between the four corners of the sheet
	// and the four corners of the new image.
	public static Matrix homography_coefs(Point x0, Point x1, Point x2, Point x3, 
			Point y0, Point y1, Point y2, Point y3) throws ArithmeticException {
		
		// M is the homography matrix.
		Matrix M = new Matrix(8,8);

		for (int i=0; i<4; i++) {
			M.set_value_at(i, 2, 1);
			M.set_value_at(i+4, 5, 1);			
			for (int j=0; j<3; j++) {
				M.set_value_at(i+4, j, 0);
				M.set_value_at(i, j+3, 0);
			}
		}
		
		M.set_value_at(0, 6, -x0.getX() * y0.getX());
		M.set_value_at(0, 7, -x0.getY() * y0.getX());
		M.set_value_at(4, 6, -x0.getX() * y0.getY());
		M.set_value_at(4, 7, -x0.getY() * y0.getY());
		M.set_value_at(4, 3, x0.getX());
		M.set_value_at(4, 4, x0.getY());
		M.set_value_at(0, 0, x0.getX());
		M.set_value_at(0, 1, x0.getY());
		
		M.set_value_at(1, 6, -x1.getX() * y1.getX());
		M.set_value_at(1, 7, -x1.getY() * y1.getX());
		M.set_value_at(5, 6, -x1.getX() * y1.getY());
		M.set_value_at(5, 7, -x1.getY() * y1.getY());
		M.set_value_at(5, 3, x1.getX());
		M.set_value_at(5, 4, x1.getY());
		M.set_value_at(1, 0, x1.getX());
		M.set_value_at(1, 1, x1.getY());
		
		M.set_value_at(2, 6, -x2.getX() * y2.getX());
		M.set_value_at(2, 7, -x2.getY() * y2.getX());
		M.set_value_at(6, 6, -x2.getX() * y2.getY());
		M.set_value_at(6, 7, -x2.getY() * y2.getY());
		M.set_value_at(6, 3, x2.getX());
		M.set_value_at(6, 4, x2.getY());
		M.set_value_at(2, 0, x2.getX());
		M.set_value_at(2, 1, x2.getY());
		
		M.set_value_at(3, 6, -x3.getX() * y3.getX());
		M.set_value_at(3, 7, -x3.getY() * y3.getX());
		M.set_value_at(7, 6, -x3.getX() * y3.getY());
		M.set_value_at(7, 7, -x3.getY() * y3.getY());
		M.set_value_at(7, 3, x3.getX());
		M.set_value_at(7, 4, x3.getY());
		M.set_value_at(3, 0, x3.getX());
		M.set_value_at(3, 1, x3.getY());
		
		
		// X is the column vector containing the coordinates of the arrival corners.
		Matrix X = new Matrix(8,1);

		X.set_value_at(0, 0, y0.getX());
		X.set_value_at(4, 0, y0.getY());
		X.set_value_at(1, 0, y1.getX());
		X.set_value_at(5, 0, y1.getY());
		X.set_value_at(2, 0, y2.getX());
		X.set_value_at(6, 0, y2.getY());
		X.set_value_at(3, 0, y3.getX());
		X.set_value_at(7, 0, y3.getY());
		
		Matrix H = new Matrix(8,1);
		H = Matrix.multiply(Matrix.inverse(M), X);
			
	    return H;
	}
    
	// If p0 is a point in the initial sheet and p a point in the new sheet,
	// we are defining the function phi^(-1) such that p0 = phi^(-1)(p).
	public static double[] inverse_phi(Point p, Matrix H) {
		double c, x, y ;
		
		// c is a normalising factor
		c = ((H.get_value_at(0,0) * H.get_value_at(7,0) * p.getY()) 
				- (H.get_value_at(3,0) * H.get_value_at(7,0) * p.getX()) 
				+ (H.get_value_at(6,0) * H.get_value_at(4,0) * p.getX())
				- (H.get_value_at(1,0) * H.get_value_at(6,0) * p.getY()) 
				- (H.get_value_at(0,0) * H.get_value_at(4,0)) 
				+ (H.get_value_at(1,0) * H.get_value_at(3,0)));
		x = ((H.get_value_at(1,0) * p.getY()) 
				- (H.get_value_at(2,0) * H.get_value_at(7,0) * p.getY())
				+ (H.get_value_at(5,0) * H.get_value_at(7,0) * p.getX()) 
				- (H.get_value_at(4,0) * p.getX()) 
				+ (H.get_value_at(4,0) * H.get_value_at(2,0)) 
				- (H.get_value_at(5,0) * H.get_value_at(1,0))) 
				/ c;
		y = ((H.get_value_at(3,0) * p.getX()) 
				- (H.get_value_at(6,0) * H.get_value_at(5,0) * p.getX())
				+ (H.get_value_at(2,0) * H.get_value_at(6,0) * p.getY()) 
				- (H.get_value_at(0,0) * p.getY()) 
				+ (H.get_value_at(0,0) * H.get_value_at(5,0))
				- (H.get_value_at(3,0) * H.get_value_at(2,0))) 
				/ c;
		
		double[] L = new double[2];
		L[0] = x;
        L[1] = y;
        
		return L;
	}
	
}