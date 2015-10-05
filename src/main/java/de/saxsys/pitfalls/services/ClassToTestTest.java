package de.saxsys.pitfalls.services;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;

/**
 * A test for {@link ClassToTest} using the solution pattern given by
 * http://blog.buildpath.de/how-to-test-javafx-services/.
 */
@RunWith(JfxRunner.class)
public class ClassToTestTest {

    private final ClassToTest cut = new ClassToTest();

    @Test(timeout = 6000)
    public void testLongLastingOperationCallingTwice() throws ExecutionException, InterruptedException {

        // first call succeeds
        myTest();

    }

    private void myTest() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = new CompletableFuture<>();

        cut.valueProperty().addListener((b, o, n) -> {
            System.out.println(n);
            if (n != null) {
                future.complete(n);
            }
        });

        // STARTET ONCE....
        cut.start();

        // EXC
        cut.valueProperty();
        // assertEquals("I'm an expensive result", cut.getValue());
        assertEquals("I'm an expensive result.", future.get());
    }
}
