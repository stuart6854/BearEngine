package voxelgame.data;

import java.util.ArrayList;
import java.util.List;

import main.java.org.joml.Vector3f;

public class MeshData {

	public List<Float> vertices = new ArrayList<Float>();
    public List<Integer> indices = new ArrayList<Integer>();
    public List<Float> uv = new ArrayList<Float>();
    public List<Float> normals = new ArrayList<Float>();
    public List<Float> vertexColors = new ArrayList<Float>();

    public List<Float> colVertices = new ArrayList<Float>();
    public List<Integer> colTriangles = new ArrayList<Integer>();

    public boolean useRenderDataForCol;

    public MeshData() {

    }

    public void addQuadTriangles() {
        indices.add(vertices.size() / 3 - 4 + 0);
        indices.add(vertices.size() / 3 - 4 + 1);
        indices.add(vertices.size() / 3 - 4 + 2);
        indices.add(vertices.size() / 3 - 4 + 2);
        indices.add(vertices.size() / 3 - 4 + 3);
        indices.add(vertices.size() / 3 - 4 + 0);

        if (useRenderDataForCol) {
            colTriangles.add(colVertices.size() - 4 * 3);
            colTriangles.add(colVertices.size() - 3 * 3);
            colTriangles.add(colVertices.size() - 2 * 3);
            colTriangles.add(colVertices.size() - 4 * 3);
            colTriangles.add(colVertices.size() - 2 * 3);
            colTriangles.add(colVertices.size() - 1 * 3);
        }
    }

    public void addVertex(Vector3f vertex, int lightLevel) {
        vertices.add(vertex.x);
        vertices.add(vertex.y);
        vertices.add(vertex.z);

        float exponentialLightPercentage = (float)Math.pow((1 - 0.2f), 15 - lightLevel);//Light decreases by 20% for each decrease in light level

        vertexColors.add(exponentialLightPercentage);
        vertexColors.add(exponentialLightPercentage);
        vertexColors.add(exponentialLightPercentage);
        vertexColors.add(1.0f);

        if (useRenderDataForCol) {
            colVertices.add(vertex.x);
            colVertices.add(vertex.y);
            colVertices.add(vertex.z);
        }
    }

    public void addTriangle(int tri) {
        indices.add(tri);

        if (useRenderDataForCol) {
            colTriangles.add(tri - (vertices.size() - colVertices.size()));
        }
    }

}
