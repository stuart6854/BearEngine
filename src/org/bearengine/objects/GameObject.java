package org.bearengine.objects;

import org.bearengine.graphics.types.Mesh;

/**
 * Created by Stuart on 28/05/2016.
 */
public class GameObject extends Object {

    public String Name = "GameObject";

    private Mesh mesh;

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}
