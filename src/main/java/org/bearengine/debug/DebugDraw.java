package main.java.org.bearengine.debug;

import main.java.org.bearengine.graphics.importers.OBJImporter;
import main.java.org.bearengine.graphics.rendering.RenderSpace;
import main.java.org.bearengine.graphics.rendering.Renderer;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.graphics.types.Transform;
import main.java.org.bearengine.objects.Camera;
import main.java.org.joml.Quaterniond;
import main.java.org.joml.Quaternionf;
import main.java.org.joml.Vector3d;
import main.java.org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import javax.swing.text.Position;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 06/07/2016.
 */
public class DebugDraw {

//    public List<Float> Vertices = new ArrayList<>();

    private static ArrayList<DebugMesh> DEBUG_MESHES = new ArrayList<>();

    private static DebugMesh Sphere_Mesh;

    public static void SetupDebugDraw(){
        Mesh mesh = new OBJImporter().LoadMesh("/main/java/resources/models/sphere.obj");

        Sphere_Mesh = new DebugMesh();
        Sphere_Mesh.SetIndices(mesh.GetIndices());
        Sphere_Mesh.SetVertices(mesh.GetVertices());

        mesh.Cleanup();
    }

    public static void BeginFrame(){
        for(DebugMesh mesh : DEBUG_MESHES){
            mesh.Cleanup();
        }
        DEBUG_MESHES.clear();
    }

    public static void EndFrame(){
        Renderer.RenderDebugMeshes(DEBUG_MESHES);
    }

//    public void Render(){
//        float[] verts = new float[Vertices.size()];
//        for(int i = 0; i < Vertices.size(); i++) verts[i] = Vertices.get(i);
//
//        FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(Vertices.size());
//        vertBuffer.put(verts);
//        vertBuffer.flip();
//
//        int[] indices = { 0, 1 };
//
//        IntBuffer indiceBuffer = BufferUtils.createIntBuffer(indices.length);
//        indiceBuffer.put(indices);
//        indiceBuffer.flip();
//
//
//
//    }

    public static void DrawCube(float Size, Vector3d position, Vector3f rotation){
        DebugMesh mesh = new DebugMesh();
        mesh = CreateCubeMesh(mesh, (Size / 2f));

        mesh.material.shaderProgram = ShaderProgram.DEBUG_MESH_SHADER_PROGRAM;

        Transform t = new Transform(position, rotation, new Vector3d(1, 1, 1));
        mesh.CreateRenderModel(t);

        mesh.SetRenderSpace(RenderSpace.WORLD_SPACE);

        DEBUG_MESHES.add(mesh);
    }

    public static void DrawSphere(float size, Vector3d position, Vector3f rotation){
        //
        DebugMesh mesh = new DebugMesh();
        mesh.SetIndices(Sphere_Mesh.GetIndices());
        mesh.SetVertices(Sphere_Mesh.GetVertices());

        mesh.material.shaderProgram = ShaderProgram.DEBUG_MESH_SHADER_PROGRAM;

        Transform t = new Transform(position, rotation, new Vector3d(size, size, size));
        mesh.CreateRenderModel(t);

        mesh.SetRenderSpace(RenderSpace.WORLD_SPACE);

        DEBUG_MESHES.add(mesh);
    }

    private static DebugMesh CreateCubeMesh(DebugMesh mesh, float HALF_SIZE){
        float[] vertices = {
                -HALF_SIZE,  HALF_SIZE, -HALF_SIZE,//North
                -HALF_SIZE, -HALF_SIZE, -HALF_SIZE,
                HALF_SIZE, -HALF_SIZE, -HALF_SIZE,
                HALF_SIZE, -HALF_SIZE, -HALF_SIZE,
                HALF_SIZE,  HALF_SIZE, -HALF_SIZE,
                -HALF_SIZE,  HALF_SIZE, -HALF_SIZE,
                //
                -HALF_SIZE, -HALF_SIZE,  HALF_SIZE,//west
                -HALF_SIZE, -HALF_SIZE, -HALF_SIZE,
                -HALF_SIZE,  HALF_SIZE, -HALF_SIZE,
                -HALF_SIZE,  HALF_SIZE, -HALF_SIZE,
                -HALF_SIZE,  HALF_SIZE,  HALF_SIZE,
                -HALF_SIZE, -HALF_SIZE,  HALF_SIZE,
                //
                HALF_SIZE, -HALF_SIZE, -HALF_SIZE,//east
                HALF_SIZE, -HALF_SIZE,  HALF_SIZE,
                HALF_SIZE,  HALF_SIZE,  HALF_SIZE,
                HALF_SIZE,  HALF_SIZE,  HALF_SIZE,
                HALF_SIZE,  HALF_SIZE, -HALF_SIZE,
                HALF_SIZE, -HALF_SIZE, -HALF_SIZE,
                //
                -HALF_SIZE, -HALF_SIZE,  HALF_SIZE,//south
                -HALF_SIZE,  HALF_SIZE,  HALF_SIZE,
                HALF_SIZE,  HALF_SIZE,  HALF_SIZE,
                HALF_SIZE,  HALF_SIZE,  HALF_SIZE,
                HALF_SIZE, -HALF_SIZE,  HALF_SIZE,
                -HALF_SIZE, -HALF_SIZE,  HALF_SIZE,
                //
                -HALF_SIZE,  HALF_SIZE, -HALF_SIZE,//Up
                HALF_SIZE,  HALF_SIZE, -HALF_SIZE,
                HALF_SIZE,  HALF_SIZE,  HALF_SIZE,
                HALF_SIZE,  HALF_SIZE,  HALF_SIZE,
                -HALF_SIZE,  HALF_SIZE,  HALF_SIZE,
                -HALF_SIZE,  HALF_SIZE, -HALF_SIZE,
                //
                -HALF_SIZE, -HALF_SIZE, -HALF_SIZE,//Down
                -HALF_SIZE, -HALF_SIZE,  HALF_SIZE,
                HALF_SIZE, -HALF_SIZE, -HALF_SIZE,
                HALF_SIZE, -HALF_SIZE, -HALF_SIZE,
                -HALF_SIZE, -HALF_SIZE,  HALF_SIZE,
                HALF_SIZE, -HALF_SIZE,  HALF_SIZE
        };

        int[] indices = {
                0, 1, 2,
                3, 4, 5,
                6, 7, 8,
                9, 10, 11,
                12, 13, 14,
                15, 16, 17,
                18, 19, 20,
                21, 22, 23,
                24, 25, 26,
                27, 28, 29,
                30, 31, 32,
                33, 34, 35
        };

        mesh.SetIndices(indices);
        mesh.SetVertices(vertices);

        return mesh;
    }

}
