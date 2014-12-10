package com.ifmo.mathproject.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Whiplash on 10.12.2014.
 */
public class Controller implements Initializable {

    @FXML
    LineChart<Double, Double> tempPlot;
    @FXML
    LineChart<Double, Double> concPlot;
    @FXML
    LineChart<Double, Double> velPlot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tempPlot.setCreateSymbols(false);
        tempPlot.setLegendVisible(false);
        tempPlot.getStyleClass().add("thick-chart");

        concPlot.setCreateSymbols(false);
        concPlot.setLegendVisible(false);
        concPlot.getStyleClass().add("thick-chart");

        velPlot.setCreateSymbols(false);
        velPlot.setLegendVisible(false);
        velPlot.getStyleClass().add("thick-chart");

        double[] x = {0, 1, 2, 3, 4, 5, 6, 7};
        double[] y = {0, 1, 2, 3, 4, 5, 6, 7};
        setPlot(tempPlot, x, y);
    }

    private void setPlot(LineChart<Double, Double> plot, double[] xAxis, double[] yAxis) {
        if (xAxis.length != yAxis.length) {
            throw new IllegalArgumentException("xAxis and yAxis length is different");
        }

        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
        LineChart.Series<Double, Double> series = new LineChart.Series<>();
        for (int i = 0; i < xAxis.length; i++) {
            series.getData().add(new XYChart.Data<>(xAxis[i], yAxis[i]));
        }
        lineChartData.add(series);
        plot.setData(lineChartData);
    }

}
