package org.megalodonte.components;


import org.megalodonte.props.TextProps;
import org.megalodonte.reactivity.v2.ReadableState;

public class TextV2 extends Component {
    private final javafx.scene.text.Text text;

    public TextV2(String textContent){
        super(new javafx.scene.text.Text(textContent));
        this.text = (javafx.scene.text.Text) this.node;
    }

    public TextV2(String textContent, TextProps props){
        super(new javafx.scene.text.Text(textContent), props);
        this.text = (javafx.scene.text.Text) this.node;
    }

    public TextV2(ReadableState<String> state) {
        super(new javafx.scene.text.Text());
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
    }

    public TextV2(ReadableState<String> state, TextProps props) {
        super(new javafx.scene.text.Text(), props);
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
    }
}
