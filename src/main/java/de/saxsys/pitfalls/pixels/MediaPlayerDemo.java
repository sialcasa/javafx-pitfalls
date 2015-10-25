package de.saxsys.pitfalls.pixels;

import com.guigarage.flatterfx.FlatterFX;
import com.sun.javafx.perf.PerformanceTracker;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Andy Moncsek on 19.08.15.
 */
public class MediaPlayerDemo
        extends Application {

    public static final String MOVIE_FILE = "H264_AAC_(720p)(mkvmerge).mkv";
    public static final int WIDTH = 1024;
    public static final int HIGHT = 768;

    private AtomicLong counter = new AtomicLong(0);

    File file = new File(getClass().getClassLoader().getResource(MOVIE_FILE).getFile());

    public static void main(final String[] args) {
        Application.launch(args);
    }

    private Label fpsLabel;
    private PerformanceTracker tracker;

    @Override
    public void start(Stage stage) throws Exception {

        FlatterFX.style();
        System.setProperty("javafx.animation.fullspeed", "true");
        long startTime = System.currentTimeMillis();

        VBox main = new VBox();
        VBox imageBox = new VBox();
        imageBox.setStyle("-fx-background-color: gainsboro");
        StackPane root = new StackPane();
        Scene scene = new Scene(main, WIDTH, HIGHT);
        imageBox.setPrefHeight(80);
        root.setPrefHeight(1024);




        main.getChildren().addAll(imageBox, root);
        stage.setTitle(getClass().getSimpleName());
        stage.setScene(scene);

        stage.show();

        Media media = new Media("http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv");
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

        MediaView mediaView = new MediaView(mediaPlayer);

        fpsLabel = new Label("FPS:");
        fpsLabel.setStyle("-fx-font-size: 5em;-fx-text-fill: red;");
        fpsLabel.setOnMouseClicked((event) -> {
            tracker.resetAverageFPS();
        });

        createPerformanceTracker(scene);
        imageBox.getChildren().add(fpsLabel);
        root.getChildren().addAll(mediaView);
    }



    public void createPerformanceTracker(Scene scene)
    {
        tracker = PerformanceTracker.getSceneTracker(scene);
        AnimationTimer frameRateMeter = new AnimationTimer()
        {

            @Override
            public void handle(long now)
            {

                float fps = getFPS();
                fpsLabel.setText(String.format("Current fps: %.0f fps", fps));

            }
        };

        frameRateMeter.start();
    }

    private float getFPS()
    {
        float fps = tracker.getAverageFPS();
        if(counter.incrementAndGet()%100==0) {
            tracker.resetAverageFPS();
        }

        return fps;
    }
}
