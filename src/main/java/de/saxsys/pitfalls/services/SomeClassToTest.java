package de.saxsys.pitfalls.services;

/**
 * A business class to be tested.
 */
public class SomeClassToTest {

    public String longRunning() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "I'm an expensive result.";
    }



}
