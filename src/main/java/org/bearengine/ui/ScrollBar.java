package main.java.org.bearengine.ui;

import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.math.MathUtils;
import main.java.org.bearengine.utils.ResourceLoader;

/**
 * Created by Stuart on 15/07/2016.
 */
public class ScrollBar extends UIObject {
    
    public enum ScrollDirection{ Horizontal, Vertical }
    
    private Texture base_texture;
    
    private float ControlPercentage = 0f; //How far up/down or left/right the scroll bar is
    
    private ScrollBarControl scrollBarControl;
    private ScrollDirection scrollDirection;
    
    private IScrollCallback scrollCallback;
    
    public ScrollBar(){
        this(null);
    }
    
    public ScrollBar(UIObject parent){
        super(parent);
        
        LoadTextures();
        BuildMesh();
    }
    
    private void LoadTextures(){
        //Base
        Image image = ResourceLoader.Load("/main/java/resources/textures/ui/scrollbar/scrollbar_base.png", Image.class);
        base_texture = new Texture().UploadTexture(image);
    }
    
    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(PixelWidth, PixelHeight));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetDiffuseTexture(base_texture);
        UpdateScrollBarControl();
        
        super.CreateDebugMesh();
    }
    
    private void UpdateScrollBarControl(){
        if(scrollBarControl == null)
            scrollBarControl = new ScrollBarControl(this);
        
        if(PixelWidth < PixelHeight) {
            scrollBarControl.SetNormalisedPosition(0.5f, 0);
            scrollBarControl.SetWidth(PixelWidth + 4);
            scrollBarControl.SetHeight(PixelWidth + 4);
        }else if(PixelHeight < PixelWidth){
            scrollBarControl.SetNormalisedPosition(0, 0.5f);
            scrollBarControl.SetWidth(PixelHeight + 4);
            scrollBarControl.SetHeight(PixelHeight + 4);
        }
        scrollBarControl.SetPixelOffset(-(scrollBarControl.PixelWidth / 2f), -(scrollBarControl.PixelHeight / 2f), 0);
        
        scrollBarControl.SetScrollCallback(this::ScrollCallback);
    }
    
    public void ScrollCallback(float x, float y){
        float percentage = 0;
        if(scrollDirection == ScrollDirection.Horizontal){
            x = MathUtils.Clamp(x, -(scrollBarControl.PixelWidth / 2f), -(scrollBarControl.PixelWidth / 2f) + PixelWidth);
            float tX= x + (scrollBarControl.PixelWidth / 2f);
            percentage = (tX / PixelWidth) * 100;
        }else if(scrollDirection == ScrollDirection.Vertical){
            y = MathUtils.Clamp(y, -(scrollBarControl.PixelHeight / 2f), -(scrollBarControl.PixelHeight / 2f) + PixelHeight);
            float tY = y + (scrollBarControl.PixelHeight / 2f);
            percentage = (tY / PixelHeight) * 100;
        }
        
        scrollBarControl.SetPixelOffset(x, y, 0);
        ControlPercentage = MathUtils.Clamp(percentage, 0.0f, 100.0f);
        scrollCallback.ScrollCallback(scrollDirection, ControlPercentage);
    }
    
    public void SetScrollDirection(ScrollDirection dir){
        this.scrollDirection = dir;
    }
    
    public ScrollDirection GetScrollDirection() {
        return scrollDirection;
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
    
    public void SetScrollCallback(IScrollCallback scrollCallback){
        this.scrollCallback = scrollCallback;
    }
    
    public interface IScrollCallback{
        void ScrollCallback(ScrollDirection dir, float percent);
    }
    
}
