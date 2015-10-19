package de.saxsys.pitfalls.services.nottestable;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import de.saxsys.pitfalls.services.TestService;

@RunWith(JfxRunner.class)
public class ServiceNotTestableTest {
	
	@Test
	public void testLongLastingOperation() throws ExecutionException, InterruptedException, TimeoutException {
		TestService service = new TestService();
		
		CompletableFuture<String> future = new CompletableFuture<>();
		
		service.valueProperty().addListener((b, o, n) -> {
			if (n != null) {
				future.complete(n);
				System.out.println("TEST");
			}
		});
		
		// STARTET ONCE....
		service.start();
		
		// assertEquals("I'm an expensive result", cut.getValue());
		assertEquals("I'm an expensive result.", future.get(5, TimeUnit.SECONDS));
	}
	
	/**
	 * This is the same test as the last, with an additional syso
	 */
	@Test
	public void testLongLastingOperationFails() throws ExecutionException, InterruptedException, TimeoutException {
		TestService service = new TestService();
		
		CompletableFuture<String> future = new CompletableFuture<>();
		
		service.valueProperty().addListener((b, o, n) -> {
			if (n != null) {
				future.complete(n);
			}
		});
		
		// STARTET ONCE....
		service.start();
		
		// EXC
		System.out.println(service.valueProperty());
		
		// assertEquals("I'm an expensive result", cut.getValue());
		assertEquals("I'm an expensive result.", future.get(5, TimeUnit.SECONDS));
	}
	
	
	/**
	 * This method fails, because the future#get call blocks the UI thread, so that the future.complete never get
	 * called.
	 */
	@Test
	@TestInJfxThread
	public void testLongLastingOperationInFXThread() throws ExecutionException, InterruptedException, TimeoutException {
		
		TestService service = new TestService();
		
		CompletableFuture<String> future = new CompletableFuture<>();
		
		service.valueProperty().addListener((b, o, n) -> {
			if (n != null) {
				future.complete(n);
			}
		});
		
		// STARTET ONCE....
		service.start();
		
		// EXC
		// cut.valueProperty();
		// assertEquals("I'm an expensive result", cut.getValue());
		assertEquals("I'm an expensive result.", future.get(5, TimeUnit.SECONDS));
		
	}
	
	/**
	 * raus?
	 */
	@TestInJfxThread
	@Test
	public void testLongLastingOperationInFXThreadDirty() throws ExecutionException, InterruptedException,
			TimeoutException {
		
		TestService service = new TestService();
		
		CompletableFuture<String> future = new CompletableFuture<>();
		
		service.valueProperty().addListener((b, o, n) -> {
			if (n != null) {
				future.complete(n);
			}
		});
		
		// STARTET ONCE....
		service.start();
		// bei mir kommt hier java.lang.IllegalStateException: Service must only be used from the FX Application Thread
		// das sollte doch aber mit deiner Annotation nicht sein?!?
		while (service.isRunning()) {
			System.out.println("wait");
		}
		
		// EXC
		// cut.valueProperty();
		// assertEquals("I'm an expensive result", cut.getValue());
		assertEquals("I'm an expensive result.", future.get(5, TimeUnit.SECONDS));
		
	}
}
