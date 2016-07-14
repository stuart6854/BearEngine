package main.java.org.bearengine.ui;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.utils.ResourceLoader;

/**
 * Created by Stuart on 24/06/2016.
 */
public class Button extends UIObject{

    private enum ButtonStates{ INACTIVE, HOVERED, CLICKED}

    private Texture base_texture, hovered_texture, clicked_texture;

    private Label label;

    private ButtonStates ButtonState = ButtonStates.INACTIVE;

    public Button(String text, Font font, UIObject parent){
        super();
        parent.AddChild(this);

        LoadTextures();
        BuildMesh();
        CreateLabel(text, font);
    }

    public boolean IsClicked(){
        return ButtonState == ButtonStates.CLICKED;
    }

    private void LoadTextures(){
        //Base
        Image image = ResourceLoader.Load("/main/java/resources/textures/ui/button/flat_red/flat_red_base.png", Image.class);
        base_texture = new Texture().UploadTexture(image);
        //Hovered
        image = ResourceLoader.Load("/main/java/resources/textures/ui/button/flat_red/flat_red_hovered.png", Image.class);
        hovered_texture = new Texture().UploadTexture(image);
        //Clicked
        image = ResourceLoader.Load("/main/java/resources/textures/ui/button/flat_red/flat_red_clicked.png", Image.class);
        clicked_texture = new Texture().UploadTexture(image);
    }

    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(PixelWidth, PixelHeight));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetTexture(base_texture);

        super.CreateDebugMesh();
    }

    private void CreateLabel(String text, Font font){
        Debug.log("Button -> Creating Label.");
        label = new Label(text, font);
        label.SetNormalisedPosition(0.5f, 0.5f);
        label.SetPixelOffset(-(label.PixelWidth / 2f), -(label.PixelHeight / 2f), 0);
        label.SetShowDebugMesh(true);
        super.AddChild(label);
    }

    @Override
    protected void OnUpdate() {

    }

    @Override
    protected void OnMouseOver() {
        ButtonState = ButtonStates.HOVERED;
        this.mesh.material.SetTexture(hovered_texture);
    }

    @Override
    protected void OnMouseOverEnd() {
        ButtonState = ButtonStates.INACTIVE;
        this.mesh.material.SetTexture(base_texture);
    }

    @Override
    protected void OnMouseClick() {
        ButtonState = ButtonStates.CLICKED;
        this.mesh.material.SetTexture(clicked_texture);
    }
}
