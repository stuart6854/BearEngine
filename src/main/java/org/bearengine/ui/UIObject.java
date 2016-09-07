package main.java.org.bearengine.ui;

import com.sun.javafx.geom.Vec3d;
import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.debug.DebugMesh;
import main.java.org.bearengine.graphics.rendering.RenderSpace;
import main.java.org.bearengine.graphics.rendering.Renderer;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.input.IMouseCallbacks;
import main.java.org.bearengine.input.Mouse;
import main.java.org.bearengine.objects.Object;
import main.java.org.joml.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 11/06/2016.
 */
public abstract class UIObject extends Object implements IMouseCallbacks {

    public boolean IsActive = true;
    public boolean IsVisible = true;
    protected boolean ShowDebugMesh = false;

    protected Vector2f NormalisedPosition; // Relative to Parent, eg. [0,0] is Top-Left of Parent Element, [0.5, 0.5] is Centre of Parent
    protected Vector3f PixelOffset; // Adjusts position relative to normalised position
    protected float PixelWidth, PixelHeight;

    protected Canvas Owner_Canvas;
    protected UIObject Parent;
    protected List<UIObject> Children;

    protected Mesh mesh;
    protected DebugMesh debugMesh;

    private boolean MouseOver = false;
    private int MouseStateLastFrame = 0; // 0 - Normal, 1 - Down, 2 - Up, 3 - Held
    
    public UIObject(){
        this(null);
    }

    public UIObject(UIObject parent){
        super();
        super.Name = "UI_Object";
        
        this.NormalisedPosition = new Vector2f();
        this.PixelOffset = new Vector3f();
        this.Children = new ArrayList<>();
        this.debugMesh = new DebugMesh();
        this.SetParent(parent);
        
        CreateDebugMesh();
    }

    protected void update(){
        OnUpdate();

        if(IsMouseOver() && !IsMouseOverChild() && MouseOver == false){
            MouseOver = true;
            MouseOver();
        }else if(MouseOver == true && !IsMouseOver()){
            MouseOver = false;
            MouseOverEnd();
        }

        for(UIObject child : Children) {
            if(child.IsActive) child.update();
        }
    }

    public void SetPixelOffset(Vector3f pos){
        SetPixelOffset((float)pos.x, (float)pos.y, (float)pos.z);
    }
    
    public void SetPixelOffset(float x, float y, float z){
        this.PixelOffset.set(x, y, z);
        UpdateTransformMatrix();
    }
    
    public void MovePixelOffset(float x, float y, float z){
        this.PixelOffset.add(x, y, z);
        UpdateTransformMatrix();
    }

    public void SetNormalisedPosition(float x, float y){
        this.NormalisedPosition.set(x, y);
        UpdateTransformMatrix();
    }

    public void SetWidth(float width){
        this.PixelWidth = width;
        BuildMesh();
        UpdateTransformMatrix();
    }

    public void SetHeight(float height){
        this.PixelHeight = height;
        BuildMesh();
        UpdateTransformMatrix();
    }
    
    public Vector3f GetPixelOffset() {
        return PixelOffset;
    }
    
    public float GetPixelWidth() {
        return PixelWidth;
    }
    
    public float GetPixelHeight() {
        return PixelHeight;
    }
    
    public void SetParent(UIObject parent){
        if(parent == null) return;
        
        this.Parent = parent;
        parent.AddChild(this);
        this.Owner_Canvas = parent.Owner_Canvas;
        this.debugMesh.SetRenderSpace(this.Owner_Canvas.Render_Space);
        this.UpdateTransformMatrix();
        
        if(parent != null)
            Mouse.RegisterMouseListner(this);
    }
    
    protected void AddChild(UIObject child){
        if(!Children.contains(child)) {
            Children.add(child);
        }
    }

    public void RemoveChild(UIObject child){
        if(Children.contains(child)) {
            Children.remove(child);
            child.Parent = null;
            child.Owner_Canvas = null;
        }
    }

    public List<UIObject> GetChildren(){
        return Children;
    }

    public void BuildMesh(){
        CreateDebugMesh();
    }

    protected void CreateDebugMesh() {
        debugMesh.Mesh_Name = this.Name + "_DebugMesh";
        float[] vertices = { 0, 0, 0, 0, PixelHeight, 0, PixelWidth, PixelHeight, 0, PixelWidth, 0, 0 };
        int[] indices = { 0, 1, 1, 2, 2, 3, 3, 0 };
//        int[] indices = { 0, 1, 2, 2, 3, 0 };

        debugMesh.SetVertices(vertices);
        debugMesh.SetIndices(indices);
        debugMesh.CreateRenderModel(this);
    }

    public void setMesh(Mesh mesh){
        if(this.mesh != null)
            this.mesh.Cleanup();

        this.mesh = mesh;
    }

    public Mesh GetMesh(){
        return mesh;
    }

    public Matrix4d GetTransformMatrix(){
        return m_transform;
    }

    public void SetShowDebugMesh(boolean show){
        ShowDebugMesh = show;
        if(show)
            Renderer.RegisterDebugMesh(this.debugMesh);
        else
            Renderer.UnregisterDebugMesh(this.debugMesh);
    }

    protected void UpdateTransformMatrix(){
        UpdateTransformMatrix(GetRenderPosition(), new Quaterniond(), new Vector3d());
    }

    @Override
    protected void UpdateTransformMatrix(Vector3d pos, Quaterniond rot, Vector3d scale) {
        m_transform.identity();
        m_transform.translate(pos.x, pos.y, pos.z);

        for(UIObject object : Children)
            object.UpdateTransformMatrix();
    }

    protected boolean IsMouseOver(){
        int X = Mouse.getMouseX();
        int Y = Mouse.getMouseY();

        Vector3d pos = GetRenderPosition();

        if(Owner_Canvas.Render_Space == RenderSpace.SCREEN_SPACE) {
            if(X < pos.x || X > pos.x + PixelWidth)
                return false;
            if(Y < pos.y || Y > pos.y + PixelHeight)
                return false;
        }else if(Owner_Canvas.Render_Space == RenderSpace.WORLD_SPACE){
            //TODO: Handle 3D(Ray Casting/Ray Picking)
        }
        
        return true;
    }
    
    protected boolean IsMouseOverChild(){
        for(UIObject child : Children)
            if(child.IsMouseOver())
                return true;
        
        return false;
    }
    
    protected Vector3d GetRenderPosition(){
        Vector3d space = new Vector3d();
        Vector2d point = new Vector2d();

        space.add(PixelOffset);
        if(this.Parent != null){

            if(Owner_Canvas.Render_Space == RenderSpace.SCREEN_SPACE) {
                point.set(Parent.PixelWidth * NormalisedPosition.x, Parent.PixelHeight * NormalisedPosition.y);
            } else if(Owner_Canvas.Render_Space == RenderSpace.WORLD_SPACE) {
                float flippedNormY = 1 - NormalisedPosition.y;
                point.set(NormalisedPosition.x / Parent.PixelWidth, Parent.PixelHeight / flippedNormY);
                point.y -= PixelHeight;
            }

            space.add(Parent.GetRenderPosition());
        }

        space.add(new Vector3d(point, 0));

        return space;
    }

    protected abstract void OnUpdate(); //Allows controls to have custom update logic while still calling this classes update method
    protected abstract void MouseOver();
    protected abstract void MouseOverEnd();
    protected abstract void MouseClick();
    protected abstract void MouseHeld();
    protected abstract void MouseDown();
    protected abstract void MouseUp();

    @Override
    public boolean equals(java.lang.Object obj) {
        UIObject uiObject = (UIObject) obj;

        if(uiObject.Name != this.Name) return false;

        if(uiObject.NormalisedPosition != this.NormalisedPosition) return false;
        if(uiObject.PixelOffset != this.PixelOffset) return false;

        if(uiObject.PixelWidth != this.PixelWidth) return false;
        if(uiObject.PixelHeight != this.PixelHeight) return false;

        return true;
    }
    
    @Override
    public void OnMouseClick() {
        if(IsMouseOver() && !IsMouseOverChild()) {
            this.MouseClick();
        }
    }
    
    @Override
    public void OnMouseHeld() {
        if(IsMouseOver() && !IsMouseOverChild())
            this.MouseHeld();
    }
    
    @Override
    public void OnMouseDown() {
        if(IsMouseOver() && !IsMouseOverChild())
            this.MouseDown();
    }
    
    @Override
    public void OnMouseUp() {
        if(IsMouseOver() && !IsMouseOverChild())
            this.MouseUp();
    }
}
