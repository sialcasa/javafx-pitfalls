package de.saxsys.pitfalls.memoryleaks.binding;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by Andy Moncsek on 14.10.15.
 * Based on article: http://tomasmikula.github.io/blog/2015/02/10/the-trouble-with-weak-listeners.html
 */
public class LeakExample {



    public static void main(String[] args) throws InterruptedException {
        SimpleDoubleProperty a = new SimpleDoubleProperty(0.0);
        for(int i = 0; i < 1000000; ++i) {
            a.add(5).multiply(10).dispose();
        }
        reportMemoryUsage();
       // a.add(0); // prevents a from being garbage collected until now

       // reportMemoryUsage();
    }

    private static void reportMemoryUsage() {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        double mb = 1024*1024;
        System.out.printf("Used Memory after GC: %.2f MB", used / mb);
    }
}
