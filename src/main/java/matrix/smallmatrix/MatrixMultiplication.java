package matrix.smallmatrix;
public class MatrixMultiplication {

    // Function to multiply two matrices in a single-threaded fashion
    public static int[][] singleThreadedMatrixMultiply(int[][] a, int[][] b) {
        int rowsA = a.length;
        int columnsA = a[0].length; // same as rowsB in matrix multiplication
        int columnsB = b[0].length;
        
        int[][] result = new int[rowsA][columnsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < columnsB; j++) {
                for (int k = 0; k < columnsA; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return result;
    }

    // Main method to run the matrix multiplication
    public static void main(String[] args) {
        // Define two matrices
        int[][] matrixA = {{1, 2}, {3, 4}};
        int[][] matrixB = {{5, 6}, {7, 8}};
    
        // Start timer for single-threaded multiplication
        long startTime = System.nanoTime();
    
        // Perform matrix multiplication
        int[][] resultMatrix = singleThreadedMatrixMultiply(matrixA, matrixB);
    
        // Stop timer and calculate elapsed time
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
    
        // Print out the resulting matrix
        for (int[] row : resultMatrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    
        // Print out the time taken
        System.out.println("Single-threaded multiplication took: " + duration + " nanoseconds.");
    }
    
}
