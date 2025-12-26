package my_app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.megalodonte.components.Column;
import org.megalodonte.components.TextV2;
import org.megalodonte.reactivity.v2.ComputedState;
import org.megalodonte.reactivity.v2.State;

import java.time.Duration;


public class App extends Application {
    public static final StackPane ROOT = new StackPane();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Meu app");

        State<Integer> count = new State<>(1);

        ComputedState<String> label =
                ComputedState.of(
                        () -> "Total: " + count.get(),
                        count
                );

        var text = new TextV2(label);

        var column = new Column()
                .child(text);

                Thread.ofVirtual().start(()->{
            try {
                Thread.sleep(Duration.ofSeconds(2));
                Platform.runLater(()-> count.set(2));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        //State<String> text = new State<>("Olá mundo");
//        var column = new Column()
//                .child(new Text(text))
//                .child(new Text("Text 2"));

//        Thread.ofVirtual().start(()->{
//            try {
//                Thread.sleep(Duration.ofSeconds(2));
//                Platform.runLater(()-> text.set("Novo texto"));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });

        ROOT.getChildren().add(column.getNode());

        Scene scene = new Scene(ROOT, 700, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}