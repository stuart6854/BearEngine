package main.java.org.bearengine.graphics.types;

import main.java.org.joml.*;

/**
 * Created by Stuart on 29/05/2016.
 *
 * Holds Position, Rotation and Scale of Objects using a Certain Mesh
 */
public class Transform {

    public final Vector3d Position;
    public final Vector3f Rotation;
    public final Vector3d Scale;

    public Transform(Vector3d pos, Vector3f rot, Vector3d scale){
        this.Position = pos;
        this.Rotation = rot;
        this.Scale = scale;
    }

    public Matrix4d GenerateMatrix(){
        Matrix4d matrix = new Matrix4d();
        matrix.translate(Position);
        matrix.rotateXYZ(Math.toRadians(Rotation.x), Math.toRadians(Rotation.y), Math.toRadians(Rotation.z));
        matrix.scale(Scale);
        return matrix;
    }

}
