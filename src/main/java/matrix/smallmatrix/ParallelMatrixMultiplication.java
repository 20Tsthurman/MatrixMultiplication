package matrix.smallmatrix;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelMatrixMultiplication {

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

    public static int[][] parallelMatrixMultiply(int[][] a, int[][] b) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new MatrixMultiplyTask(a, b, 0, a.length - 1));
    }

    public static void main(String[] args) {
        // Define two matrices
        int[][] matrixA = {{1, 2}, {3, 4}};
        int[][] matrixB = {{5, 6}, {7, 8}};
    
        // Warm-up phase to allow JIT compilation optimization
        for (int i = 0; i < 100; i++) {
            parallelMatrixMultiply(matrixA, matrixB);
        }
    
        // Benchmarking phase
        int numberOfTrials = 10; // Number of times to run the test
        long totalTime = 0; // Store total time for all trials
    
        for (int i = 0; i < numberOfTrials; i++) {
            long startTime = System.nanoTime();
    
            // Perform parallel matrix multiplication
            int[][] resultMatrix = parallelMatrixMultiply(matrixA, matrixB);
    
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
            
            // Optional: Print out the resulting matrix from the last trial only
            if (i == numberOfTrials - 1) {
                for (int[] row : resultMatrix) {
                    for (int value : row) {
                        System.out.print(value + " ");
                    }
                    System.out.println();
                }
            }
        }
    
        // Calculate average time
        long averageTime = totalTime / numberOfTrials;
        System.out.println("Average time for parallel matrix multiplication over " +
                           numberOfTrials + " trials: " + averageTime + " nanoseconds.");
    }
    
}
