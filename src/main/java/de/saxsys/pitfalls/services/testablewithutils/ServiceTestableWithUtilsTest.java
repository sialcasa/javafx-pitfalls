package de.saxsys.pitfalls.services.testablewithutils;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.pitfalls.services.SomeBusinessLogic;
import de.saxsys.pitfalls.services.TestService;
import javafx.concurrent.WorkerStateEvent;
import org.jacpfx.concurrency.FXWorker;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * A test for {@link TestService} using the solution pattern given by
 * http://blog.buildpath.de/how-to-test-javafx-services/.
 */
@RunWith(JfxRunner.class)
public class ServiceTestableWithUtilsTest {


    @Test(timeout = 60000)
    public void testLongLastingOperationWithFXWorker() throws ExecutionException, InterruptedException {
        int counter = 10;
        final TestService service = new TestService();
        CountDownLatch waitTillFinished = new CountDownLatch(counter);
        IntStream.range(0, counter).forEach(v -> {
            FXWorker<?> handler = FXWorker.instance();
            CountDownLatch stop = new CountDownLatch(1);
            CountDownLatch waitForAsynyResult = new CountDownLatch(1);

            handler.supplyOnFXThread(() -> {
                restartService(service);
                registerResultListener(service, waitForAsynyResult);
                return service;
            }).functionOnExecutorThread((cut1) -> {
                // wait outside FX application thread !!!
                waitForService(waitForAsynyResult);
                return cut1;
            }).execute(val -> {
                System.out.println(v+": --> "+val.valueProperty().getValue());
                assertEquals("I'm an expensive result.", val.getValue());
                stop.countDown();
            });

            awaitServiceDone(stop);
            waitTillFinished.countDown();
        });

        //waitTillFinished.await();
        assertEquals(true, true);

    }

    private void awaitServiceDone(CountDownLatch stop) {
        try {
            stop.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForService(CountDownLatch waitForAsynyResult) {
        try {
            waitForAsynyResult.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void registerResultListener(final TestService service,CountDownLatch waitForAsynyResult) {
        service.valueProperty().addListener((b, o, n) -> {
            if (n != null) {
                assertEquals("I'm an expensive result.", n);
                waitForAsynyResult.countDown();
            }
        });
    }

    private void restartService(final TestService service) {
        if (!service.isRunning()) {
            service.reset();
            service.start();
        }
    }


    @Test(timeout = 60000)
    public void testLongLastingOperationWithFXWorker2() throws ExecutionException, InterruptedException {
        CountDownLatch stop = new CountDownLatch(1);
        final FXWorker<?> handler = FXWorker.instance();
        CountDownLatch waitForAsynyResult = new CountDownLatch(1);
        handler.supplyOnFXThread(() -> {
            TestService service = new TestService();
            service.start();
            service.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (val) -> {
                assertEquals("I'm an expensive result.", service.getValue());
                waitForAsynyResult.countDown();
            });
            return service;
        }).functionOnExecutorThread((cut) -> {
            // wait outside FX application thread !!!
            waitForService(waitForAsynyResult);

            return cut;
        }).functionOnFXThread(cut -> {
            System.out.println(cut.valueProperty().getValue());
            return cut.getValue();
        }).execute(val -> {
            assertEquals("I'm an expensive result.", val);
            stop.countDown();
        });
        assertEquals(true, true);
        stop.await();
    }

    @Test(timeout = 60000)
    public void testLongLastingOperationWithFXWorker3() throws ExecutionException, InterruptedException {
        CountDownLatch stop = new CountDownLatch(1);
        final FXWorker<?> handler = FXWorker.instance();

        handler.supplyOnExecutorThread(() -> {
            SomeBusinessLogic service = new SomeBusinessLogic();
            String result = service.longRunning();
            System.out.println("result step1: " + result);
            handler.updateMessage(result);
            return result;
        }).functionOnFXThread((cut) -> {
            System.out.println("result step2: " + cut);
            assertEquals("I'm an expensive result.", cut);
            assertEquals("I'm an expensive result.", handler.messageProperty().getValue());
            System.out.println("result step3: " + handler.messageProperty().getValue());
            return cut;
        }).execute(val -> {
            assertEquals("I'm an expensive result.", val);
            System.out.println("result step4: " + val);
            stop.countDown();
        });
        stop.await();
        assertEquals(true, true);
    }


}
