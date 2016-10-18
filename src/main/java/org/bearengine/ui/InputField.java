package main.java.org.bearengine.ui;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.input.ICharacterListener;
import main.java.org.bearengine.input.Keyboard;
import main.java.org.bearengine.utils.ResourceLoader;

/**
 * Created by Stuart on 04/07/2016.
 */
public class InputField extends UIObject implements ICharacterListener{

    //TODO: Allow user to change current input location using arrow keys or by clicking on position in input(change cursor to show this)
    
    public Texture base_texture;

    private String InputText = "";

    private Label label;
    private Label placeholderLabel;
    private Caret caret;

    private boolean IsActive = false;
    
//    public InputField(Font font, Font placeholderFont){
//        this(font, placeholderFont, null);
//    }
    
    public InputField(Font font, Font placeholderFont, UIObject parent){
        super(parent);

        LoadTextures();
        BuildMesh();
        this.label = CreateLabel(InputText, font);
        this.placeholderLabel = CreateLabel("InputField", placeholderFont);
        CreateCaret();

        Keyboard.RegisterCharacterListener(this);
    }

    private void LoadTextures(){
        //Base
        Image image = ResourceLoader.Load("/main/java/resources/textures/ui/inputfield/inputfield_base.png", Image.class);
        base_texture = new Texture().UploadTexture(image);
    }

    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(PixelWidth, PixelHeight));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetDiffuseTexture(base_texture);

        super.CreateDebugMesh();
    }

    private Label CreateLabel(String text, Font font){
        Debug.log("InputField -> Creating Label.");
        Label lbl = new Label(text, font, this);
        lbl.SetNormalisedPosition(0f, .5f);
        lbl.SetPixelOffset(5, -(lbl.PixelHeight / 2f), 0);
//        lbl.SetShowDebugMesh(true);
        return lbl;
    }

    private void CreateCaret(){
        Debug.log("InputField -> Creating Caret.");
        Font font = label.GetFont();
        caret = new Caret(this);
        caret.SetHeight(font.FontFile.LineHeight * font.GetFontSize() + font.FontFile.PaddingHeight);
        caret.SetNormalisedPosition(0f, .5f);
        caret.SetPixelOffset(label.PixelOffset.x, -(caret.PixelHeight / 2f), 0);
//        caret.SetShowDebugMesh(true);
        caret.IsVisible = IsActive;
    }

    private void RefreshLabel(){
        label.SetText(InputText);
        label.SetPixelOffset(5, -(label.PixelHeight / 2f), 0);
        UpdateCaretPosition();
    }

    public void Clear(){
        SetInputText("");
    }

    public void setActive(boolean active){
        if(active) StartEditing();
        else StopEditing();
    }

    public void SetInputText(String text){
        InputText = text;
        RefreshLabel();
    }

    public String GetInputText(){
        return InputText;
    }

    public void SetPlaceholderText(String text){
        this.placeholderLabel.SetText(text);
    }

    public String GetPlaceholderText(){
        return placeholderLabel.GetText();
    }

    public boolean IsActive(){
        return IsActive;
    }

    public boolean IsEmpty(){
        return label.GetText().isEmpty();
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
        if(IsActive){
            if(Keyboard.isClicked(Keyboard.KEY_BACKSPACE) && InputText.length() > 0){
                InputText = InputText.substring(0, InputText.length() - 1);
                InputText = InputText.replace("\\s+", "");
//                Debug.log("Text:" + InputText);
                RefreshLabel();
                UpdateCaretPosition();
            }
            
//            if(Keyboard.isClicked(Keyboard.KEY_ENTER)){
//                StopEditing();
//            }
        }
    }
    
    private void StartEditing(){
        IsActive = true;
        caret.IsVisible = true;
    
        placeholderLabel.IsVisible = false;
        label.IsVisible = true;
    }
    
    private void StopEditing(){
        IsActive = false;
        caret.IsVisible = false;
    
        if(InputText.isEmpty()){
            placeholderLabel.IsVisible = true;
            label.IsVisible = false;
        }
    }

    @Override
    protected void MouseOver() {

    }

    @Override
    protected void MouseOverEnd() {

    }

    @Override
    protected void MouseClick() {
        StartEditing();
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
    
    @Override
    public void OnMouseClick() {
        if(IsMouseOver()) {
            this.MouseClick();
        }else{
            StopEditing();
        }
    }
}