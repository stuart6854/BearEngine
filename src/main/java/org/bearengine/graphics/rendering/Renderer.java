package main.java.org.bearengine.graphics.rendering;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.debug.DebugMesh;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.bearengine.graphics.types.Material;
import main.java.org.bearengine.objects.*;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.graphics.types.RenderModel;
import main.java.org.bearengine.objects.Object;
import main.java.org.bearengine.ui.Canvas;
import main.java.org.bearengine.ui.ScrollPane;
import main.java.org.bearengine.ui.UIObject;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Stuart on 31/05/2016.
 */
public class Renderer {

    //TODO: Stop Objects, UI, Debug Meshes from Multiple Screens being rendered/put together (possible solution: make class non-static)

    private static Map<String, Mesh> MeshRenderList = new HashMap<>();
    private static List<Canvas> UIRenderList = new ArrayList<>();
    private static List<DebugMesh> DebugMeshes = new ArrayList<>();

	private static Vector3f lightPos = new Vector3f(-2f, 152, 0f);

    public static void RenderObjects(){
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        for(Mesh mesh : MeshRenderList.values()){
	        ShaderProgram shaderProgram = mesh.material.shaderProgram;
            shaderProgram.Bind();
            if(mesh.material.GetTexture() != null)
                mesh.material.GetTexture().Bind();

            if(DevCamera.ENABLED) {
                shaderProgram.SetUniform("projection", DevCamera.DEV_CAMERA.GetProjection());
                shaderProgram.SetUniform("view", DevCamera.DEV_CAMERA.GetViewMatrix());
            } else {
                shaderProgram.SetUniform("projection", mesh.material.RenderCamera.GetProjection());
                shaderProgram.SetUniform("view", mesh.material.RenderCamera.GetViewMatrix());
            }

            //Lighting Shader Test
	        if(shaderProgram.ProgramID == ShaderProgram.DEFAULT_LIGHTING.ProgramID) {
		        ShaderProgram.DEFAULT_LIGHTING.SetUniform("objectColor", new Color(1.0f, 0.5f, 0.31f));
		        ShaderProgram.DEFAULT_LIGHTING.SetUniform("lightColor", new Color(1.0f, 1.0f, 1.0f));
		        ShaderProgram.DEFAULT_LIGHTING.SetUniform("lightPosition", lightPos);
	        }

            mesh.renderModel.PrepareRender();

            for(Object obj : mesh.renderModel.GetTransforms()) {
                Matrix4d transform = obj.GetTransformMatrix();
                shaderProgram.SetUniform("model", transform);

                glDrawElements(GL_TRIANGLES, mesh.IndicesCount, GL_UNSIGNED_INT, 0);
            }
        }
    }

    public static void RenderUI(){
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        for(Canvas canvas : UIRenderList){
            RenderUIObjectChildren(canvas, canvas);
        }
    }

    private static void RenderUIObjectChildren(UIObject uiObject, Canvas canvas){
        for(UIObject child : uiObject.GetChildren()) {
            if(!child.IsVisible) continue;

            Mesh mesh = child.GetMesh();
            if(mesh.renderModel == null) continue;

            Material material = mesh.material;
            ShaderProgram shaderProgram = material.shaderProgram;
            shaderProgram.Bind();
            if(material.GetTexture() != null)
                material.GetTexture().Bind();

            if(canvas.Render_Space == RenderSpace.SCREEN_SPACE) {
                shaderProgram.SetUniform("projection", new Matrix4d().ortho2D(0, Display.mainDisplay.getWidth(), Display.mainDisplay.getHeight(), 0));
                shaderProgram.SetUniform("view", new Matrix4d());
            } else {
                shaderProgram.SetUniform("projection", Camera.Main_Camera.GetProjection());

                if(DevCamera.ENABLED) {
                    mesh.material.shaderProgram.SetUniform("view", DevCamera.DEV_CAMERA.GetViewMatrix());
                } else {
                    mesh.material.shaderProgram.SetUniform("view", Camera.Main_Camera.GetViewMatrix());
                }
            }

            shaderProgram.SetUniform("model", child.GetTransformMatrix());

            mesh.renderModel.PrepareRender();
    
            if(child instanceof ScrollPane){
                int x = 10;
                int y = 720 - 400 - 10;
                int width = 512;
                int height = 400;
                glEnable(GL_SCISSOR_TEST);
                glScissor(x, y, width, height);
            }
            
            glDrawElements(GL_TRIANGLES, mesh.IndicesCount, GL_UNSIGNED_INT, 0);
            
            RenderUIObjectChildren(child, canvas);
            
            if(child instanceof ScrollPane){
                glDisable(GL_SCISSOR_TEST);
            }
            
        }
    }

    public static void RenderDebugMeshes(){
        glEnable(GL11.GL_DEPTH_TEST);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

        glLineWidth(2);

        for(DebugMesh mesh : DebugMeshes){
            mesh.material.shaderProgram.Bind();

            ShaderProgram shaderProgram = mesh.material.shaderProgram;

            if(mesh.GetRenderSpace() == RenderSpace.SCREEN_SPACE) {
                shaderProgram.SetUniform("projection", new Matrix4d().ortho2D(0, Display.mainDisplay.getWidth(), Display.mainDisplay.getHeight(), 0));
                shaderProgram.SetUniform("view", new Matrix4d());
            } else {
                shaderProgram.SetUniform("projection", Camera.Main_Camera.GetProjection());

                if(DevCamera.ENABLED) {
                    mesh.material.shaderProgram.SetUniform("view", DevCamera.DEV_CAMERA.GetViewMatrix());
                } else {
                    mesh.material.shaderProgram.SetUniform("view", Camera.Main_Camera.GetViewMatrix());
                }
            }

            mesh.renderModel.PrepareRender();

            for(Object obj : mesh.renderModel.GetTransforms()) {
                Matrix4d transform = obj.GetTransformMatrix();
                mesh.material.shaderProgram.SetUniform("model", transform);

                glDrawElements(GL_LINES, mesh.IndicesCount, GL_UNSIGNED_INT, 0);
            }
        }

        glLineWidth(1);
    }

    public static void RegisterGameObject(GameObject obj){
        Debug.log("Renderer -> Registering GameObject: " + obj.Name);
	    Mesh mesh = MeshRenderList.get(obj.GetMesh().Mesh_Name + obj.GetMesh().material.shaderProgram.ProgramID);

        if(mesh != null && obj.GetMesh().material.shaderProgram.ProgramID == mesh.material.shaderProgram.ProgramID){
            RenderModel model = MeshRenderList.get(obj.GetMesh().Mesh_Name).renderModel;
            model.AddTransform(obj);
            Debug.log("Renderer -> Mesh('" + obj.GetMesh().Mesh_Name + "') already registered. Adding transform.");
        }else{
            obj.GetMesh().renderModel.AddTransform(obj);
            MeshRenderList.put(obj.GetMesh().Mesh_Name + obj.GetMesh().material.shaderProgram.ProgramID, obj.GetMesh());
            Debug.log("Renderer -> New Mesh. Adding  Mesh('" + obj.GetMesh().Mesh_Name + "')to MeshRenderList.");
        }
    }

    public static void UnregisterGameObject(GameObject obj){
        if(obj.GetMesh() == null) return;
        if(MeshRenderList.containsKey(obj.GetMesh().Mesh_Name)){
            Debug.log("Renderer -> UnRegistering GameObject: " + obj.Name);
            RenderModel model = MeshRenderList.get(obj.GetMesh().Mesh_Name).renderModel;
            model.RemoveTransform(obj);
            if(model.GetTransforms().size() == 0) MeshRenderList.remove(obj.GetMesh().Mesh_Name);
        }
    }

    public static void RegisterUICanvas(Canvas canvas){
        Debug.log("Renderer -> Registering UI Canvas: " + canvas.Name);
        if(!UIRenderList.contains(canvas)){
            UIRenderList.add(canvas);
            Debug.log("Renderer -> New Canvas. Adding '" + canvas.Name + "' to UIRenderList.");
        }else{
            Debug.log("Renderer -> Canvas '" + canvas.Name + "' already registered!");
        }
    }

    public static void UnregisterUICanvas(Canvas canvas){
        if(UIRenderList.contains(canvas)){
            Debug.log("Renderer -> Unregistering UI Canvas: " + canvas.Name);
            UIRenderList.remove(canvas);
        }
    }

    public static void RegisterDebugMesh(DebugMesh mesh){
        Debug.log("Renderer -> Registering DebugMesh: " + mesh.Mesh_Name);
        if(!DebugMeshes.contains(mesh.Mesh_Name)){
            DebugMeshes.add(mesh);
        }
    }

    public static void UnregisterDebugMesh(DebugMesh mesh){
        if(DebugMeshes.contains(mesh)){
            Debug.log("Renderer -> UnRegistering DebugMesh: " + mesh.Mesh_Name);
            DebugMeshes.remove(mesh);
        }
    }

}
