package main.java.org.bearengine.graphics.rendering;

import main.java.org.bearengine.graphics.types.VertexAttribute;
import main.java.org.joml.Matrix4f;
import main.java.org.joml.Vector2f;
import main.java.org.joml.Vector3d;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.bearengine.objects.Camera;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Stuart on 20/05/2016.
 */
public class BatchRenderer {

    protected int VAO_ID;
    protected int VBO_ID;

    protected FloatBuffer Vertices;
    protected int NumOfVertices;
    protected boolean isDrawing;

    private int Batch_Size = 4096;
    private static int MAX_BATCH_SIZE = 1024 * 1024;

    protected final Camera camera;
    protected final ShaderProgram shaderProgram;

    public BatchRenderer(Camera camera, ShaderProgram shaderProgram){
        this.camera = camera;
        this.shaderProgram = shaderProgram;
    }

    public void Init(){
        //Generate Vertex Array Object
        VAO_ID = glGenVertexArrays();
        glBindVertexArray(VAO_ID);

        //Generate Vertex Buffer Object
        VBO_ID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);

        //Create FloatBuffer
        Vertices = BufferUtils.createFloatBuffer(Batch_Size * 12);

        //Upload null data to allocate storage for the VBO
        long size = Vertices.capacity() * Float.BYTES;
        glBufferData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);

        //Initialise variables
        NumOfVertices = 0;
        isDrawing = false;

        SpecifyVertexAttributes();
    }

    public void Begin(){
        NumOfVertices = 0;
        isDrawing = true;
    }

    public void End(){
        Flush();
        isDrawing = false;
    }

    private void Flush(){
        if(!isDrawing) return; //Cant render if we haven't drawn anything
        if(NumOfVertices == 0) return; // No point in rendering if we haven't got anything to render

        Vertices.flip();

        shaderProgram.Bind();

        shaderProgram.setUniform("projection", camera.GetProjection());
        shaderProgram.setUniform("view", camera.GetViewMatrix());
        shaderProgram.setUniform("model", new Matrix4f().translate(0, 0, 0));

        glBindVertexArray(VAO_ID);
        shaderProgram.EnableAttributes();

        //Upload new Vertex Data
        glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Vertices);

        //Draw batch
        glDrawArrays(GL_TRIANGLES, 0, NumOfVertices);

        //Clear Vertex data for next batch
        Vertices.clear();
        NumOfVertices = 0;
    }

    public void AddVertex(Vector3d pos){
        AddVertex(pos, Color.WHITE);
    }

    public void AddVertex(Vector3d pos, Color vertexColor){
        AddVertex(pos, vertexColor, new Vector2f());
    }

    public void AddVertex(Vector3d pos, Vector2f textureCoords){
        AddVertex(pos, Color.WHITE, textureCoords, new Vector3d());
    }

    public void AddVertex(Vector3d pos, Color vertexColor, Vector2f textureCoords){
        AddVertex(pos, vertexColor, textureCoords, new Vector3d());
    }

    public void AddVertex(Vector3d pos, Vector2f textureCoords, Vector3d normal){
        AddVertex(pos, Color.WHITE, textureCoords, normal);
    }

    public void AddVertex(Vector3d pos, Color vertexColor, Vector2f textureCoords, Vector3d normal){
        if(NumOfVertices >= Batch_Size){
            if(Batch_Size >= MAX_BATCH_SIZE){
                //Dont resize more than max batch size
                Flush();
            }else{
                //Resize batch by adding block of vertices at the end
                SetBatchSize(Batch_Size + Math.min(4096, MAX_BATCH_SIZE - Batch_Size));
            }
        }

        float r = vertexColor.r;
        float g = vertexColor.g;
        float b = vertexColor.b;
        float a = vertexColor.a;

        float texX = textureCoords.x;
        float texY = textureCoords.y;

        float normX = (float)normal.x;
        float normY = (float)normal.x;
        float normZ = (float)normal.x;

        Vertices.put((float)pos.x).put((float)pos.y).put((float)pos.z)//Vertex Position
                .put(r).put(g).put(b).put(a)//Vertex Color
                .put(texX).put(texY)//Vertex Texture Coord
                .put(normX).put(normY).put(normZ);//Vertex Normal

        NumOfVertices += 1;
    }

    private void SetBatchSize(int size){
        if(size == Batch_Size) return;
        Batch_Size = size;

        FloatBuffer newBuffer = BufferUtils.createFloatBuffer(Batch_Size * 12);
        newBuffer.put(Vertices);
        Vertices = newBuffer;

        glBufferData(GL_ARRAY_BUFFER, newBuffer.capacity() * Float.BYTES, GL_DYNAMIC_DRAW);
    }

    private void SpecifyVertexAttributes(){
        int stride = shaderProgram.TotalAttributeComponentNum * Float.BYTES;
        int pointerOffset = 0;

        for(VertexAttribute vertexAttribute : shaderProgram.Attributes.values()){
            if(vertexAttribute.Location != -1) {
                glEnableVertexAttribArray(vertexAttribute.Location);
                glVertexAttribPointer(vertexAttribute.Location, vertexAttribute.NumOfDataComponents, GL_FLOAT, false, stride, pointerOffset * Float.BYTES);
            }

            pointerOffset += vertexAttribute.NumOfDataComponents;
        }
    }

    public void Cleanup(){
        shaderProgram.Cleanup();
        Vertices.clear();

        glDeleteBuffers(VBO_ID);
        glDeleteVertexArrays(VAO_ID);
    }

}
