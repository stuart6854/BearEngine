package main.java.org.bearengine.ui;

import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.utils.ResourceLoader;

/**
 * Created by Stuart on 09/07/2016.
 */
public class Caret extends UIObject {

    public Texture base_texture;

    public Caret(){
        this(null);
    }
    
    public Caret(UIObject parent) {
        super(parent);

        SetWidth(1);

        LoadTextures();
        BuildMesh();
    }

    private void LoadTextures(){
        //Base
        Image image = ResourceLoader.Load("/main/java/resources/textures/ui/caret/black_caret.png", Image.class);
        base_texture = new Texture().UploadTexture(image);
    }

    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(PixelWidth, PixelHeight));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetTexture(base_texture);

        super.BuildMesh();
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
