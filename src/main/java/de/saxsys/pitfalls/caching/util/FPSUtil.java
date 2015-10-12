package de.saxsys.pitfalls.caching.util;

import java.security.AccessControlException;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import com.sun.javafx.perf.PerformanceTracker;

public class FPSUtil {
	
	private static final Font FONT = Font.font("Verdana", 50);
	private static PerformanceTracker tracker;
	
	public static void displayFPS(Scene scene, Pane displayOn) {
		Label label1 = new Label();
		Label label2 = new Label();
		label1.setFont(FONT);
		label2.setFont(FONT);
		VBox vBox = new VBox(label1, label2);
		displayOn.getChildren().add(0, vBox);
		
		try {
			System.setProperty("prism.verbose", "true");
			System.setProperty("prism.dirtyopts", "false");
			// System.setProperty("javafx.animation.fullspeed", "true");
			System.setProperty("javafx.animation.pulse", "10");
		} catch (AccessControlException e) {
		}
		
		scene.setOnKeyPressed((e) -> {
			label2.setText(label1.getText());
		});
		
		tracker = PerformanceTracker.getSceneTracker(scene);
		AnimationTimer frameRateMeter = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				label1.setText(String.format("Current frame rate: %.3f fps", getFPS()));
			}
		};
		
		frameRateMeter.start();
	}
	
	
	private static float getFPS() {
		float fps = tracker.getAverageFPS();
		tracker.resetAverageFPS();
		return fps;
	}
	
}
