package de.saxsys.pitfalls.fxml;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class FXMLPerformance extends Application {
	
	public static void main(String[] args) throws IOException {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		for (int i = 0; i < 5; i++) {
			System.out.println("Loading Iteration###### " + i);
			loadWithWildcards();
			loadWithoutWildcards();
		}
	}
	
	private void loadWithoutWildcards() throws IOException {
		long startTime = System.currentTimeMillis();
		FXMLLoader loader = new FXMLLoader(FXMLPerformance.class.getResource("WithoutWildcards.fxml"));
		loader.load();
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Without Wildcards: \t" + estimatedTime);
	}
	
	private void loadWithWildcards() throws IOException {
		long startTime = System.currentTimeMillis();
		FXMLLoader loader = new FXMLLoader(FXMLPerformance.class.getResource("WithWildcards.fxml"));
		loader.load();
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("With Wildcards: \t" + estimatedTime);
	}
	
}
