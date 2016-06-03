package main.java.org.bearengine.graphics.rendering;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.objects.GameObject;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.graphics.types.RenderModel;
import main.java.org.bearengine.objects.Camera;
import main.java.org.bearengine.objects.Object;
import main.java.org.bearengine.utils.GLError;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Stuart on 31/05/2016.
 */
public class Renderer {

    private static Map<String, Mesh> RenderList = new HashMap<>();

    public static void Render(){
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        for(Mesh mesh : RenderList.values()){
            mesh.material.GetTexture().Bind();
            mesh.material.shaderProgram.Bind();

            mesh.material.shaderProgram.setUniform("projection", Camera.Main_Camera.GetProjection());
            mesh.material.shaderProgram.setUniform("view", Camera.Main_Camera.GetViewMatrix());

            mesh.renderModel.PrepareRender();

            for(Object obj : mesh.renderModel.GetTransforms()) {
                Matrix4d transform = obj.GetTransformMatrix();
                mesh.material.shaderProgram.setUniform("model", transform);

                glDrawElements(GL_TRIANGLES, mesh.IndicesCount, GL_UNSIGNED_INT, 0);
                GLError.Check("Renderer");
            }
        }
    }

    public static void RegisterGameObject(GameObject obj){
        Debug.log("Renderer -> Registering GameObject.");
        if(RenderList.containsKey(obj.getMesh().Mesh_Name)){
            RenderModel model = RenderList.get(obj.getMesh().Mesh_Name).renderModel;
            model.AddTransform(obj);
            Debug.log("Renderer -> Mesh already registered. Adding transform.");
        }else{
            obj.getMesh().renderModel.AddTransform(obj);
            RenderList.put(obj.getMesh().Mesh_Name, obj.getMesh());
            Debug.log("Renderer -> New Mesh. Adding to RenderList.");
        }
    }

    public static void UnRegisterGameObject(GameObject obj){
        if(obj.getMesh() == null) return;
        if(RenderList.containsKey(obj.getMesh().Mesh_Name)){
            RenderModel model = RenderList.get(obj.getMesh().Mesh_Name).renderModel;
            model.RemoveTransform(obj);
        }
    }

}
