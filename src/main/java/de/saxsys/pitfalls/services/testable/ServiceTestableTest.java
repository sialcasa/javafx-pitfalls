package de.saxsys.pitfalls.services.testable;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.pitfalls.services.TestService;
import javafx.application.Platform;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@RunWith(JfxRunner.class)
public class ServiceTestableTest {
    @Test
    public void testLongLastingOperationSimple() throws ExecutionException, InterruptedException,
            TimeoutException {

        TestService service = new TestService();
        CompletableFuture<String> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            service.valueProperty().addListener((b, o, n) -> {
                if (n != null) {
                    System.out.println("compleate:");
                    future.complete(n);
                }

            });

            service.restart();
        });
        assertEquals("I'm an expensive result.", future.get(5, TimeUnit.SECONDS));
        System.out.println("result: " + future.get());


    }


    @Test
    public void testLongLastingOperation() throws ExecutionException, InterruptedException,
            TimeoutException {

        TestService service = new TestService();
        AtomicReference<String> ref = new AtomicReference<>();
        int counter = 20;

        CountDownLatch awaitAllServiceFinish = new CountDownLatch(counter);
        IntStream.range(0, counter).forEach(v -> {
            CountDownLatch awaitServiceFinish = new CountDownLatch(1);
            AtomicReference<String> localRef = new AtomicReference<>();
            Platform.runLater(() -> {
                service.valueProperty().addListener((b, o, n) -> {
                    if (n != null) {
                        ref.set(n);
                        localRef.set(n);
                        awaitServiceFinish.countDown();
                        System.out.println(v + ": " + ref.get());
                    }

                });

                service.restart();
            });
            try {
                awaitServiceFinish.await();
                awaitAllServiceFinish.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals("I'm an expensive result.", localRef.get());

        });

        awaitAllServiceFinish.await();
        assertEquals("I'm an expensive result.", ref.get());
        System.out.println(ref.get());

    }

    @Test
    public void testLongLastingOperation2() throws ExecutionException, InterruptedException,
            TimeoutException {

        TestService service = new TestService();

        int counter = 20;

        CountDownLatch awaitAllServiceFinish = new CountDownLatch(counter);
        IntStream.range(0, counter).forEach(v -> {
            CompletableFuture<String> future = new CompletableFuture<>();
            Platform.runLater(() -> {
                service.valueProperty().addListener((b, o, n) -> {
                    if (n != null) {
                        future.complete(n);
                        System.out.println(v + ": " + n);
                    }

                });

                service.restart();
            });
            try {
                assertEquals("I'm an expensive result.", future.get(5, TimeUnit.SECONDS));
                awaitAllServiceFinish.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        });

        awaitAllServiceFinish.await();

    }
}