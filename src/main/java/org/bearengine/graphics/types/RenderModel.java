package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.objects.Object;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Stuart on 29/05/2016.
 */
public class RenderModel {

    public static List<RenderModel> Models = new ArrayList<>();

    private int VAO_ID;
    private int INDEX_VBO_ID;
    private int[] VBO_IDs;

    private ArrayList<Object> Transforms;

    public RenderModel(int vao_id, int index_vbo_id, int[] vbos){
        this.VAO_ID = vao_id;
        this.INDEX_VBO_ID = index_vbo_id;
        this.VBO_IDs = vbos;
        this.Transforms = new ArrayList<>();
    }

    public void AddTransform(Object object){
        this.Transforms.add(object);
    }

    public void RemoveTransform(Object object){
        this.Transforms.remove(object);
    }

    public ArrayList<Object> GetTransforms() {
        return Transforms;
    }

    public void PrepareRender(){
        glBindVertexArray(VAO_ID);

        for(int vbo : VBO_IDs) {
            glEnableVertexAttribArray(vbo);
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, INDEX_VBO_ID);
    }

    public void ReleaseModel(){
        glDeleteVertexArrays(VAO_ID);
        for(int vbo : VBO_IDs)
            glDeleteBuffers(vbo);

        glDeleteBuffers(INDEX_VBO_ID);
    }

    ///RENDER-MODEL FACTORY///

    private static boolean CREATING = false;

    private static int Current_Model = -1;
    private static int Current_VBO_Index = -1;
    private static int[] Current_VBOs = null;
    private static int Current_Index_VBO = -1;


    public static RenderModel Create2DModel(float[] vertices, float[] uvs, int[] indices){
        if(CREATING){
            Debug.error("RenderModel -> Already Creating RenderModel!");
            return null;
        }

        CreateModel(2);
        SetupAttributeBuffer(0, 3, vertices);
        SetupAttributeBuffer(1, 2, uvs);
        SetupIndexBuffer(indices);
        FinaliseModel();
        return StoreModel();
    }

    public static RenderModel Create3DModel(float[] vertices, float[] uvs, float[] normals, int[] indices){
        if(CREATING){
            Debug.error("RenderModel -> Already Creating RenderModel!");
            return null;
        }

        CreateModel(3);
        SetupAttributeBuffer(0, 3, vertices);
        SetupAttributeBuffer(1, 2, uvs);
        SetupAttributeBuffer(2, 3, normals);
        SetupIndexBuffer(indices);
        FinaliseModel();
        return StoreModel();
    }

    private static void CreateModel(int vbos){
        CREATING = true;
        Current_Model = glGenVertexArrays();
        Current_VBO_Index = -1;
        Current_VBOs = new int[vbos];
        for(int i = 0; i < vbos; i++) Current_VBOs[i] = -1;
        glBindVertexArray(Current_Model);
    }

    private static void SetupAttributeBuffer(int location, int components, float[] data){
        int vbo_id = glGenBuffers();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
        buffer.put(data);
        buffer.flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(location);
        glVertexAttribPointer(location, components, GL_FLOAT, false, 0, 0);

        Current_VBO_Index += 1;
        Current_VBOs[Current_VBO_Index] = vbo_id;
    }

    private static void SetupIndexBuffer(int[] indices){
        Current_Index_VBO = glGenBuffers();
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, Current_Index_VBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private static void FinaliseModel(){
        for(int vbo : Current_VBOs)
            glEnableVertexAttribArray(vbo);

        glEnableVertexAttribArray(Current_Index_VBO);

        glBindVertexArray(0);
    }

    private static RenderModel StoreModel(){
        RenderModel model = new RenderModel(Current_Model, Current_Index_VBO, Current_VBOs);
        CREATING = false;

        ResetStaticVars();
        return model;
    }

    private static void ResetStaticVars(){
        Current_Model = -1;
        Current_VBO_Index = -1;
        Current_VBOs = null;
    }

    private static void ReleaseModels(){
        for(RenderModel model : Models)
            model.ReleaseModel();
    }

}
