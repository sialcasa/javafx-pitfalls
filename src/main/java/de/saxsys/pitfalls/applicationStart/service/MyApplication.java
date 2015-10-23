package de.saxsys.pitfalls.applicationStart.service;

/**
 * Created by Andy Moncsek on 23.10.15.
 */

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MyApplication extends Application {
    private InitialUIService service = new InitialUIService();
    BorderPane root = new BorderPane();

    @Override
    public void init() throws Exception {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Do some heavy lifting
        service.valueProperty().addListener((b, o, n) -> {
            if (n != null) {
                TilePane box = new TilePane();
                box.getChildren().addAll(n);
                root.setCenter(box);
            }
        });
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.progressProperty().bind(service.progressProperty());
        StackPane pane = new StackPane(indicator);
        root.setCenter(pane);
        service.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Label label = new Label("Loading complete!");
        label.setFont(new Font("Cambria", 32));
        label.setStyle("-fx-background-color: red");
        root.setTop(label);

        Scene scene = new Scene(root);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        LauncherImpl.launchApplication(MyApplication.class, MyPreloader.class, args);
    }

}