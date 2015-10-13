package de.saxsys.pitfalls.concurrency;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by Andy Moncsek on 15.09.15.
 */
public class ServiceChainDemoMain extends Application {

    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        FlatterFX.style();
        ServiceChain demoControl = new ServiceChain();
        Scene scene = new Scene(demoControl, 800, 400);
        stage.setScene(scene);
        stage.setTitle("ServiceChain Demo");

        final Service<String> service = createServiceOne();
        createServiceChain(service,demoControl);
        demoControl.startService.setOnMouseClicked((val) -> {
            if (!service.isRunning()) {
                service.reset();
                demoControl.stepOneRectangle.setStroke(Color.GREEN);
                demoControl.stepOneRectangle.setVisible(true);
                service.start();
                demoControl.progressService.progressProperty().bind(service.progressProperty());
            }
        });

        demoControl.progressService.progressProperty().bind(service.progressProperty());

        stage.show();
    }

    private void createServiceChain(final Service<String> service, final ServiceChain demoControl) {
        service.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (val) -> {

            final Service<String> service2 = createServiceTwo();

            service2.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (val2) -> {
                demoControl.stepTwoRectangle.setStroke(Color.GRAY);
                service.getWorkDone();
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            demoControl.progressService.progressProperty().unbind();
            demoControl.progressService.progressProperty().bind(service2.progressProperty());

            demoControl.stepOneRectangle.setStroke(Color.GRAY);
            demoControl.stepTwoRectangle.setStroke(Color.GREEN);
            demoControl.stepTwoRectangle.setVisible(true);
            service2.start();
        });
    }

    private Service<String> createServiceOne() {
        return new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return createTaskInstance();
            }
        };
    }

    private Service<String> createServiceTwo() {
        return new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return createTaskInstance2();
            }
        };
    }

    private Task<String> createTaskInstance() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateMessage("start");
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(20);
                    updateMessage("progress: " + i);
                    updateProgress(i, 200);
                }
                return "finished - Step 1";
            }
        };
    }

    private Task<String> createTaskInstance2() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateMessage("start");
                for (int i = 101; i <= 200; i++) {
                    Thread.sleep(20);
                    updateMessage("progress: " + i);
                    updateProgress(i, 200);
                }
                return "finished - Step 2";
            }
        };
    }

}
