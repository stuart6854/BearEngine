package main.java.org.bearengine.ui;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.input.Mouse;
import main.java.org.bearengine.utils.ResourceLoader;

/**
 * Created by Stuart on 24/06/2016.
 */
public class Button extends UIObject{

    private Texture base_texture, hovered_texture, clicked_texture;

    private Label label;

    public Button(String text, Font font, float width, float height){
        super(0, 0, width, height);
        LoadTextures();
        BuildMesh();
        CreateLabel(text, font);
    }

    @Override
    protected void update() {
        if(IsMouseOver()){
            if(Mouse.isButtonPressed(Mouse.BUTTON_LEFT)){
                this.mesh.material.SetTexture(clicked_texture);
            }else{
                this.mesh.material.SetTexture(hovered_texture);
            }
        }else{
            this.mesh.material.SetTexture(base_texture);
        }

        super.update();
    }

    public boolean IsClicked(){
        return IsClicked(Mouse.BUTTON_LEFT);
    }

    public boolean IsClicked(int mouseButton){
        return (IsMouseOver() && Mouse.isButtonClicked(mouseButton));
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
        this.setMesh(UIMesh.Square(Width, Height));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetTexture(base_texture);
    }

    private void CreateLabel(String text, Font font){
        Debug.log("Button -> Creating Label.");
        label = new Label(text, font);
        label.SetAnchor(Anchor.CENTRE);
        label.SetPosition(0, 0, 0);
        super.AddChild(label);
    }

}
