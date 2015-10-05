package de.saxsys.pitfalls.services;

/**
 * A Service that provides an operation which can last some time for calculating.
 */
public class SomeService {

    /**
     * A very long lasting operation blocking the current thread. So this operation should be executed asynchronously.
     *
     * @return A String indicating that the operation has finished.
     * @throws InterruptedException Is thrown when an error occurs during execution.
     */
    public String longLastingOperation() throws InterruptedException {
        Thread.sleep(5000);
        return "An expensive result";
    }
}
