package main.java.org.bearengine.objects;

import main.java.org.bearengine.graphics.rendering.Renderer;
import main.java.org.bearengine.graphics.types.Mesh;

import java.lang.*;

/**
 * Created by Stuart on 28/05/2016.
 */
public class GameObject extends Object {

    public String Name = "GameObject";

    private Mesh mesh;

    public GameObject(){
        super();
    }

    private void RegisterRender(){
        Renderer.RegisterGameObject(this);
    }

    private void UnRegisterRender(){
        Renderer.UnRegisterGameObject(this);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        UnRegisterRender();
        this.mesh = mesh;
        RegisterRender();
    }

}
