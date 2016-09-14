package main.java.org.bearengine.objects;

import main.java.org.bearengine.debug.Debug;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Matrix4f;
import main.java.org.joml.Vector3f;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Camera extends GameObject {

    public static Camera Main_Camera;

    protected Vector3f Rotation;

    protected final Matrix4d m_projection;
    protected final Matrix4d m_view;

    protected boolean viewIsDirty = true;

    public Camera(Matrix4f projection){
        super();
        super.Name = "Camera";

        this.Rotation = new Vector3f();

        this.m_projection = new Matrix4d(projection);
        this.m_view = new Matrix4d();

        if(Main_Camera == null) SetAsMain();
    }

    public void SetAsMain(){
        Main_Camera = this;

        Debug.log("Camera -> Main Camera Set.");
    }

    @Override
    public void SetPosition(double x, double y, double z){
        if(DevCamera.ENABLED && DevCamera.DEV_CAMERA != this) return;
        Position.set(x, y, z);
        viewIsDirty = true;
    }

    @Override
    public void MovePosition(float x, float y, float z){
        if(DevCamera.ENABLED && DevCamera.DEV_CAMERA != this) return;
        Position.add(x, y, z);
        viewIsDirty = true;
    }

    @Override
    public void SetRotation(float x, float y, float z){
        if(DevCamera.ENABLED && DevCamera.DEV_CAMERA != this) return;
        Rotation.set(x, y, z);
        ClampRotation();
        viewIsDirty = true;
    }

    @Override
    public void Rotate(float x, float y, float z){
        if(DevCamera.ENABLED && DevCamera.DEV_CAMERA != this) return;
//        Debug.log("Before: " + Rotation);
        Rotation.add(x, y, z);
        ClampRotation();
//        Debug.log("After: " + Rotation);
        viewIsDirty = true;
    }

    @Override
    public void SetScale(float x, float y, float z){
        if(DevCamera.ENABLED && DevCamera.DEV_CAMERA != this) return;
        Scale.set(x, y, z);
        viewIsDirty = true;
    }

    public Matrix4d GetProjection(){
        return m_projection;
    }

    public void SetProjection(Matrix4f projection){
        this.m_projection.set(projection);

        Debug.log("Camera -> Projection Set.");
    }

    public Matrix4d GetViewMatrix(){
        if(viewIsDirty) {
            UpdateViewMatrix();
        }
        return m_view;
    }

    protected void UpdateViewMatrix() {
        m_view.identity();
        m_view.rotate((float)Math.toRadians(Rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(Rotation.y), new Vector3f(0, 1, 0));
        m_view.translate(-Position.x, -Position.y, -Position.z);
        viewIsDirty = false;
    }

    public Vector3f GetEulerRotation(){
        return Rotation;
    }
    
    private void ClampRotation(){
        if(Rotation.x < 0) Rotation.x = 360;
        if(Rotation.x > 360) Rotation.x = 0;
        if(Rotation.y < 0) Rotation.y = 360;
        if(Rotation.y > 360) Rotation.y = 0;
        if(Rotation.z < 0) Rotation.z = 360;
        if(Rotation.z > 360) Rotation.z = 0;
    }
    
    public void LookAt(Vector3f point){
        //TODO: Make sure this works
        Vector3f dir = new Vector3f((float)Position.x, (float)Position.y, (float)Position.z).sub(point).normalize();
        SetRotation(dir.x, dir.y, 0);//No Roll
    }
    
}
