package main.java.org.bearengine.ui;

import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;

/**
 * Created by Stuart on 11/06/2016.
 */
public class Panel extends UIObject {

    private Texture texture;

    public Panel(int width, int height){
        super(0, 0, width, height);
        Image image = new Image("/main/java/resources/textures/ui/panel/white_flat_panel.png");
        texture = new Texture().UploadTexture(image);
        BuildMesh();
    }

    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(Width, Height));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetTexture(texture);
    }

}