package matrix.xlmatrix;

public class MatrixMultiplication4 {

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

    // Method to generate a matrix with random values
    private static int[][] generateMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (int) (Math.random() * 10); // Random values between 0-9
            }
        }
        return matrix;
    }

    // Main method to run the matrix multiplication
    public static void main(String[] args) {
        // Define the size of the matrices
        int size = 1500;

        // Initialize two matrices with random values
        int[][] matrixA = generateMatrix(size, size);
        int[][] matrixB = generateMatrix(size, size);
    
        // Start timer for single-threaded multiplication
        long startTime = System.nanoTime();
    
        // Perform matrix multiplication
        int[][] resultMatrix = singleThreadedMatrixMultiply(matrixA, matrixB);
    
        // Stop timer and calculate elapsed time
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
    
        // Print out the time taken
        System.out.println("Single-threaded multiplication of " + size + "x" + size + 
                           " matrices took: " + duration + " nanoseconds.");

        // Print a small part of the resulting matrix to validate correctness
        System.out.println("Part of the resulting matrix:");
            for (int i = 0; i < Math.min(resultMatrix.length, 3); i++) {
                for (int j = 0; j < Math.min(resultMatrix[i].length, 3); j++) {
                    System.out.print(resultMatrix[i][j] + " ");
    }
                    System.out.println();
}

    }    
}
