package de.saxsys.pitfalls.applicationStart.service;

import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Andy Moncsek on 23.10.15.
 */
public class InitialUIService extends Service<List<Node>> {

    @Override
    protected Task<List<Node>> createTask() {
        return new Task<List<Node>>() {
            List<Node> labels = FXCollections.observableList(new ArrayList<Node>());
            @Override
            protected List<Node> call() throws Exception {
                IntStream.range(0, 1000).forEach(v -> {
                    labels.add(new Label("" + v));
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateProgress(v,1000);
                });
                return labels;
            }
        };
    }

}