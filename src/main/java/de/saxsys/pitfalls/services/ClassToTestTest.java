package de.saxsys.pitfalls.services;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;

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
	
	@Test(timeout = 6000)
	@TestInJfxThread
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
}
