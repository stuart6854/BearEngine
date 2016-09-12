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

    private enum ButtonStates{ INACTIVE, HOVERED, ACTIVE, CLICKED}

    private Texture inactive_texture, hovered_texture, active_texture;

    private Label label;

    private ButtonStates ButtonState = ButtonStates.INACTIVE;
    
    public Button(String text, Font font){
        this(text, font, null);
    }
    
    public Button(String text, Font font, UIObject parent){
        super(parent);
        super.Name = "UIButton";
        
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
        inactive_texture = new Texture().UploadTexture(image);
        //Hovered
        image = ResourceLoader.Load("/main/java/resources/textures/ui/button/flat_red/flat_red_hovered.png", Image.class);
        hovered_texture = new Texture().UploadTexture(image);
        //Clicked
        image = ResourceLoader.Load("/main/java/resources/textures/ui/button/flat_red/flat_red_clicked.png", Image.class);
        active_texture = new Texture().UploadTexture(image);
    }

    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(PixelWidth, PixelHeight));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetDiffuseTexture(inactive_texture);

        super.CreateDebugMesh();
    }

    private void CreateLabel(String text, Font font){
        Debug.log("Button -> Creating Label.");
        label = new Label(text, font, this);
        label.SetNormalisedPosition(0.5f, 0.5f);
        label.SetPixelOffset(-(label.PixelWidth / 2f), -(label.PixelHeight / 2f), 0);
        label.SetShowDebugMesh(true);
    }

    @Override
    protected void OnUpdate() {
        if(ButtonState == ButtonStates.CLICKED)
            MouseUp();
    }

    @Override
    protected void MouseOver() {
        ButtonState = ButtonStates.HOVERED;
        this.mesh.material.SetDiffuseTexture(hovered_texture);
    }

    @Override
    protected void MouseOverEnd() {
        ButtonState = ButtonStates.INACTIVE;
        this.mesh.material.SetDiffuseTexture(inactive_texture);
    }

    @Override
    protected void MouseClick() {
        ButtonState = ButtonStates.CLICKED;
    }
    
    @Override
    protected void MouseHeld() {
        
    }
    
    @Override
    protected void MouseDown() {
        ButtonState = ButtonStates.ACTIVE;
        this.mesh.material.SetDiffuseTexture(active_texture);
    }
    
    @Override
    protected void MouseUp() {
        if(IsMouseOver() && !IsMouseOverChild()) {
            ButtonState = ButtonStates.HOVERED;
            this.mesh.material.SetDiffuseTexture(hovered_texture);
        }else{
            ButtonState = ButtonStates.INACTIVE;
            this.mesh.material.SetDiffuseTexture(inactive_texture);
        }
    }
    
    @Override
    public void OnMouseDown() {
        if(IsMouseOver())
            this.MouseDown();
    }
    
    @Override
    public void OnMouseUp() {
        if(IsMouseOver())
            MouseUp();
    }
    
    @Override
    public void OnMouseClick() {
        if(IsMouseOver())
            this.MouseClick();
    }
    
}
