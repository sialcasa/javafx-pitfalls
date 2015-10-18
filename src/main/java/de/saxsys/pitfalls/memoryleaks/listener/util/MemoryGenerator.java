package de.saxsys.pitfalls.memoryleaks.listener.util;

import java.util.function.Supplier;

public class MemoryGenerator {
	public static void generateMemoryWhileTrue(Supplier<Boolean> shouldRun) {
		Thread thread = new Thread(() -> {
			// While the View retains in memory, we force the GarbageCollection to work
				while (shouldRun.get()) {
					System.out.println("View retains in memory");
					String[] generateOutOfMemoryStr = new String[999999];
					System.gc();
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("Released the memory");
			});
		thread.setDaemon(true);
		thread.start();
	}
}
