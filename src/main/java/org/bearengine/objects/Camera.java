package main.java.org.bearengine.objects;

import main.java.org.bearengine.debug.Debug;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Matrix4f;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Camera extends Object {

    public static Camera Main_Camera;

    private final Matrix4d m_projection;
    private final Matrix4d m_view;

    public Camera(Matrix4f projection){
        super();

        this.m_projection = new Matrix4d(projection);
        this.m_view = new Matrix4d();

        if(Main_Camera == null) Main_Camera = this;
    }

    public void SetAsMain(){
        Main_Camera = this;

        Debug.log("Camera -> Main Camera Set.");
    }

    public Matrix4d GetProjection(){
        return m_projection;
    }

    public void SetProjection(Matrix4f projection){
        this.m_projection.set(projection);

        Debug.log("Camera -> Projection Set.");
    }

    public Matrix4d GetViewMatrix(){
        UpdateViewMatrix();

        return m_view;
    }

    private void UpdateViewMatrix() {
        m_view.identity().translate(-Position.x, -Position.y, -Position.z).rotate(Rotation);
    }

}
