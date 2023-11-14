package com.myprojects325;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

// Utility class to handle matrix operations
public class MatrixUtils {

    // Inner class for ForkJoin multi-threaded multiplication
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

    // Generates a random matrix of given size
    public static int[][] generateRandomMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (int) (Math.random() * 10); // Random values between 0-9
            }
        }
        return matrix;
    }

    // Performs single-threaded matrix multiplication
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

    // Performs multi-threaded matrix multiplication using ForkJoinPool
    public static int[][] multiThreadedMatrixMultiply(int[][] a, int[][] b) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new MatrixMultiplyTask(a, b, 0, a.length - 1));
    }
}