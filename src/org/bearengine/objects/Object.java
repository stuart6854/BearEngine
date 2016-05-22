package org.bearengine.objects;

import javafx.geometry.Pos;
import org.bearengine.math.types.Matrix4;
import org.bearengine.math.types.Quaternion;
import org.bearengine.math.types.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Object {

    public static List<Object> Objects = new ArrayList<>();

    public Vector3 Position;
    public Quaternion Rotation;
    public Vector3 Scale;

    private Matrix4 transform;
    private boolean transformIsDirty = true;

    public Object(){
        this.Position = new Vector3();
        this.Rotation = new Quaternion();
        this.Scale = new Vector3();

        Objects.add(this);
    }

    public Matrix4 GetTransform(){
        if(transformIsDirty){
            UpdateTransform();
        }
        return transform;
    }

    private void UpdateTransform(){
        transform = Matrix4.translate(Position.x, Position.y, Position.z)
                    .multiply(Matrix4.rotation(Rotation))
                    .multiply(Matrix4.scale(Scale.x, Scale.y, Scale.z));

        transformIsDirty = false;
    }

    public void Destroy(){
        Objects.remove(this);
        Position = null;
        Rotation = null;
        Scale = null;
        transform = null;
    }

}
