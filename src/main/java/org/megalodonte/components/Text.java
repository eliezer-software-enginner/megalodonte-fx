package org.megalodonte.components;


import org.megalodonte.props.TextProps;
import org.megalodonte.reactivity.State;

public class Text extends Component {
    private final javafx.scene.text.Text text;

    public Text(String textContent){
        super(new javafx.scene.text.Text(textContent));
        this.text = (javafx.scene.text.Text) this.node;
    }

    public Text(String textContent, TextProps props){
        super(new javafx.scene.text.Text(textContent), props);
        this.text = (javafx.scene.text.Text) this.node;
    }

    public Text(State<String> state) {
        super(new javafx.scene.text.Text());
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
    }

    public Text(State<String> state,  TextProps props) {
        super(new javafx.scene.text.Text(), props);
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
    }
}
