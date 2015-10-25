package de.saxsys.pitfalls.concurrency;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

/**
 * Created by Andy Moncsek on 15.09.15. Simple view for Service-chain demo.
 */
public class ServiceChainView extends VBox {

    public
    @FXML
    ProgressIndicator progressService;
    public
    @FXML
    Button startService;
    public @FXML
    Rectangle stepOneRectangle;
    public @FXML
    Rectangle stepTwoRectangle;



    public static void main(final String[] args) {
        Application.launch(args);
    }

    public ServiceChainView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/ServiceChainDefaultView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        stepOneRectangle.setVisible(false);
        stepTwoRectangle.setVisible(false);
    }


}

