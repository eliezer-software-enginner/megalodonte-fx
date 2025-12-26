package my_app;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import toolkit.Component;

public class MainView extends StackPane {
    @Component
    Text title = new Text("Coesion App 2");

    @Component
    Text description = new Text("Your JavaFX base project with all the things already setup for you");

    @Component
    VBox layout = new VBox(title, description);
    public MainView() {
        getChildren().add(layout);
        getStyleClass().add("bg");

        title.getStyleClass().add("text");
        description.getStyleClass().add("text");

        layout.getStyleClass().add("home");
    }
}
