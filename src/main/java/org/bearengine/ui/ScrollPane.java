package main.java.org.bearengine.ui;

import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.joml.Vector3f;

/**
 * Created by Stuart on 15/07/2016.
 */
public class ScrollPane extends UIObject {
     
    private Texture base_texture;
    
    private ScrollBar scrollBarVert, scrollBarHor;
    
    public ScrollPane(){
        this(null);
    }
    
    public ScrollPane(UIObject parent){
        super(parent);
        LoadTextures();
        BuildMesh();
    }
    
    private void LoadTextures(){
        //Base
        Image image = ResourceLoader.Load("/main/java/resources/textures/ui/scrollpane/scrollpane_base.png", Image.class);
        base_texture = new Texture().UploadTexture(image);
    }
    
    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(PixelWidth, PixelHeight));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetTexture(base_texture);
        
        super.CreateDebugMesh();
    }
    
    public void SetScrollBarVertical(ScrollBar scrollBar){
        this.scrollBarVert = scrollBar;
        scrollBar.SetScrollDirection(ScrollBar.ScrollDirection.Vertical);
        scrollBar.SetScrollCallback(this::ScrollCallback);
    }
    
    public void SetScrollBarHorizontal(ScrollBar scrollBar){
        this.scrollBarHor = scrollBar;
        scrollBar.SetScrollDirection(ScrollBar.ScrollDirection.Horizontal);
        scrollBar.SetScrollCallback(this::ScrollCallback);
    }
    
    public ScrollBar GetScrollBarVertical() {
        return scrollBarVert;
    }
    
    public ScrollBar GetScrollBarHorizontal() {
        return scrollBarHor;
    }
    
    @Override
    protected void OnUpdate() {
        float x = 0;
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
    
    public void ScrollCallback(ScrollBar.ScrollDirection dir, float percent){
        percent /= 100;
        Vector3f offset = new Vector3f(PixelOffset);
        if(dir == ScrollBar.ScrollDirection.Horizontal){
            offset.x = (percent * PixelWidth - PixelWidth);
        }else if(dir == ScrollBar.ScrollDirection.Vertical){
            offset.y = (percent * -PixelHeight - PixelHeight);
        }
        SetPixelOffset(offset);
    }
    
}
