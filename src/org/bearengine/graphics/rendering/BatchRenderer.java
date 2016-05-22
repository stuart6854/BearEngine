package org.bearengine.graphics.rendering;

import org.bearengine.graphics.Display;
import org.bearengine.graphics.shaders.ShaderProgram;
import org.bearengine.graphics.types.Color;
import org.bearengine.graphics.types.VertexAttribute;
import org.bearengine.math.types.Matrix4;
import org.bearengine.math.types.Vector2;
import org.bearengine.math.types.Vector3;
import org.bearengine.utils.GLError;
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

    private int VAO_ID;
    private int VBO_ID;

    private FloatBuffer Vertices;
    private int NumOfVertices;
    private boolean isDrawing;

    private ShaderProgram shaderProgram;
    private final Matrix4 projection;

    public BatchRenderer(Matrix4 projection, ShaderProgram shaderProgram){
        this.projection = projection;
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
        Vertices = BufferUtils.createFloatBuffer(4096);

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
        if(NumOfVertices == 0) return;

        Vertices.flip();

        shaderProgram.Bind();

        shaderProgram.setUniforms("projection", projection);
        shaderProgram.setUniforms("view", Matrix4.translate(0, 0, 0));
        shaderProgram.setUniforms("model", Matrix4.translate(0, 0, 0));

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

        GLError.Check("BatchRenderer -> Flush()");

        //Debug.log("BatchRenderer -> Flush.");
    }

    public void AddVertex(Vector3 pos, Color vertexColor, Vector2 textureCoords){
        if(Vertices.remaining() < 9){
            //We need for space in buffer, so flush it
            Flush();
        }

        float r = vertexColor.r;
        float g = vertexColor.g;
        float b = vertexColor.b;
        float a = vertexColor.a;

        float texX = textureCoords.x;
        float texY = textureCoords.y;

        Vertices.put(pos.x).put(pos.y).put(pos.z)//Vertex Position
                .put(r).put(g).put(b).put(a)//Vertex Color
                .put(texX).put(texY);//Vertex Texture Coord

        NumOfVertices += 1;
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

        GLError.Check("BatchRenderer -> SpecifyVertexAttributes()");
    }

    public void Cleanup(){
        shaderProgram.Cleanup();
        Vertices.clear();

        glDeleteBuffers(VBO_ID);
        glDeleteVertexArrays(VAO_ID);
    }

}
