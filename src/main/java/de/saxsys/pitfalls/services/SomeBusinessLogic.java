package de.saxsys.pitfalls.services;

/**
 * A business class to be tested.
 */
public class SomeBusinessLogic {
	
	public String longRunning() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "I'm an expensive result.";
	}
	
	
	
}
