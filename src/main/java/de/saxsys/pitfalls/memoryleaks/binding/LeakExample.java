package de.saxsys.pitfalls.memoryleaks.binding;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Andy Moncsek on 14.10.15.
 */
public class LeakExample extends Application {
    DoubleBinding mult;
    SimpleDoubleProperty a = new SimpleDoubleProperty(0.0);
    @Override
    public void start(Stage primaryStage) throws Exception {
        FlatterFX.style();
        HBox container = new HBox();
        VBox vbox = new VBox();
        Scene scene = new Scene(vbox, 400, 150);


        Button createBindingsButton = new Button("create bindings");
        Button addValue = new Button("clear");
        Label label = new Label("");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Slider Sample");


        createBindingsButton.setOnAction((event) -> Platform.runLater(() -> {
            for (int i = 0; i < 1000000; ++i) {
                a.add(5).multiply(10).dispose();
            }
            label.setText(reportMemoryUsage());
        }));

        addValue.setOnAction((event)-> {
            a.add(10); // this should normally be enough;
            a = null;
            a = new SimpleDoubleProperty(0.0);
            label.setText(reportMemoryUsage());
        });

        container.getChildren().addAll(createBindingsButton,addValue);
        vbox.getChildren().addAll(container, label);
        primaryStage.show();
    }

    private String reportMemoryUsage() {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        double mb = 1024 * 1024;
        return String.format("after GC: %.2f MB Memory", used / mb);
    }
}
