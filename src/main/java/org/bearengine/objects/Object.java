package main.java.org.bearengine.objects;

import main.java.org.bearengine.debug.Debug;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Quaterniond;
import main.java.org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Object {

    public static List<Object> Objects = new ArrayList<>();

    public String Name = "Object";

    protected final Matrix4d m_transform = new Matrix4d();

    public Object(){
        Objects.add(this);
    }

    public Matrix4d GetTransformMatrix(){
        return m_transform;
    }

    protected void UpdateTransformMatrix(Vector3d pos, Quaterniond rot, Vector3d scale){
        m_transform.identity().translate(pos).rotate(rot).scale(scale);
        Debug.log("");
    }

}
