package de.saxsys.pitfalls.concurrency;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by Andy Moncsek on 15.09.15.
 */
public class ServiceAndTask extends VBox {

    public
    @FXML
    ProgressIndicator progressTask;
    public
    @FXML
    ProgressIndicator progressService;
    public
    @FXML
    Button startTask;
    public
    @FXML
    Button startService;
    public @FXML
    Label labelTask;
    public @FXML
    Label labelService;



    public static void main(final String[] args) {
        Application.launch(args);
    }

    public ServiceAndTask() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/ServiceAndTaskView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }


}

