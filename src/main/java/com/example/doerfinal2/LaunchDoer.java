package com.example.doerfinal2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LaunchDoer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchDoer.class.getResource("log-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),600, 410);
        stage.setTitle("Doer");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        org.burningwave.core.classes.Modules.create().exportAllToAll();
    }

    public static void main(String[] args) {

        launch();
    }
}