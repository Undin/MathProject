package com.ifmo.mathproject.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Whiplash on 10.12.2014.
 */
public class Window extends Application {

    FXMLLoader loader;

    @Override
    public void start(Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("main_window.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Math project");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        ((Controller) loader.getController()).stopTimer();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
