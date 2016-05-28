package org.bearengine.graphics.importers;

import org.bearengine.graphics.types.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 27/05/2016.
 */
public abstract class ModelImporter {
    
    protected List<Vector3f> vertices = new ArrayList<>();
    protected List<Vector2f> uvs = new ArrayList<>();
    protected List<Vector3f> normals = new ArrayList<>();
    protected List<Float> verticesOrdered = new ArrayList<>();
    protected List<Float> uvsOrdered = new ArrayList<>();
    protected List<Float> normalsOrdered = new ArrayList<>();

    public abstract Mesh LoadMesh(String path);

}
