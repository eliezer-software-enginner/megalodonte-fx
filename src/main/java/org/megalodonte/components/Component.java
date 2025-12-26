package org.megalodonte.components;

import javafx.scene.Node;
import org.megalodonte.props.Props;

public abstract class Component {
    protected final Node node;
    protected Props props;

    protected Component(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    protected Component(Node node, Props props) {
        this.node = node;

        if(props != null){
            this.props = props;
            this.props.apply(node);
        }
    }

}
