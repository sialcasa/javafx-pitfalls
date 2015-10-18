package de.saxsys.pitfalls.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * A service to be tested.
 */
public class TestService extends Service<String> {
	
	@Override
	protected Task<String> createTask() {
		return new Task<String>() {
			@Override
			protected String call() throws Exception {
				Thread.sleep(5000);
				return "I'm an expensive result.";
			}
		};
	}
	
}
