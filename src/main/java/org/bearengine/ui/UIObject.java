package main.java.org.bearengine.ui;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.input.Mouse;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Vector2f;
import main.java.org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 11/06/2016.
 */
public abstract class UIObject {

    public String Name = "UIObject";

    public boolean IsActive = true;
    public boolean IsVisible = true;

    public enum Anchor{ TL, TR, CL, CENTRE, CR, BL, BR }
    protected Anchor PositionAnchor = Anchor.TL;

    protected float PosX, PosY, PosZ;
    protected float Width, Height;

    protected Canvas ParentCanvas;
    protected UIObject Parent;
    protected List<UIObject> Children;

    protected Mesh mesh;

    protected final Matrix4d m_transform;
    protected boolean transformMatrixDirty = true;

    public UIObject(){
        this(0, 0, 0, 0);
    }

    public UIObject(float x, float y, float width, float height){
        this.PosX = x;
        this.PosY = y;
        this.Width = width;
        this.Height = height;
        this.m_transform = new Matrix4d();
        this.Children = new ArrayList<>();
    }

    protected void update(){
        for(UIObject child : Children) {
            if(child.IsActive) child.update();
        }
    }

    public void SetPosition(float x, float y, float z){
        this.PosX = x;
        this.PosY = y;
        this.PosZ = z;
        transformMatrixDirty = true;
    }

    public void SetAnchor(Anchor anchor){
        this.PositionAnchor = anchor;
    }

    public Vector3f GetPosition(){
        return new Vector3f(PosX, PosY, PosZ);
    }

    public void AddChild(UIObject child){
        if(!Children.contains(child)) {
            child.Parent = this;
            child.ParentCanvas = this.ParentCanvas;
            Children.add(child);
        }
    }

    public void RemoveChild(UIObject child){
        if(Children.contains(child)) {
            Children.remove(child);
            child.Parent = null;
            child.ParentCanvas = null;
        }
    }

    public List<UIObject> GetChildren(){
        return Children;
    }

    public abstract void BuildMesh();

    public void setMesh(Mesh mesh){
        if(this.mesh != null)
            this.mesh.Cleanup();

        this.mesh = mesh;
    }

    public Mesh GetMesh(){
        return mesh;
    }

    public Matrix4d GetTransformMatrix(){
        if(transformMatrixDirty){
            UpdateTransformMatrix();
            for(UIObject object : Children)
                object.transformMatrixDirty = true;
        }
        return m_transform;
    }

    protected void UpdateTransformMatrix(){
        Vector3f pos = GetRenderPosition();

        m_transform.identity();
        m_transform.translate(pos.x, pos.y, pos.z);

        transformMatrixDirty = false;
    }

    protected boolean IsMouseOver(){
        int X = Mouse.getMouseX();
        int Y = Mouse.getMouseY();

        Vector3f pos = GetRenderPosition();

        if(ParentCanvas.renderMode == Canvas.RenderMode.ScreenSpace) {
            if(X < pos.x || X > pos.x + Width)
                return false;
            if(Y < pos.y || Y > pos.y + Height)
                return false;
        }else{
            //TODO: Handle 3D(Ray Casting/Ray Picking)
        }

        return true;
    }

    protected Vector3f GetRenderPosition(){
        if(this instanceof Canvas) return new Vector3f(PosX, PosY, PosZ); //Because Canvas's dont have parents

        Vector3f parentRenderPos = Parent.GetRenderPosition();
        float x = parentRenderPos.x + PosX;
        float y = parentRenderPos.y + PosY;
        float z = parentRenderPos.z + PosZ;

        switch(PositionAnchor) {
            case TL:
                break;
            case TR:
                x = Parent.Width - x - Width;
                break;
            case BL:
                y = Parent.Height - y - Height;
                break;
            case BR:
                x = Parent.Width - x - Width;
                y = Parent.Height - y - Height;
                break;
            case CL:
                y += (Parent.Height / 2) - (this.Height / 2);
                break;
            case CENTRE:
                x += (Parent.Width / 2) - (this.Width / 2);
                y += (Parent.Height / 2) - (this.Height / 2);
                break;
        }

        return new Vector3f(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        UIObject uiObject = (UIObject) obj;

        if(uiObject.Name != this.Name) return false;

        if(uiObject.PosX != this.PosX) return false;
        if(uiObject.PosY != this.PosY) return false;
        if(uiObject.PosZ != this.PosZ) return false;

        if(uiObject.Width != this.Width) return false;
        if(uiObject.Height != this.Height) return false;

        return true;
    }

}
