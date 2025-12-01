package com.group.sortalgo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// TEMP test Main to verify JavaFX + project wiring
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Label label = new Label("JavaFX Works!");
        stage.setScene(new Scene(new StackPane(label), 300, 200));
        stage.setTitle("SortLab Test");
        stage.show();
    }
}
