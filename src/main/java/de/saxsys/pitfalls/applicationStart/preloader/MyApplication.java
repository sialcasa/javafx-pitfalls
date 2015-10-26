package de.saxsys.pitfalls.applicationStart.preloader;

/**
 * Created by Andy Moncsek on 23.10.15.
 */

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MyApplication extends Application {
    List<Node> labels = FXCollections.observableList(new ArrayList<Node>());

    @Override
    public void init() throws Exception {
        // Do some heavy lifting
        IntStream.range(0, 1000).forEach(v -> {
            labels.add(new Label("" + v));
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        Label label = new Label("Loading complete!");
        label.setFont(new Font("Cambria", 32));
        label.setStyle("-fx-background-color: red");
        root.setTop(label);
        TilePane box = new TilePane();
        box.getChildren().addAll(labels);
        root.setCenter(box);
        Scene scene = new Scene(root);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // do not use internal API! This is only for Demo: https://docs.oracle.com/javafx/2/deployment/preloaders.html
        LauncherImpl.launchApplication(MyApplication.class, MyPreloader.class, args);
    }

}