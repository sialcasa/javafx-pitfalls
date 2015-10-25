package de.saxsys.pitfalls.memoryleaks.binding;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * Created by Andy Moncsek on 14.10.15.
 */
public class AccidentalGCQuiz extends Application {
    DoubleBinding add;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FlatterFX.style();
        HBox container = new HBox();
        Scene scene = new Scene(container, 400, 150);
        VBox main = new VBox();
        HBox input = new HBox();

        TextField numberOne = new TextField();
        TextField numberTwo = new TextField();

        Button forceGC = new Button("(try) force gc");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Slider Sample");

        forceGC.setOnAction(event -> System.gc());

        StringConverter<Number> converter = new NumberStringConverter();
        DoubleProperty one = new SimpleDoubleProperty();
        DoubleProperty two = new SimpleDoubleProperty();

        Bindings.bindBidirectional(numberOne.textProperty(), one, converter);
        Bindings.bindBidirectional(numberTwo.textProperty(), two, converter);


        Bindings.
                add(one, two).
                addListener((x, y, z) -> {
                    if (z != null && z.intValue() == 10) {
                        javafx.scene.control.Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Login Dialog");
                        alert.setHeaderText("Look, an Information Dialog");
                        alert.setContentText("Correct");

                        alert.showAndWait();
                    }

                });


        /**
         * Solution !!
         add = add(one, two);
         add.addListener((a, b, c) -> {
          ....
         });
         **/
        input.getChildren().addAll(numberOne,numberTwo);
        main.getChildren().addAll(input, forceGC);

        container.getChildren().addAll(main);
        primaryStage.show();
    }
}
