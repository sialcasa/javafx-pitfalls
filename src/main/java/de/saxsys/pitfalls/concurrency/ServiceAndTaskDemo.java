package de.saxsys.pitfalls.concurrency;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.ExecutionException;

/**
 * Created by Andy Moncsek on 15.09.15.
 */
public class ServiceAndTaskDemo extends Application {

    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        FlatterFX.style();
        ServiceAndTask demoControl = new ServiceAndTask();
        Scene scene = new Scene(demoControl, 800, 400);
        stage.setScene(scene);
        stage.setTitle("Service and Task Demo");

        final Task<String> task = createTaskInstance();
        bindTaskToControl(demoControl,task);
        final Service<String> service = createService();
        bindServiceToControl(demoControl,service);
        stage.show();
    }


    private void bindTaskToControl(ServiceAndTask demoControl,Task<String> task) {


        demoControl.startTask.setOnMouseClicked((val) -> new Thread(task).start());

        demoControl.labelTask.textProperty().bind(task.messageProperty());


        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (val) -> {
            try {
                demoControl.labelTask.textProperty().unbind();
                demoControl.labelTask.setText(task.get());
                demoControl.startTask.setDisable(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        demoControl.progressTask.progressProperty().bind(task.progressProperty());
    }

    private void bindServiceToControl(ServiceAndTask demoControl,Service<String> service) {


        demoControl.startService.setOnMouseClicked((val) -> {
            if (!service.isRunning()) {
                service.reset();
                service.start();
                demoControl.labelService.textProperty().bind(service.messageProperty());
            }
        });

        demoControl.labelService.textProperty().bind(service.messageProperty());


        service.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (val) -> {
            demoControl.labelService.textProperty().unbind();
            demoControl.labelService.setText(service.getValue());
        });

        demoControl.progressService.progressProperty().bind(service.progressProperty());
    }




    private Service<String> createService() {
        return new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return createTaskInstance();
            }
        };
    }

    private Task<String> createTaskInstance() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateMessage("start");
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(10);
                    updateMessage("progress: " + i);
                    updateProgress(i, 100);
                }
                return "finished";
            }
        };
    }
}
