package de.saxsys.pitfalls.memoryleaks.listener;

import java.lang.ref.WeakReference;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LeakExample extends Application {
	
	// Long living model element
	private final Car dataModel = new Car();
	
	// While the WeakReference contains the reference to the view, it's the proof that the view retains in memory
	private WeakReference<LeakingView> weakReference;
	
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Create the leaking view and the WeakReference
		LeakingView view = new LeakingView(dataModel);
		// While the WeakReference contains the reference to the view, it's the proof that the view retains in memory
		weakReference = new WeakReference<LeakingView>(view);
		
		// UI
		Button removeViewButton = new Button("Remove View");
		Button stopLeakingButton = new Button("Remove Leak");
		VBox root = new VBox(view, removeViewButton, stopLeakingButton);
		
		stopLeakingButton.setOnAction(event -> {
			view.removeListener();
			// If you don't remove the action handler, the line before will create a leak
				stopLeakingButton.setOnAction(null);
			});
		
		
		removeViewButton.setOnAction(event -> {
			root.getChildren().remove(view);
			// If you don't remove the action handler, the line before will create a leak
				removeViewButton.setOnAction(null);
			});
		
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		generateMemory();
	}
	
	private void generateMemory() {
		Thread thread = new Thread(() -> {
			// While the View retains in memory, we force the GarbageCollection to work
				while (isViewStillInMemory()) {
					System.out.println("View retains in memory");
					String[] generateOutOfMemoryStr = new String[999999];
					System.gc();
					sleep();
				}
				System.out.println("Released the memory");
			});
		thread.setDaemon(true);
		thread.start();
	}
	
	private boolean isViewStillInMemory() {
		return weakReference.get() != null;
	}
	
	private void sleep() {
		try {
			Thread.sleep(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
