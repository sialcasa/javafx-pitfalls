package de.saxsys.pitfalls.caching;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import de.saxsys.pitfalls.caching.util.CircleFactory;
import de.saxsys.pitfalls.caching.view.CircleArea;

public class RightUsageOfCaching extends Application {
	
	private final CircleArea circleArea = new CircleArea();
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// STACKPANE MIT Overlapping Circles (Grid mit mehreren Pyramiden nebeneinander)
		StackPane circleStack = CircleFactory.createCircleStack(400);
		circleArea.getCircleContainer().getChildren().add(circleStack);
		
		CircleFactory.bounce(circleStack);
		
		circleArea.setCallback(value -> {
			circleStack.setCache(value);
			System.out.println("Caching " + value);
		});
		
		Scene scene = new Scene(circleArea);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
