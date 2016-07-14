package main.java.org.bearengine.ui;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.input.ICharacterListener;
import main.java.org.bearengine.input.Keyboard;
import main.java.org.bearengine.input.Mouse;
import main.java.org.bearengine.utils.ResourceLoader;

/**
 * Created by Stuart on 04/07/2016.
 */
public class InputField extends UIObject implements ICharacterListener{

    public Texture base_texture;

    private String PlaceholderText = "Placeholder Text";
    private String InputText = "";

    private Label label;
    private Caret caret;

    private boolean IsActive = false;

    public InputField(Font font, UIObject parent){
        super();
        parent.AddChild(this);

        LoadTextures();
        BuildMesh();
        CreateLabel(PlaceholderText, font);
        CreateCaret();

        Keyboard.RegisterCharacterListener(this);
    }

    private void LoadTextures(){
        //Base
        Image image = ResourceLoader.Load("/main/java/resources/textures/ui/inputfield/inputfield_base.png", Image.class);
        base_texture = new Texture().UploadTexture(image);
    }

    @Override
    protected void update() {
        if(IsMouseOver()){
            if(Mouse.isButtonPressed(Mouse.BUTTON_LEFT)){
                IsActive = true;
                caret.IsVisible = true;

                if(InputText.isEmpty()){
                    label.SetText("");
                }
            }
        }else{
            if(Mouse.isButtonPressed(Mouse.BUTTON_LEFT)) {
                IsActive = false;
                caret.IsVisible = false;

                if(InputText.isEmpty()){
                    label.SetText(PlaceholderText);
                }
            }
        }

        if(IsActive){
            if(Keyboard.isClicked(Keyboard.KEY_BACKSPACE) && InputText.length() > 0){
                InputText = InputText.substring(0, InputText.length() - 1);
                InputText = InputText.replace("\\s+", "");
                Debug.log("Text:" + InputText + ";");
                RefreshLabel();
                UpdateCaretPosition();
            }
        }

        super.update();
    }

    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(PixelWidth, PixelHeight));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetTexture(base_texture);

        super.CreateDebugMesh();
    }

    private void CreateLabel(String text, Font font){
        Debug.log("InputField -> Creating Label.");
        label = new Label(text, font);
        label.SetNormalisedPosition(0f, .5f);
        label.SetPixelOffset(5, -(label.PixelHeight / 2f), 0);
//        label.SetShowDebugMesh(true);
        super.AddChild(label);
    }

    private void CreateCaret(){
        Debug.log("InputField -> Creating Caret.");
        Font font = label.GetFont();
        caret = new Caret();
        caret.SetHeight(font.FontFile.LineHeight * font.GetFontSize() + font.FontFile.PaddingHeight);
        caret.SetNormalisedPosition(0f, .5f);
        caret.SetPixelOffset(label.PixelOffset.x, -(caret.PixelHeight / 2f), 0);
//        caret.SetShowDebugMesh(true);
        caret.IsVisible = IsActive;
        super.AddChild(caret);
    }

    private void RefreshLabel(){
        label.SetText(InputText);
    }

    public void SetPlaceholderText(String text){
        this.PlaceholderText = text;
    }

    public String GetPlaceholderText(){
        return PlaceholderText;
    }

    @Override
    public void CharacterListener(int codepoint) {
        if(IsActive){
//            Debug.log("InputField -> Character Typed: " + (char)codepoint);
            char chr = (char)codepoint;
            InputText += chr;
            RefreshLabel();
            UpdateCaretPosition();
        }
    }

    private void UpdateCaretPosition(){
        float x = label.PixelOffset.x + label.PixelWidth;
        if(InputText.endsWith(" "))
            x += label.SpaceWidth;

        caret.SetPixelOffset(x, caret.PixelOffset.y, 0);
    }

    @Override
    protected void OnUpdate() {

    }

    @Override
    protected void OnMouseOver() {

    }

    @Override
    protected void OnMouseOverEnd() {

    }

    @Override
    protected void OnMouseClick() {
        IsActive = true;
        caret.IsVisible = true;

        if(InputText.isEmpty()){
            label.SetText("");
        }
    }

}