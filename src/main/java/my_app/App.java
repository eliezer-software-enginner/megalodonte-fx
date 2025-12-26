package my_app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.megalodonte.components.Column;
import org.megalodonte.components.TextV2;
import org.megalodonte.props.TextPropsV2;
import org.megalodonte.reactivity.v2.ComputedState;
import org.megalodonte.reactivity.v2.State;

import java.time.Duration;

public class App extends Application {
    public static final StackPane ROOT = new StackPane();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Meu app");

        State<Integer> count = new State<>(1);
        State<Integer> fontSize = new State<>(14);

        ComputedState<String> label =
                ComputedState.of(
                        () -> "Total: " + count.get(),
                        count
                );


        var column = new Column()
                .child(new TextV2(label, new TextPropsV2().fontSize(fontSize)));

                Thread.ofVirtual().start(()->{
            try {
                Thread.sleep(Duration.ofSeconds(2));
                Platform.runLater(()-> count.set(2));
                Thread.sleep(Duration.ofSeconds(1));
                Platform.runLater(()-> fontSize.set(40));
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