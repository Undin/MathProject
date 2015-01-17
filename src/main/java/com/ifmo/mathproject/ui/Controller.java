package com.ifmo.mathproject.ui;

import com.ifmo.mathproject.Model;
import com.ifmo.mathproject.d1.Layer1D;
import com.ifmo.mathproject.d1.Method1D;
import com.ifmo.mathproject.d1.PredictorCorrectorMethod;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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
    private Button start;
    @FXML
    private Button pause;
    @FXML
    private Button resume;

    @FXML
    private TextField deltaZ;
    @FXML
    private TextField deltaT;
    @FXML
    private TextField stepNumber;
    @FXML
    private TextField twValue;
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
    @FXML
    private TextField speedValue;

    private Model model = new Model();
    private double[] steps;
    private Layer1D prevLayer;
    private Layer1D curLayer;
    private Method1D method;
    private int speed = 500;

    private Timer timer;

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
        stepNumber.setText(String.valueOf(model.getN()));
        twValue.setText(String.valueOf(model.getTw()));
        kValue.setText(String.valueOf(model.getK()));
        eValue.setText(String.valueOf(model.getE()));
        alphaValue.setText(String.valueOf(model.getAlpha()));
        qValue.setText(String.valueOf(model.getQ()));
        pValue.setText(String.valueOf(model.getP()));
        tValue.setText(String.valueOf(model.getInitT()));
        lambdaValue.setText(String.valueOf(model.getLambda()));
        cValue.setText(String.valueOf(model.getC()));
        dValue.setText(String.valueOf(model.getD()));
        speedValue.setText("500");

        deltaZ.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        deltaT.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        stepNumber.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        twValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        kValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        eValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        alphaValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        qValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        pValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        tValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        lambdaValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        cValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        dValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        speedValue.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
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


    @FXML
    private void startClick(ActionEvent event) {
        pauseClick(event);
        resume.setDisable(true);

        double[] temperature = new double[model.getN()];
        temperature[0] = model.getTw();
        for (int i = 1; i < temperature.length; i++) {
            temperature[i] = model.getInitT();
        }

        double[] concentration = new double[model.getN()];
        concentration[0] = 0;
        for (int i = 1; i < concentration.length; i++) {
            concentration[i] = 1;
        }

        steps = new double[model.getN()];
        for (int i = 0; i < concentration.length; i++) {
            steps[i] = i * model.getDx();
        }

        method = new PredictorCorrectorMethod(model);
        prevLayer = new Layer1D(concentration, temperature);
        curLayer = method.nextLayer(prevLayer);
        drawLayer();
        resumeClick(event);
    }

    private void drawLayer() {
        setPlot(tempPlot, steps, curLayer.getTemperature());
        setPlot(concPlot, steps, curLayer.getConcentration());
        double[] w = new double[model.getN()];
        for (int i = 0; i < w.length; i++) {
            w[i] = -(curLayer.getConcentration()[i] - prevLayer.getConcentration()[i]) / model.getDt();
        }
        setPlot(velPlot, steps, w);
    }

    @FXML
    private void pauseClick(ActionEvent event) {
        if (timer != null) {
            timer.cancel();
            resume.setDisable(false);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @FXML
    private void resumeClick(ActionEvent event) {
        timer = new Timer();
        if (model != null) {
            timer.schedule(new Drawer(), 0, speed);
        }
        resume.setDisable(true);
    }

    private class Drawer extends TimerTask {
        @Override
        public void run() {
            prevLayer = curLayer;
            curLayer = method.nextLayer(prevLayer);
            Platform.runLater(Controller.this::drawLayer);
        }
    }


    private void changeValues() {
        try {
            model.setDx(Double.parseDouble(deltaZ.getText()))
                    .setDt(Double.parseDouble(deltaT.getText()))
                    .setN(Integer.parseInt(stepNumber.getText()))
                    .setInitT(Double.parseDouble(tValue.getText()))
                    .setK(Double.parseDouble(kValue.getText()))
                    .setE(Double.parseDouble(eValue.getText()))
                    .setAlpha(Double.parseDouble(alphaValue.getText()))
                    .setQ(Double.parseDouble(qValue.getText()))
                    .setP(Double.parseDouble(pValue.getText()))
                    .setTw(Double.parseDouble(twValue.getText()))
                    .setLambda(Double.parseDouble(lambdaValue.getText()))
                    .setC(Double.parseDouble(cValue.getText()))
                    .setD(Double.parseDouble(dValue.getText()));
            speed = (int) Double.parseDouble(speedValue.getText());
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
