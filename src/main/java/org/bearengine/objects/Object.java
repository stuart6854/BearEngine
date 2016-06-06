package main.java.org.bearengine.objects;

import main.java.org.joml.Matrix4d;
import main.java.org.joml.Quaterniond;
import main.java.org.joml.Vector3d;
import main.java.org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Object {

    public String Name = "UN-NAMED";

    public static List<Object> Objects = new ArrayList<>();

    protected Vector3d Position;
    protected Quaterniond Rotation;
    protected Vector3d Scale;

    protected final Matrix4d m_transformation;
    protected boolean transformIsDirty = true;

    public Object(){
        this.Position = new Vector3d(0, 0, 0);
        this.Rotation = new Quaterniond();
        this.Scale = new Vector3d(1, 1, 1);

        this.m_transformation = new Matrix4d();

        Objects.add(this);
    }

    public void setPosition(Vector3d position){
        SetPosition(position.x, position.y, position.z);
    }

    public void SetPosition(double x, double y, double z){
        Position.set(x, y, z);
        transformIsDirty = true;
    }

    public void MovePosition(Vector3f translation){
        this.MovePosition(translation.x, translation.y, translation.z);
    }

    public void MovePosition(float x, float y, float z){
        Position.add(x, y, z);
        transformIsDirty = true;
    }

    public void SetRotation(float x, float y, float z){
        Rotation.set(x, y, z);
        transformIsDirty = true;
    }

    public void Rotate(Vector3f rotation){
        this.Rotate(rotation.x, rotation.y, rotation.z);
    }

    public void Rotate(float x, float y, float z){
        Rotation.rotate(x, y, z);
        transformIsDirty = true;
    }

    public void SetScale(float x, float y, float z){
        Scale.set(x, y, z);
        transformIsDirty = true;
    }

    public Matrix4d GetTransformMatrix(){
        if(transformIsDirty){
            UpdateTransformMatrix();
        }
        return m_transformation;
    }

    private void UpdateTransformMatrix(){
        m_transformation.identity().translate(Position).rotate(Rotation).scale(Scale);
        transformIsDirty = false;
    }

    public Vector3d getPosition() {
        return Position;
    }

    public Quaterniond getRotation() {
        return Rotation;
    }

    public Vector3d getScale() {
        return Scale;
    }

    public void Destroy(){
        Objects.remove(this);
        Position = null;
        Rotation = null;
        Scale = null;
        m_transformation.zero();
    }

}
