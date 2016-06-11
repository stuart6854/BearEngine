package main.java.org.bearengine.objects;

import main.java.org.bearengine.graphics.rendering.Renderer;
import main.java.org.bearengine.graphics.types.Mesh;

/**
 * Created by Stuart on 28/05/2016.
 */
public class GameObject extends Object {

    private Mesh mesh;

    public GameObject(){
        super();
        this.Name = "GameObject";
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

    public void setMesh(Mesh mesh) {
        UnRegisterRender();
        if(this.mesh != null)
            this.mesh.Cleanup();

        this.mesh = mesh;
        RegisterRender();
    }

}
