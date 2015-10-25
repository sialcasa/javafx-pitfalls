package de.saxsys.pitfalls.pixels;

import com.guigarage.flatterfx.FlatterFX;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Andy Moncsek on 19.08.15.
 */
public class VLCDemo
        extends Application {

    public static final String MOVIE_FILE = "H264_AAC_(720p)(mkvmerge).mkv";
    public static final int WIDTH = 1024;
    public static final int HIGHT = 768;

    File file = new File(getClass().getClassLoader().getResource(MOVIE_FILE).getFile());


    private DirectMediaPlayerComponent mp;
    WritablePixelFormat<ByteBuffer> byteBgraInstance = PixelFormat.getByteBgraPreInstance();
    //final WritablePixelFormat<ByteBuffer> byteBgraInstance = PixelFormat.getByteBgraInstance();  // negative test

    static {
        NativeLibrary.addSearchPath("vlc", "/Applications/VLC.app/Contents/MacOS/lib/");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FlatterFX.style();
        StackPane stack = new StackPane();
        final Canvas canvas = new Canvas(WIDTH, HIGHT);
        final Label label = new Label("");
        final VBox vBox = new VBox();
        final Button bSwitch= new Button("switch to ByteBgra");

        bSwitch.setOnAction((event) -> swtichPrixelformat(bSwitch));
        vBox.getChildren().addAll(bSwitch,label);

        label.setStyle("-fx-font: 36px \"Segoe UI Semibold\";-fx-text-fill: white;");
        StackPane.setMargin(label, new Insets(0, 10, 0, 0));

        stack.getChildren().addAll(canvas, vBox);
        Scene scene = new Scene(stack);
        final PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();


        mp = new DirectMediaPlayerComponent(formatCallback) {

            private AtomicLong totalTime = new AtomicLong(0);
            private long totalFrames;

            @Override
            public void display(DirectMediaPlayer mediaPlayer,
                                Memory[] nativeBuffers, final BufferFormat bufferFormat) {

                final Memory nativeBuffer = nativeBuffers[0];
                final ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
                totalFrames++;
                Platform.runLater(() -> {
                            long startTime = System.currentTimeMillis();
                            pixelWriter.setPixels(0, 0, WIDTH, HIGHT, byteBgraInstance, byteBuffer, WIDTH * 4);
                            long renderTime = System.currentTimeMillis() - startTime;
                            totalTime.set(totalTime.longValue()+renderTime);
                            String s = String.format("Frames: %4d   Avg.time: %4.1f ms    (Max)FPS: %3.0f fps\n", totalFrames, (double) totalTime.longValue() / totalFrames,  1000.0 / ((double) totalTime.longValue() / totalFrames));
                            label.setText(s);
                        }
                );
            }
        };

        mp.getMediaPlayer().playMedia(file.getPath());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void swtichPrixelformat(Button bSwitch) {
        if(byteBgraInstance.getType().equals(PixelFormat.Type.BYTE_BGRA_PRE)){
            byteBgraInstance = PixelFormat.getByteBgraInstance();
            bSwitch.setText("switch to ByteBgraPre");
        }   else {
            byteBgraInstance = PixelFormat.getByteBgraPreInstance();
            bSwitch.setText("switch to ByteBgra");
        }
    }


    /**
     * Callback to get the buffer format to use for video playback.
     */
    private final BufferFormatCallback formatCallback = (sourceWidth, sourceHeight) -> {

        int width = WIDTH;
        int height = HIGHT;

        return new RV32BufferFormat(width, height);
    };

    public static void main(final String[] args) {
        Application.launch(args);
    }
}
