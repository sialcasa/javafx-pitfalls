package de.saxsys.pitfalls.caching;

import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import de.saxsys.pitfalls.caching.util.FPSUtil;

public class RightUsageOfCaching extends Application {
	
	VBox container = new VBox();
	Random random = new Random();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// STACKPANE MIT Overlapping Circles (Grid mit mehreren Pyramiden nebeneinander)
		StackPane last = new StackPane();
		container.getChildren().add(last);
		
		for (int i = 500; i > 0; i--) {
			Circle circle = new Circle(i);
			circle.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			last.getChildren().add(circle);
		}
		
		startMovemenet(last);
		
		ToggleButton caching = new ToggleButton("Cache this magical circle");
		
		
		caching.selectedProperty().addListener(
				(ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
					last.setCache(newValue);
					System.out.println("Caching " + newValue);
				});
		
		VBox vbox = new VBox(caching, container);
		
		Scene scene = new Scene(vbox, 1000, 1300);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		FPSUtil.displayFPS(scene, vbox);
	}
	
	private void startMovemenet(Node node) {
		double toX = random.nextDouble() * 100;
		double toY = random.nextDouble() * 100;
		TranslateTransition transition = new TranslateTransition(Duration.seconds(0.2), node);
		transition.setToX(toX);
		transition.setToY(toY);
		transition.setOnFinished(e -> startMovemenet(node));
		transition.play();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
