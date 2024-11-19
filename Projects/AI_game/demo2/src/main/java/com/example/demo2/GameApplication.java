package com.example.demo2;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;


public class GameApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/com/example/demo2/hello-view.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        launch();
    }
}