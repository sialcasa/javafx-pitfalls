package de.saxsys.pitfalls.services;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import org.jacpfx.concurrency.FXWorker;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

	@Test(timeout = 6000)
	public void testLongLastingOperationWithFXWorker() throws ExecutionException, InterruptedException {

		final FXWorker<?> handler = FXWorker.instance();

		handler.supplyOnFXThread(()-> {
			ClassToTest cut = new ClassToTest();
			cut.start();
			return cut;
		}).functionOnFXThread((cut)->{
			assertEquals("I'm an expensive result", cut.getValue());
			System.out.println(cut.valueProperty());
			return cut.getValue();
		}).execute(val->assertEquals("I'm an expensive result.", val));
		assertEquals(true,true);
	}

	@Test(timeout = 6000)
	public void testLongLastingOperationWithFXWorker2() throws ExecutionException, InterruptedException {

		final FXWorker<?> handler = FXWorker.instance();

		handler.supplyOnExecutorThread(() -> {
			ClassToTestService cut = new ClassToTestService();
			String result = cut.longRunning();
			handler.updateMessage(result);
			return result;
		}).functionOnFXThread((cut)->{
			assertEquals("I'm an expensive result", cut);
			assertEquals("I'm an expensive result", handler.messageProperty().getValue());
			return cut;
		}).execute(val->assertEquals("I'm an expensive result.", val));
		assertEquals(true,true);
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
