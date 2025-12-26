package org.megalodonte.props;

import javafx.scene.Node;
import javafx.scene.text.Text;
import org.megalodonte.reactivity.v2.ReadableState;
import org.megalodonte.utils.Commons;

public class TextPropsV2 extends Props {
    private Integer fontSize = 14;
    private ReadableState<Integer> fontSizeState;
    private String color;

    public TextPropsV2 fontSize(int fontSize){
        this.fontSize = fontSize;
        return this;
    }

    public TextPropsV2 fontSize(ReadableState<Integer> state) {
        this.fontSizeState = state;
        return this;
    }

    public TextPropsV2 color(String color){
        this.color = color;
        return this;
    }

    @Override
     public void apply(Node node) {
        if (!(node instanceof Text t)) return;

        if (fontSize != null) {
            applyFontSize(t, fontSize);
        }

        if (fontSizeState != null) {
            fontSizeState.subscribe(v -> applyFontSize(t, v));
        }

        if (color != null) {
            applyColor(t, color);
        }
    }

    private void applyFontSize(Text t, int size) {
        var current = t.getStyle();
        System.out.println(current);
        var updated = Commons.UpdateEspecificStyle(
                current,
                Commons.FX_FontSize,
                String.valueOf(size)
        );
        t.setStyle(updated);
    }

    private void applyColor(Text t, String color) {
        var current = t.getStyle();
        var updated = Commons.UpdateEspecificStyle(
                current,
                Commons.FX_FILL,
                color
        );
        t.setStyle(updated);
    }
}
