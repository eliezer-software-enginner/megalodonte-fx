package org.megalodonte.components;

import javafx.scene.layout.VBox;
import org.megalodonte.props.ColumnProps;

public class Column extends Component {
    private final VBox vBox;

    /*
        var columnProps = new ColumnProps()
        new Column(columnProps)
        .child(new Text("ola mundo"))
        .child(new Text("text 2"))
     */
    public Column(){
        super(new VBox());
        this.vBox = (VBox) this.node;
    }

    public Column(ColumnProps props){
        super(new VBox(), props);
        this.vBox = (VBox) this.node;
    }

    public Column child(Component component){
        this.vBox.getChildren().add(component.getNode());
        return this;
    }
}
