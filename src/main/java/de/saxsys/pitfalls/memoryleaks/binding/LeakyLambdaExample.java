package de.saxsys.pitfalls.memoryleaks.binding;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by Andy Moncsek on 14.10.15.
 * This Example demonstrates how WeakListeners are not being garbage collected when used with lambdas or call by reference.
 */
public class LeakyLambdaExample {
    /**
     * please uncomment method 1,2 or 3 (only simultaneously)
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        SimpleDoubleProperty a = new SimpleDoubleProperty(0.0);

        /**  IntStream.range(0, 1000000).forEach(n -> {
         a.add(5).multiply(10).dispose();
         });  **/

        //
        // Method 1: runnable(()-> runInForLoop(a)).run();

        runnable(()-> runInForLoop(a)).run();

        // Method 2: runInForLoop(a);
        /**
         * Method 3:
         * for (int i = 0; i < 1000000; ++i) {
            a.add(5).multiply(10).dispose();
         }**/


        reportMemoryUsage();

        a.add(0); // prevents a from being garbage collected until now

        reportMemoryUsage();
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void runInForLoop(SimpleDoubleProperty a) {
        for (int i = 0; i < 1000000; ++i) {
            a.add(5).multiply(10).dispose();
        }
    }

    private static Runnable runnable(Runnable r) {
        return r;
    }

    private static void reportMemoryUsage() {
        System.gc();  // try perform gc
        System.runFinalization();
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        double mb = 1024 * 1024;
        System.out.printf("Used Memory after GC: %.2f MB", used / mb);
    }
}
