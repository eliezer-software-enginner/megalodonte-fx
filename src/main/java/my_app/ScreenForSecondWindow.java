package my_app;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ScreenForSecondWindow extends VBox {

    public ScreenForSecondWindow(){
        // Conteúdo finalizado para Hot Reload
        Text text = new Text("Screen Content - bla bla");
        text.getStyleClass().add("text2");

        getChildren().add(text);
        setAlignment(Pos.CENTER);

        // Estilo básico para a tela secundária
        getStyleClass().add("second-screen-root");
    }
}