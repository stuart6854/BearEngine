package main.java.org.bearengine.debug;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 06/07/2016.
 */
public class DebugDraw {



    public List<Float> Vertices = new ArrayList<>();

    public void BeginFrame(){

    }

    public void EndFrame(){

    }

    public void Render(){
        float[] verts = new float[Vertices.size()];
        for(int i = 0; i < Vertices.size(); i++) verts[i] = Vertices.get(i);

        FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(Vertices.size());
        vertBuffer.put(verts);
        vertBuffer.flip();

        int[] indices = { 0, 1 };

        IntBuffer indiceBuffer = BufferUtils.createIntBuffer(indices.length);
        indiceBuffer.put(indices);
        indiceBuffer.flip();



    }
    
}
