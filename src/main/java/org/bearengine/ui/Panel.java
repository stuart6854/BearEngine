package main.java.org.bearengine.ui;

import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.utils.ResourceLoader;

/**
 * Created by Stuart on 11/06/2016.
 */
public class Panel extends UIObject {

    private Texture texture;

    public Panel(int width, int height){
        super(0, 0, width, height);
        Image image = Image.GetImage("textures/ui/panel/grey_panel.png", ResourceLoader.FileType.Internal);
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
