package main.java.org.bearengine.ui;

import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 11/06/2016.
 */
public abstract class UIObject {

    public String Name = "UIObject";

    public enum Anchor{ TL, TR, BL, BR }
    protected Anchor PositionAnchor = Anchor.TL;

    protected int PosX, PosY, PosZ;
    protected int Width, Height;

    protected UIObject Parent;
    protected List<UIObject> Children;

    protected Mesh mesh;

    protected final Matrix4d m_transform;
    protected boolean transformMatrixDirty = true;

    public UIObject(){
        this(0, 0, 0, 0);
    }

    public UIObject(int x, int y, int width, int height){
        this.PosX = x;
        this.PosY = y;
        this.Width = width;
        this.Height = height;
        this.m_transform = new Matrix4d();
        this.Children = new ArrayList<>();
    }

    public void SetPosition(int x, int y, int z){
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
            Children.add(child);
        }
    }

    public void RemoveChild(UIObject child){
        if(Children.contains(child)) {
            Children.remove(child);
            child.Parent = null;
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
        }
        return m_transform;
    }

    private void UpdateTransformMatrix(){
        int x = Parent.PosX + PosX;
        int y = Parent.PosY + PosY;

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
        }

        m_transform.identity();
        m_transform.translate(x, y, PosZ);

        transformMatrixDirty = false;
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
