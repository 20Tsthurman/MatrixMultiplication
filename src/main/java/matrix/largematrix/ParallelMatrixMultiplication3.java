package matrix.largematrix;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelMatrixMultiplication3 {

    private static class MatrixMultiplyTask extends RecursiveTask<int[][]> {
        private int[][] a;
        private int[][] b;
        private int startRow;
        private int endRow;

        MatrixMultiplyTask(int[][] a, int[][] b, int startRow, int endRow) {
            this.a = a;
            this.b = b;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        protected int[][] compute() {
            if (startRow == endRow) {
                int[][] result = new int[1][b[0].length];
                for (int j = 0; j < b[0].length; j++) {
                    for (int k = 0; k < b.length; k++) {
                        result[0][j] += a[startRow][k] * b[k][j];
                    }
                }
                return result;
            } else {
                int midRow = (startRow + endRow) / 2;
                MatrixMultiplyTask task1 = new MatrixMultiplyTask(a, b, startRow, midRow);
                MatrixMultiplyTask task2 = new MatrixMultiplyTask(a, b, midRow + 1, endRow);
                task1.fork(); // This line queues task1 to be run in parallel.
                int[][] result2 = task2.compute(); // This line runs task2 in the current thread.
                int[][] result1 = task1.join(); // This line waits for task1 to finish if it hasn't already.

                // Combine results
                int[][] result = new int[endRow - startRow + 1][result1[0].length];
                System.arraycopy(result1, 0, result, 0, result1.length);
                System.arraycopy(result2, 0, result, result1.length, result2.length);

                return result;
            }
        }
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

    public static int[][] parallelMatrixMultiply(int[][] a, int[][] b) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new MatrixMultiplyTask(a, b, 0, a.length - 1));
    }

    public static void main(String[] args) {
        // Define the size of the matrices
        int size = 250;

        // Initialize two matrices with random values
        int[][] matrixA = generateMatrix(size, size);
        int[][] matrixB = generateMatrix(size, size);

        // Start timer for parallel matrix multiplication
        long startTime = System.nanoTime();

        // Perform parallel matrix multiplication
        int[][] resultMatrix = parallelMatrixMultiply(matrixA, matrixB);

        // Stop timer and calculate elapsed time
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        // Print out the time taken
        System.out.println("Parallel matrix multiplication of " + size + "x" + size + 
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
