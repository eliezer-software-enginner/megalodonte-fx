package org.megalodonte.props;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ColumnProps extends Props {
    private double minWidth = -1;
    private double maxHeight = Double.MAX_VALUE;

    ColumnProps minWidth(double minWidth){
        this.minWidth = minWidth;
        return this;
    }

    @Override
    protected void apply(Node node) {
        if (node instanceof VBox vBox) {
            if (minWidth >= 0) {
                vBox.setMinWidth(minWidth);
            }
            if (maxHeight >= 0) {
                vBox.setMaxHeight(maxHeight);
            }
        }
    }
}
