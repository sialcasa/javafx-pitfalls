package de.saxsys.pitfalls.caching.util;

import java.security.AccessControlException;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import com.sun.javafx.perf.PerformanceTracker;

public class FPSUtil {
	
	private static final Font FONT = Font.font("Verdana", 50);
	private static PerformanceTracker tracker;
	
	private static float getFPS() {
		float fps = tracker.getAverageFPS();
		return fps;
	}
	
	public static void resetAverageFPS() {
		tracker.resetAverageFPS();
	}
	
	public static void displayFPS(Label fpsLabel) {
		fpsLabel.sceneProperty().addListener((ChangeListener<Scene>) (observable, oldValue, newValue) -> {
			if (newValue != null) {
				try {
					System.setProperty("prism.verbose", "true");
					System.setProperty("prism.dirtyopts", "false");
					// System.setProperty("javafx.animation.fullspeed", "true");
				System.setProperty("javafx.animation.pulse", "10");
			} catch (AccessControlException e) {
			}
			
			tracker = PerformanceTracker.getSceneTracker(newValue);
			AnimationTimer frameRateMeter = new AnimationTimer() {
				
				@Override
				public void handle(long now) {
					fpsLabel.setText(String.format("FPS: %.0f fps", getFPS()));
					
				}
			};
			frameRateMeter.start();
		}
	}	);
		
	}
	
}
