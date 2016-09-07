package main.java.org.bearengine.ui;

import main.java.org.bearengine.graphics.rendering.RenderSpace;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.input.Mouse;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.joml.Vector2d;
import main.java.org.joml.Vector2f;
import main.java.org.joml.Vector3d;
import main.java.org.joml.Vector3f;

/**
 * Created by Stuart on 15/07/2016.
 */
public class ScrollBarControl extends UIObject {
    
    private Texture inactive_texture, active_texture;
    
    private ScrollBar scrollBar;
    private IScrollCallback scrollCallback;
    
    private boolean IsGrabbed = false;
    
    public ScrollBarControl(ScrollBar scrollbar){
        super(scrollbar);
        this.scrollBar = scrollbar;
        
        scrollbar.AddChild(this);
        
        LoadTextures();
        BuildMesh();
    }
    
    private void LoadTextures(){
        //Inactive
        Image image = ResourceLoader.Load("/main/java/resources/textures/ui/scrollbar/scrollbar_control_inactive.png", Image.class);
        inactive_texture = new Texture().UploadTexture(image);
        //Active
        image = ResourceLoader.Load("/main/java/resources/textures/ui/scrollbar/scrollbar_control_active.png", Image.class);
        active_texture = new Texture().UploadTexture(image);
    }
    
    @Override
    public void BuildMesh() {
        this.setMesh(UIMesh.Square(PixelWidth, PixelHeight));
        this.mesh.material.shaderProgram = ShaderProgram.DEFAULT_UI;
        this.mesh.material.SetTexture(inactive_texture);
        
        super.CreateDebugMesh();
    }
    
    @Override
    protected boolean IsMouseOver() {
        int X = Mouse.getMouseX();
        int Y = Mouse.getMouseY();
    
        Vector3d pos = GetRenderPosition();
    
        if(Owner_Canvas.Render_Space == RenderSpace.SCREEN_SPACE) {
            float radius = PixelWidth / 2f;
            Vector2d pos2D = new Vector2d(pos.x + radius, pos.y + radius);
            Vector2d mousePos2D = new Vector2d(X, Y);
            if(pos2D.distance(mousePos2D) > radius)
                return false;
            
        }else if(Owner_Canvas.Render_Space == RenderSpace.WORLD_SPACE){
            //TODO: Handle 3D(Ray Casting/Ray Picking)
        }
    
        return true;
    }
    
    @Override
    protected void OnUpdate() {
        if(IsGrabbed){
            switch(scrollBar.GetScrollDirection()){
                case Horizontal:
                    this.MovePixelOffset((float)Mouse.getDX(), 0, 0);
                    break;
                case Vertical:
                    this.MovePixelOffset(0, (float)Mouse.getDY(), 0);
                    break;
                default:
                    break;
            }
            scrollCallback.ScrollCallback(PixelOffset.x, PixelOffset.y);
        }
    }
    
    @Override
    protected void MouseOver() {
        this.mesh.material.SetTexture(active_texture);
    }
    
    @Override
    protected void MouseOverEnd() {
        this.mesh.material.SetTexture(inactive_texture);
    }
    
    @Override
    protected void MouseClick() {
        
    }
    
    @Override
    protected void MouseHeld() {
        
    }
    
    @Override
    protected void MouseUp() {
        IsGrabbed = false;
    }
    
    @Override
    protected void MouseDown() {
        IsGrabbed = true;
    }
    
    public void SetScrollCallback(IScrollCallback scrollCallback){
        this.scrollCallback = scrollCallback;
    }
    
    public interface IScrollCallback{
        void ScrollCallback(float x, float y);
    }
    
    @Override
    public void OnMouseUp() {
        MouseUp();
    }
}
