package org.bearengine.objects;

import org.joml.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Object {

    public static List<Object> Objects = new ArrayList<>();

    public Vector3d Position;
    public Quaterniond Rotation;
    public Vector3d Scale;

    private Matrix4d m_transformation;
    private boolean transformIsDirty = true;

    public Object(){
        this.Position = new Vector3d();
        this.Rotation = new Quaterniond();
        this.Scale = new Vector3d();

        Objects.add(this);
    }

    public void SetPosition(float x, float y, float z){
        Position.set(x, y, z);
    }

    public void TranslatePosition(Vector3f translation){
        this.TranslatePosition(translation.x, translation.y, translation.z);
    }

    public void TranslatePosition(float x, float y, float z){
        Position.add(x, y, z);
    }

    public void SetRotation(float x, float y, float z){
        Rotation.set(x, y, z);
    }

    public void Rotate(Vector3f rotation){
        Rotation.rotate(rotation.x, rotation.y, rotation.z);
    }

    public void Rotate(float x, float y, float z){
        Rotation.rotate(x, y, z);
    }

    public void SetScale(float x, float y, float z){
        Scale.set(x, y, z);
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

    public void Destroy(){
        Objects.remove(this);
        Position = null;
        Rotation = null;
        Scale = null;
        m_transformation = null;
    }

}
