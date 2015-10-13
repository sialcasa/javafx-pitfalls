package de.saxsys.pitfalls.caching;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import de.saxsys.pitfalls.caching.util.CircleFactory;
import de.saxsys.pitfalls.caching.view.CircleArea;

public class RightUsageOnGradientBackground extends Application {
	
	Random random = new Random();
	
	CircleArea circleArea = new CircleArea();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Pane circleContainer = circleArea.getCircleContainer();
		Pane someContainer = CircleFactory.createCircleStack(100);
		circleContainer.getChildren().add(someContainer);
		
		for (int i = 0; i < 80; i++) {
			Circle circle = CircleFactory.createCircleWithDropShadow();
			circleContainer.getChildren().add(circle);
			CircleFactory.moveCircle(circle);
		}
		
		circleArea.setCallback(value -> {
			someContainer.setCache(value);
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
