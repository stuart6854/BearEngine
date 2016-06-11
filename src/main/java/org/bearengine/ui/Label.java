package main.java.org.bearengine.ui;

import main.java.org.bearengine.font.Font;

/**
 * Created by Stuart on 10/06/2016.
 */
public class Label extends UIObject {

    private String Text;
    private Font font;

    public Label(String text, Font font){
        super();
        this.Text = text;
        this.font = font;

        BuildMesh();
    }

    @Override
    public void BuildMesh() {

    }

}
