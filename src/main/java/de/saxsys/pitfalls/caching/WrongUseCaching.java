package de.saxsys.pitfalls.caching;

import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import de.saxsys.pitfalls.caching.util.CircleFactory;
import de.saxsys.pitfalls.caching.view.CircleArea;

public class WrongUseCaching extends Application {
	
	private final CircleArea circleArea = new CircleArea();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Pane circleContainer = circleArea.getCircleContainer();
		
		for (int i = 0; i < 4000; i++) {
			Circle createCircle = CircleFactory.createCircle(50);
			circleContainer.getChildren().add(createCircle);
			CircleFactory.moveCircle(createCircle);
		}
		
		circleArea.setCallback(value -> {
			setCircleCache(value);
			System.out.println("Caching " + value);
		});
		
		Scene scene = new Scene(circleArea);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	private void setCircleCache(boolean enabled) {
		Pane circleContainer = circleArea.getCircleContainer();
		for (int i = 0; i < circleContainer.getChildren().size(); i++) {
			Node node = circleContainer.getChildren().get(i);
			node.setCache(enabled);
			node.setCacheHint(CacheHint.SPEED);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
