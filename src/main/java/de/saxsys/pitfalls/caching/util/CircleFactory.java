package de.saxsys.pitfalls.caching.util;

import java.util.Random;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class CircleFactory {
	
	private static Random random = new Random();
	
	public static Circle createCircle(int radius) {
		Circle circle = new Circle(radius);
		circle.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		return circle;
	}
	
	public static Circle createCircleWithDropShadow() {
		Circle circle = createCircle(50);
		circle.setEffect(new DropShadow());
		return circle;
	}
	
	public static StackPane createCircleStack(int circleCount) {
		StackPane circleStack = new StackPane();
		for (int i = 480; i > 0; i--) {
			circleStack.getChildren().add(CircleFactory.createCircle(i));
		}
		return circleStack;
	}
	
	public static void moveCircle(Node node) {
		Scene scene = node.getScene();
		double toX = random.nextDouble() * (scene == null ? 0.0 : scene.getWidth());
		double toY = random.nextDouble() * (scene == null ? 0.0 : scene.getHeight());
		
		TranslateTransition transition = new TranslateTransition(Duration.seconds(1), node);
		transition.setToX(toX);
		transition.setToY(toY);
		transition.setOnFinished(e -> moveCircle(node));
		transition.play();
	}
	
	
	public static void bounce(Node node) {
		TranslateTransition transition = new TranslateTransition(Duration.seconds(0.2), node);
		transition.setByX(50);
		transition.setByY(50);
		transition.setAutoReverse(true);
		transition.setCycleCount(Timeline.INDEFINITE);
		transition.play();
	}
}
