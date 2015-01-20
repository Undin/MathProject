package com.ifmo.mathproject.d2ui;

import com.ifmo.mathproject.d2.ExplicitMethod;
import com.ifmo.mathproject.d2.Layer2D;
import com.ifmo.mathproject.d2.Method2D;
import com.ifmo.mathproject.d2.Model2D;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Whiplash on 10.12.2014.
 */
public class Controller2D implements Initializable {

    private static final String PREC = "%.8f";

    private static final String[] METHODS_NAME = {"Explicit"};

    @FXML
    private Pane tempPlot;
    @FXML
    private Pane concPlot;
    @FXML
    private Pane velPlot;

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
    private TextField deltaT;
    @FXML
    private TextField deltaZ;
    @FXML
    private TextField stepNumberZ;
    @FXML
    private TextField deltaY;
    @FXML
    private TextField stepNumberY;
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

    @FXML
    private ChoiceBox<String> methodsSelector;
    @FXML
    private TextField iterValue;

    private Model2D model = new Model2D();
    private double[][] steps;
    private Layer2D prevLayer;
    private Layer2D curLayer;
    private Method2D method;
    private int speed = 100;

    private Timer timer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deltaZ.setText(String.valueOf(model.getDx()));
        deltaY.setText(String.valueOf(model.getDy()));
        deltaT.setText(String.valueOf(model.getDt()));
        stepNumberZ.setText(String.valueOf(model.getXN()));
        stepNumberY.setText(String.valueOf(model.getYN()));
        twValue.setText(String.valueOf(model.getTw()));
        kValue.setText(String.valueOf(model.getK()));
        eValue.setText(String.valueOf(model.getE()));
        alphaValue.setText(String.valueOf(model.getAlpha()));
        qValue.setText(String.valueOf(model.getQ()));
        pValue.setText(String.valueOf(model.getP()));
        tValue.setText(String.valueOf(model.getInitT()));
        lambdaValue.setText(String.valueOf(model.getLambda()));
        cValue.setText(String.valueOf(model.getC()));
        dValue.setText(String.format(Locale.US, "%.9f", model.getD()));
        speedValue.setText(String.valueOf(speed));

        methodsSelector.setItems(FXCollections.observableArrayList(METHODS_NAME));
        methodsSelector.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            switch ((int) newValue) {
                default:
                    expMethod();
                    break;
            }
        });

        deltaZ.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        deltaT.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
        stepNumberZ.textProperty().addListener((observable, oldValue, newValue) -> changeValues());
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

    private void expMethod() {
        iterValue.setText("");
        iterValue.setDisable(true);
    }

    private Canvas tempCanvas;
    private Canvas concCanvas;
    private Canvas velCanvas;

    private void setPlot(Pane pane, double[][] values, double maxValue) {
        double width = pane.getWidth() - 10;
        double height = pane.getHeight() - 10;
        Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
        if (pane.equals(tempPlot)) {
            if (tempCanvas != null) {
                pane.getChildren().remove(tempCanvas);
            }
            tempCanvas = canvas;
        } else if (pane.equals(concPlot)) {
            if (concCanvas != null) {
                pane.getChildren().remove(concCanvas);
            }
            concCanvas = canvas;
        } else if (pane.equals(velPlot)) {
            if (velCanvas != null) {
                pane.getChildren().remove(velCanvas);
            }
            velCanvas = canvas;
        }
        pane.getChildren().add(canvas);

        int lineWidth = (int) (width / values[0].length) + 1;
        int lineHeight = (int) (height / values.length) + 1;

        GraphicsContext context = canvas.getGraphicsContext2D();
        for (int y = 0; y < values.length; y++) {
            for (int x = 0; x < values[0].length; x++) {
                double val = Math.max(0, values[y][x] / maxValue);
                val = Math.min(val, 1);
                context.setFill(Color.rgb((int) (val * 255), 0, (int) (255 * (1 - val))));
                context.fillRect(5 + x * lineWidth, 5 + y * lineHeight, lineWidth, lineHeight);
            }
        }
    }

    @FXML
    private void startClick(ActionEvent event) {
        pauseClick(event);
        resume.setDisable(true);

        double[][] temperature = new double[model.getYN()][model.getXN()];
        for (int i = 0; i < temperature.length; i++) {
            temperature[i][0] = model.getTw();
            for (int j = 1; j < temperature[0].length; j++) {
                temperature[i][j] = model.getInitT();
            }
        }

        double[][] concentration = new double[model.getYN()][model.getXN()];
        for (int i = 0; i < temperature.length; i++) {
            concentration[i][0] = 0;
            for (int j = 1; j < temperature[0].length; j++) {
                concentration[i][j] = 1;
            }
        }

        method = getMethod();
        prevLayer = new Layer2D(concentration, temperature);
        curLayer = method.nextLayer(prevLayer);
        drawLayer();
        resumeClick(event);
    }

    private Method2D getMethod() {
        switch (methodsSelector.getValue()) {
            case "Explicit":
                return new ExplicitMethod(model);
            default:
                return null;
        }
    }

    private void drawLayer() {
        setPlot(tempPlot, curLayer.getTemperature(), model.getTw());
        setPlot(concPlot, curLayer.getConcentration(), 1);
        double[][] w = new double[model.getYN()][model.getXN()];
        double maxW = -Double.MAX_VALUE;
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < w[0].length; j++) {
                w[i][j] = -(curLayer.getConcentration()[i][j] - prevLayer.getConcentration()[i][j]) / model.getDt();
                maxW = Math.max(maxW, w[i][j]);
            }
        }
        setPlot(velPlot, w, maxW);
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
            Platform.runLater(Controller2D.this::drawLayer);
        }
    }


    private void changeValues() {
        try {
            model.setDt(Double.parseDouble(deltaT.getText()));
            model.setDx(Double.parseDouble(deltaZ.getText()));
            model.setXN(Integer.parseInt(stepNumberZ.getText()));
            model.setDy(Double.parseDouble(deltaY.getText()));
            model.setYN(Integer.parseInt(stepNumberY.getText()));
            model.setInitT(Double.parseDouble(tValue.getText()));
            model.setK(Double.parseDouble(kValue.getText()));
            model.setE(Double.parseDouble(eValue.getText()));
            model.setAlpha(Double.parseDouble(alphaValue.getText()));
            model.setQ(Double.parseDouble(qValue.getText()));
            model.setP(Double.parseDouble(pValue.getText()));
            model.setTw(Double.parseDouble(twValue.getText()));
            model.setLambda(Double.parseDouble(lambdaValue.getText()));
            model.setC(Double.parseDouble(cValue.getText()));
            model.setD(Double.parseDouble(dValue.getText()));
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
