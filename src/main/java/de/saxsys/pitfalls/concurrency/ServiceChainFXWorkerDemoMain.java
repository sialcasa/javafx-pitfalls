package de.saxsys.pitfalls.concurrency;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jacpfx.concurrency.FXWorker;

/**
 * Created by Andy Moncsek on 15.09.15.
 */
public class ServiceChainFXWorkerDemoMain extends Application {

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



        demoControl.startService.setOnMouseClicked((val) -> {
            final FXWorker<?> handler = createExecutionChain(demoControl);

            demoControl.progressService.progressProperty().bind(handler.progressProperty());
            demoControl.stepTwoRectangle.setVisible(false);
            demoControl.stepOneRectangle.setVisible(false);

            handler.execute(value -> {
                demoControl.stepTwoRectangle.setStroke(Color.GRAY);
            });
        });

        stage.show();
    }

    private FXWorker<?> createExecutionChain(ServiceChain demoControl) {
        final FXWorker<?> handler = FXWorker.instance();

        handler
                .consumeOnFXThread((val) -> {
                    demoControl.stepOneRectangle.setStroke(Color.GREEN);
                    demoControl.stepOneRectangle.setVisible(true);
                })
                .supplyOnExecutorThread(() -> longRunningTask1(handler))
                .onError(throwable -> "")
                .consumeOnFXThread(stringVal -> {
                    demoControl.stepOneRectangle.setStroke(Color.GRAY);
                })
                .onError(throwable -> null)
                .consumeOnExecutorThread((v) -> longRunningTask2())
                .onError(throwable -> null)
                .supplyOnFXThread(() -> {
                    demoControl.stepTwoRectangle.setStroke(Color.GREEN);
                    demoControl.stepTwoRectangle.setVisible(true);
                    return "step2";
                })
                .functionOnExecutorThread((val) -> "step2".equals(val) ? longRunningTask3(handler) : "error");
        return handler;
    }

    private String longRunningTask3(FXWorker<?> handler) {
        handler.updateMessage("start");
        for (int i = 99; i < 200; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.updateMessage("progress: " + i);
        }
        return "finished - Step 3";
    }

    private void longRunningTask2() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String longRunningTask1(FXWorker<?> handler) {
        handler.updateMessage("start");
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.updateMessage("progress: " + i);
        }
        return "finished - Step 1";
    }


}
