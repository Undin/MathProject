package com.ifmo.mathproject.ui;

import com.ifmo.mathproject.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Whiplash on 10.12.2014.
 */
public class Controller implements Initializable {

    private static final String PREC = "%.8f";

    @FXML
    private LineChart<Double, Double> tempPlot;
    @FXML
    private LineChart<Double, Double> concPlot;
    @FXML
    private LineChart<Double, Double> velPlot;

    @FXML
    private Label betta;
    @FXML
    private Label gamma;
    @FXML
    private Label u;
    @FXML
    private Label deltaH;
    @FXML
    private Label deltaR;
    @FXML
    private Label tm;

    @FXML
    private TextField deltaZ;
    @FXML
    private TextField deltaT;
    @FXML
    private TextField kValue;
    @FXML
    private TextField eValue;
    @FXML
    private TextField alphaValue;
    @FXML
    private TextField qValue;
    @FXML
    private TextField pValue;
    @FXML
    private TextField tValue;
    @FXML
    private TextField lambdaValue;
    @FXML
    private TextField cValue;
    @FXML
    private TextField dValue;

    private Model model = new Model();

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

        deltaZ.setText(String.valueOf(model.getDx()));
        deltaT.setText(String.valueOf(model.getDt()));
        kValue.setText(String.valueOf(model.getK()));
        eValue.setText(String.valueOf(model.getE()));
        alphaValue.setText(String.valueOf(model.getAlpha()));
        qValue.setText(String.valueOf(model.getQ()));
        pValue.setText(String.valueOf(model.getP()));
        tValue.setText(String.valueOf(model.getT0()));
        lambdaValue.setText(String.valueOf(model.getLambda()));
        cValue.setText(String.valueOf(model.getC()));
        dValue.setText(String.valueOf(model.getD()));

        deltaZ.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        deltaT.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        kValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        eValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        alphaValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        qValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        pValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        tValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        lambdaValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        cValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        dValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        updateLabels();
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

    private void changeValues() {
        try {
            model.setDx(Double.parseDouble(deltaZ.getText()))
                    .setDt(Double.parseDouble(deltaT.getText()))
                    .setK(Double.parseDouble(kValue.getText()))
                    .setE(Double.parseDouble(eValue.getText()))
                    .setAlpha(Double.parseDouble(alphaValue.getText()))
                    .setQ(Double.parseDouble(qValue.getText()))
                    .setP(Double.parseDouble(pValue.getText()))
                    .setT0(Double.parseDouble(tValue.getText()))
                    .setLambda(Double.parseDouble(lambdaValue.getText()))
                    .setC(Double.parseDouble(cValue.getText()))
                    .setD(Double.parseDouble(dValue.getText()));
            updateLabels();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void updateLabels() {
        betta.setText(String.format(PREC, model.getBetta()));
        gamma.setText(String.format(PREC, model.getGamma()));
        u.setText(String.format(PREC, model.getU()));
        deltaH.setText(String.format(PREC, model.getDeltaH()));
        deltaR.setText(String.format(PREC, model.getDeltaR()));
        tm.setText(String.format(PREC, model.getTm()));
    }

}
