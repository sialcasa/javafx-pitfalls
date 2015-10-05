package de.saxsys.pitfalls.caching;

import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WrongUseCaching extends Application {
	
	StackPane container = new StackPane();
	Random random = new Random();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		for (int i = 0; i < 10000; i++) {
			Circle circle = new Circle(50);
			circle.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			container.getChildren().add(circle);
			startMovemenet(circle);
		}
		
		ToggleButton caching = new ToggleButton("Caching");
		
		caching.selectedProperty().addListener(
				(ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
					setCircleCache(newValue);
					System.out.println("Caching " + newValue);
				});
		
		Pane vbox = new Pane(container, caching);
		
		Scene scene = new Scene(vbox);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void startMovemenet(Circle node) {
		
		Scene scene = node.getScene();
		double toX = random.nextDouble() * (scene == null ? 0.0 : scene.getWidth());
		double toY = random.nextDouble() * (scene == null ? 0.0 : scene.getHeight());
		
		TranslateTransition transition = new TranslateTransition(Duration.seconds(1), node);
		transition.setToX(toX);
		transition.setToY(toY);
		transition.setOnFinished(e -> startMovemenet(node));
		transition.play();
	}
	
	private void setCircleCache(boolean enabled) {
		for (int i = 0; i < container.getChildren().size(); i++) {
			Node node = container.getChildren().get(i);
			node.setCache(enabled);
			node.setCacheHint(CacheHint.SPEED);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
