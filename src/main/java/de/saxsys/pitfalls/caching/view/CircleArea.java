package de.saxsys.pitfalls.caching.view;

import de.saxsys.pitfalls.caching.util.FPSUtil;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.function.Consumer;

public class CircleArea extends Region {
	
	
	@FXML
	private Pane circleContainer;
	
	@FXML
	private ToggleButton cacheToggle;
	
	@FXML
	private Label fpsLabel;
	
	private Consumer<Boolean> callback;
	
	public CircleArea() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/CircleArea.fxml"));
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.getChildren().add(loader.getRoot());
	}
	
	@FXML
	void initialize() {
		cacheToggle.selectedProperty().addListener(
				(ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
					callback.accept(newValue);
					FPSUtil.resetAverageFPS();
					if (newValue) {
						cacheToggle.setText("Disable Caching");
					} else {
						cacheToggle.setText("Enable Caching");
					}
				});
		
		FPSUtil.displayFPS(fpsLabel);
	}
	
	public Pane getCircleContainer() {
		return circleContainer;
	}
	
	public Consumer<Boolean> getCallback() {
		return callback;
	}
	
	public void setCallback(Consumer<Boolean> callback) {
		this.callback = callback;
	}
	
	
	
	
}
