package main.java.org.bearengine.objects;

import main.java.org.bearengine.graphics.rendering.Renderer;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.joml.Vector3d;
import main.java.org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 28/05/2016.
 */
public class GameObject extends Object {

    public String Name = "Object";

    public static List<GameObject> GameObjects = new ArrayList<>();

    protected Vector3d Position;
    protected Vector3f Rotation;
    protected Vector3d Scale;

    private Mesh mesh;

    public GameObject(){
        super();
        this.Name = "GameObject";

        this.Position = new Vector3d(0, 0, 0);
        this.Rotation = new Vector3f(0, 0, 0);
        this.Scale = new Vector3d(1, 1, 1);
        
        this.mesh = new Mesh();

        GameObjects.add(this);
    }

    public void SetPosition(Vector3d position){
        this.SetPosition(position.x, position.y, position.z);
    }

    public void SetPosition(double x, double y, double z){
        Position.set(x, y, z);
        UpdateTransformMatrix(Position, Rotation, Scale);
    }

    public void MovePosition(Vector3f translation){
        this.MovePosition(translation.x, translation.y, translation.z);
    }

    public void MovePosition(float x, float y, float z){
        Position.add(x, y, z);
        UpdateTransformMatrix(Position, Rotation, Scale);
    }

    public void SetRotation(float x, float y, float z){
        Rotation.set(x, y, z);
        UpdateTransformMatrix(Position, Rotation, Scale);
    }

    public void Rotate(Vector3f rotation){
        this.Rotate(rotation.x, rotation.y, rotation.z);
    }

    public void Rotate(float x, float y, float z){
        Rotation.add(x, y, z);
        UpdateTransformMatrix(Position, Rotation, Scale);
    }
    
    public void SetScale(float scale){
        this.SetScale(scale, scale, scale);
    }
    
    public void SetScale(float x, float y, float z){
        Scale.set(x, y, z);
        UpdateTransformMatrix(Position, Rotation, Scale);
    }

    public Vector3d GetPosition() {
        return Position;
    }

    public Vector3f GetRotation() {
        return Rotation;
    }

    public Vector3d GetScale() {
        return Scale;
    }
    
    public void Destroy(){
        GameObjects.remove(this);
        Position = null;
        Rotation = null;
        Scale = null;
        m_transform.zero();
    }

    private void RegisterRender(){
        Renderer.RegisterGameObject(this);
    }

    private void UnRegisterRender(){
        Renderer.UnregisterGameObject(this);
    }

    public Mesh GetMesh() {
        return mesh;
    }

    public void SetMesh(Mesh mesh) {
        UnRegisterRender();
        if(this.mesh != null)
            this.mesh.Cleanup();

        this.mesh = mesh;
        RegisterRender();
    }

    public static GameObject FindGameObject(String name){
        for(GameObject obj : GameObjects){
            if(obj.Name.equals(name))
                return obj;
        }

        return null;
    }

    @Override
    public String toString() {
        return Name + "( Pos: " + Position.toString() + " | Rot: " + Rotation.toString() + " | Scale: " + Scale.toString() + ")";
    }

}
