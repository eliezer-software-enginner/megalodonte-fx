package org.megalodonte.props;

import javafx.scene.Node;
import javafx.scene.text.Text;
import org.megalodonte.utils.Commons;

public class TextProps extends Props {
    private int fontSize = 14;
    private String color;

    public TextProps fontSize(int fontSize){
        this.fontSize = fontSize;
        return this;
    }

    public TextProps color(String color){
        this.color = color;
        return this;
    }

    @Override
    public void apply(Node node) {
        if(node instanceof Text t){
            var currentStyle = t.getStyle();
            var updatedStyle = currentStyle = Commons.UpdateEspecificStyle(currentStyle, Commons.FX_FontSize, String.valueOf(fontSize));
            updatedStyle = Commons.UpdateEspecificStyle(currentStyle, Commons.FX_FILL, color);

            t.setStyle(updatedStyle);
        }
    }
}
