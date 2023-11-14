package com.myprojects325;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    private TextField matrixSizeField;
    
    @FXML
    private Label singleCoreTimeLabel;
    
    @FXML
    private Label multiCoreTimeLabel;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private LineChart<Number, Number> performanceChart;
    
    private XYChart.Series<Number, Number> singleCoreSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> multiCoreSeries = new XYChart.Series<>();
    
    // Call this method in an @FXML initialize() method or constructor
    public void setupChart() {
        singleCoreSeries.setName("Single-Core");
        multiCoreSeries.setName("Multi-Core");
    
        performanceChart.getData().addAll(singleCoreSeries, multiCoreSeries);
    }
    
    @FXML
    private void runMultiplication() {
        try {
            int matrixSize = Integer.parseInt(matrixSizeField.getText());
            int[][] matrixA = MatrixUtils.generateRandomMatrix(matrixSize, matrixSize);
            int[][] matrixB = MatrixUtils.generateRandomMatrix(matrixSize, matrixSize);
    
            // Measure and run single-threaded multiplication
            long startTime = System.nanoTime();
            MatrixUtils.singleThreadedMatrixMultiply(matrixA, matrixB);
            long singleCoreTime = System.nanoTime() - startTime;
            singleCoreTime /= 1_000_000; // Convert to milliseconds
            singleCoreTimeLabel.setText("Single-Core Time: " + singleCoreTime + " ms");
            singleCoreSeries.getData().add(new XYChart.Data<>(matrixSize, singleCoreTime));
    
            // Measure and run multi-threaded multiplication
            startTime = System.nanoTime();
            MatrixUtils.multiThreadedMatrixMultiply(matrixA, matrixB);
            long multiCoreTime = System.nanoTime() - startTime;
            multiCoreTime /= 1_000_000; // Convert to milliseconds
            multiCoreTimeLabel.setText("Multi-Core Time: " + multiCoreTime + " ms");
            multiCoreSeries.getData().add(new XYChart.Data<>(matrixSize, multiCoreTime));
    
        } catch (NumberFormatException e) {
            singleCoreTimeLabel.setText("Invalid matrix size. Please enter an integer.");
            multiCoreTimeLabel.setText("");
        }
    }
    
    @FXML
    public void initialize() {
        setupChart();
    }
}

