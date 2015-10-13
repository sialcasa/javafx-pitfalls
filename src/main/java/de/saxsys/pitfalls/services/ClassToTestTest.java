package de.saxsys.pitfalls.services;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import javafx.concurrent.WorkerStateEvent;
import org.jacpfx.concurrency.FXWorker;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * A test for {@link ClassToTest} using the solution pattern given by
 * http://blog.buildpath.de/how-to-test-javafx-services/.
 */
@RunWith(JfxRunner.class)
public class ClassToTestTest {


    @Test(timeout = 6000)
    public void testLongLastingOperation() throws ExecutionException, InterruptedException {
        ClassToTest cut = new ClassToTest();

        CompletableFuture<String> future = new CompletableFuture<>();

        cut.valueProperty().addListener((b, o, n) -> {
            if (n != null) {
                future.complete(n);
            }
        });

        // STARTET ONCE....
        cut.start();

        // EXC
        System.out.println(cut.valueProperty());
        // assertEquals("I'm an expensive result", cut.getValue());
        assertEquals("I'm an expensive result.", future.get());
    }

    @Test(timeout = 60000)
    public void testLongLastingOperationWithFXWorker() throws ExecutionException, InterruptedException {
        final ClassToTest cut = new ClassToTest();

        IntStream.range(0,5).forEach(v-> {
            CountDownLatch stop = new CountDownLatch(1);
            FXWorker<?> handler = FXWorker.instance();
            CountDownLatch waitForAsynyResult = new CountDownLatch(1);
            handler.supplyOnFXThread(() -> {

                if (!cut.isRunning()) {
                    cut.reset();
                    cut.start();
                }

                cut.valueProperty().addListener((b, o, n) -> {
                    if (n != null) {
                        assertEquals("I'm an expensive result.", n);
                        waitForAsynyResult.countDown();
                    }
                });
                return cut;
            }).functionOnExecutorThread((cut1) -> {
                // wait outside FX application thread !!!
                try {
                    waitForAsynyResult.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return cut1;
            }).functionOnFXThread(cut2 -> {
                System.out.println(cut2.valueProperty().getValue());
                return cut2.getValue();
            }).execute(val -> {
                assertEquals("I'm an expensive result.", val);
                stop.countDown();
            });

            try {
                stop.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });



        assertEquals(true, true);

    }


    @Test(timeout = 60000)
    public void testLongLastingOperationWithFXWorker2() throws ExecutionException, InterruptedException {
        CountDownLatch stop = new CountDownLatch(1);
        final FXWorker<?> handler = FXWorker.instance();
        CountDownLatch waitForAsynyResult = new CountDownLatch(1);
        handler.supplyOnFXThread(() -> {
            ClassToTest cut = new ClassToTest();
            cut.start();
            cut.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (val) -> {
                assertEquals("I'm an expensive result.", cut.getValue());
                waitForAsynyResult.countDown();
            });
            return cut;
        }).functionOnExecutorThread((cut) -> {
            // wait outside FX application thread !!!
            try {
                waitForAsynyResult.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
            ClassToTestService cut = new ClassToTestService();
            String result = cut.longRunning();
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

    @Test(timeout = 6000)
    @TestInJfxThread
    /**
     * Das kann nicht funktionieren aus dem selben Grund warum CountDown latch nicht funktioniert.
     * Du blockierst den FX application Thread (bzw. per Zufall kann es klappen (wenn der Service kürzer Arbeitet als die Excecution der Testmethode dauert))
     */
    public void testLongLastingOperationInFXThread() throws ExecutionException, InterruptedException {

        ClassToTest cut = new ClassToTest();

        CompletableFuture<String> future = new CompletableFuture<>();

        cut.valueProperty().addListener((b, o, n) -> {
            if (n != null) {
                future.complete(n);
            }
        });

        // STARTET ONCE....
        cut.start();

        // EXC
        // cut.valueProperty();
        // assertEquals("I'm an expensive result", cut.getValue());
        assertEquals("I'm an expensive result.", future.get());

    }



    /**
     * Das kann nicht funktionieren aus dem selben Grund warum CountDown latch nicht funktioniert.
     * Du blockierst den FX application Thread (bzw. per Zufall kann es klappen (wenn der Service kürzer Arbeitet als die Excecution der Testmethode dauert))
     */
    @Test(timeout = 6000)
    @TestInJfxThread
    public void testLongLastingOperationInFXThreadDirty() throws ExecutionException, InterruptedException {

        ClassToTest cut = new ClassToTest();

        CompletableFuture<String> future = new CompletableFuture<>();

        cut.valueProperty().addListener((b, o, n) -> {
            if (n != null) {
                future.complete(n);
            }
        });

        // STARTET ONCE....
        cut.start();
         // bei mir kommt hier java.lang.IllegalStateException: Service must only be used from the FX Application Thread
        // das sollte doch aber mit deiner Annotation nicht sein?!?
        while (cut.isRunning()) {
            System.out.println("wait");
        }

        // EXC
        // cut.valueProperty();
        // assertEquals("I'm an expensive result", cut.getValue());
        assertEquals("I'm an expensive result.", future.get());

    }
}
