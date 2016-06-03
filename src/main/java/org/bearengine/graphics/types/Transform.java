package main.java.org.bearengine.graphics.types;

import main.java.org.joml.Matrix4d;
import main.java.org.joml.Vector3d;
import main.java.org.joml.Quaterniond;

/**
 * Created by Stuart on 29/05/2016.
 *
 * Holds Position, Rotation and Scale of Objects using a Certain Mesh
 */
public class Transform {

    public final Vector3d Position = new Vector3d();
    public final Quaterniond Rotation = new Quaterniond();
    public final Vector3d Scale = new Vector3d();

    public Matrix4d GenerateMatrix(){
        Matrix4d matrix = new Matrix4d();
        matrix.translate(Position);
        matrix.rotate(Rotation);
        matrix.scale(Scale);
        return matrix;
    }

}
