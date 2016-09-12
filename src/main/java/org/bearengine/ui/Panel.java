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

    public Panel(){
        super();
        super.Name = "UIPanel";
        Image image = ResourceLoader.Load("/main/java/resources/textures/ui/panel/white_flat_panel.png", Image.class, false);
        texture = new Texture().UploadTexture(image);
        BuildMesh();
    }

    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(PixelWidth, PixelHeight));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetDiffuseTexture(texture);
        super.CreateDebugMesh();
    }

    @Override
    protected void OnUpdate() {

    }

    @Override
    protected void MouseOver() {

    }

    @Override
    protected void MouseOverEnd() {

    }

    @Override
    protected void MouseClick() {

    }
    
    @Override
    protected void MouseHeld() {
        
    }
    
    
    @Override
    protected void MouseDown() {
        
    }
    
    @Override
    protected void MouseUp() {
        
    }
    
}
