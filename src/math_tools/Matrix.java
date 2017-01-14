package math_tools;
public class Matrix {

    private int n_rows;
    private int n_cols;
    private double[][] data;
    
    
    public Matrix(double[][] data) {
        this.data = data;
        this.n_rows = data.length;
        this.n_cols = data[0].length;
    }
    
    
    public Matrix(int n_rows, int n_cols) {
        this.n_rows = n_rows;
        this.n_cols = n_cols;
        data = new double[n_rows][n_cols];
    }
    
    public int get_n_rows() {
    	return n_rows;
    }
    
    public int get_n_cols() {
    	return n_cols;
    }

    public double get_value_at(int i, int j) {
    	return data[i][j];
    }
    
    public void set_value_at(int i, int j, double value) {
    	data[i][j] = value;
    }
    
    // Return the transpose of the matrix.
    public static Matrix transpose(Matrix matrix) {
        Matrix transposedMatrix = new Matrix(matrix.get_n_cols(), matrix.get_n_rows());
        for (int i=0; i<matrix.get_n_rows(); i++) {
            for (int j=0; j<matrix.get_n_cols(); j++) {
                transposedMatrix.set_value_at(j, i, matrix.get_value_at(i, j));
            }
        }
        return transposedMatrix;
    } 
    
    public static int change_sign(int i) {
    	if (i % 2 == 0) {
    		return 1;    		
    	}
    	return -1;
    }
    
    // Compute the determinant of the matrix using the Laplace expansion.
    public static double determinant(Matrix matrix) {
        if (matrix.get_n_cols() == 1) {
        	return matrix.get_value_at(0, 0);
        }
        if (matrix.get_n_cols() == 2) {
            return (matrix.get_value_at(0, 0) * matrix.get_value_at(1, 1))
            		- (matrix.get_value_at(0, 1) * matrix.get_value_at(1, 0));
        }
        double sum = 0.0;
        for (int i=0; i<matrix.get_n_cols(); i++) {
            sum += change_sign(i) * matrix.get_value_at(0, i) 
            		* determinant(create_sub_matrix(matrix, 0, i));
        }
        return sum;
    } 
    
    public static Matrix create_sub_matrix(Matrix matrix, int row, int col) {
        Matrix mat = new Matrix(matrix.get_n_rows() - 1, matrix.get_n_cols() - 1);
        int r = -1;
        for (int i=0; i<matrix.get_n_rows(); i++) {
            if (i == row)
                continue;
                r++;
                int c = -1;
            for (int j=0; j<matrix.get_n_cols(); j++) {
                if (j == col)
                    continue;
                mat.set_value_at(r, ++c, matrix.get_value_at(i, j));
            }
        }
        return mat;
    }
    
    public static Matrix cofactor_matrix(Matrix matrix) {
        Matrix mat = new Matrix(matrix.get_n_rows(), matrix.get_n_cols());
        for (int i=0; i<matrix.get_n_rows(); i++) {
            for (int j=0; j<matrix.get_n_cols(); j++) {
                mat.set_value_at(i, j, change_sign(i) * change_sign(j)
                		* determinant(create_sub_matrix(matrix, i, j)));
            }
        }
        
        return mat;
    }
    
    public static Matrix multiply_by_constant(Matrix matrix, double r) {
    	Matrix multiplied_matrix = new Matrix(matrix.get_n_rows(), matrix.get_n_cols());
    	for (int i=0; i<matrix.get_n_rows(); i++) {
            for (int j=0; j<matrix.get_n_cols(); j++) {
            	multiplied_matrix.set_value_at(i, j, r * matrix.get_value_at(i, j));
            }
        }
    	return multiplied_matrix ;
    }
    
    // Return the inverse of the matrix.
	public static Matrix inverse(Matrix matrix) {
    	double d = determinant(matrix);
    	Matrix M = new Matrix(matrix.get_n_rows(), matrix.get_n_cols());
    	Matrix N = new Matrix(matrix.get_n_rows(), matrix.get_n_cols());
    	
        M = transpose(cofactor_matrix(matrix));
        N = Matrix.multiply_by_constant(M, 1.0/d);
        return N;
    }
	
	// Matrix multiplication.
	public static Matrix multiply(Matrix A, Matrix B) {
		
    	Matrix C = new Matrix(A.get_n_rows(), B.get_n_cols()) ;
    	for (int i=0; i<A.get_n_rows(); i++) {
            for (int j=0; j< B.get_n_cols(); j++) {
            	
	    		double s = 0;
	    		
	            for (int k=0; k<A.get_n_cols(); k++) {
	            	s = s + A.get_value_at(i, k) * B.get_value_at(k, j);
	            }
	            C.set_value_at(i, j, s);
            }
        } 
    	return C;
	}
    
}