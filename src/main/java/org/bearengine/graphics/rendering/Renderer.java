package main.java.org.bearengine.graphics.rendering;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.debug.DebugMesh;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Material;
import main.java.org.bearengine.objects.Camera;
import main.java.org.bearengine.objects.DevCamera;
import main.java.org.bearengine.objects.GameObject;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.graphics.types.RenderModel;
import main.java.org.bearengine.objects.Object;
import main.java.org.bearengine.ui.Canvas;
import main.java.org.bearengine.ui.Panel;
import main.java.org.bearengine.ui.ScrollPane;
import main.java.org.bearengine.ui.UIObject;
import main.java.org.joml.Matrix4d;
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

    public static void RenderObjects(){
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        for(Mesh mesh : MeshRenderList.values()){
            mesh.material.shaderProgram.Bind();
            if(mesh.material.GetTexture() != null)
                mesh.material.GetTexture().Bind();

            if(DevCamera.ENABLED) {
                mesh.material.shaderProgram.setUniform("projection", DevCamera.DEV_CAMERA.GetProjection());
                mesh.material.shaderProgram.setUniform("view", DevCamera.DEV_CAMERA.GetViewMatrix());
            } else {
                mesh.material.shaderProgram.setUniform("projection", mesh.material.RenderCamera.GetProjection());
                mesh.material.shaderProgram.setUniform("view", mesh.material.RenderCamera.GetViewMatrix());
            }

            mesh.renderModel.PrepareRender();

            for(Object obj : mesh.renderModel.GetTransforms()) {
                Matrix4d transform = obj.GetTransformMatrix();
                mesh.material.shaderProgram.setUniform("model", transform);

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
                shaderProgram.setUniform("projection", new Matrix4d().ortho2D(0, Display.mainDisplay.getWidth(), Display.mainDisplay.getHeight(), 0));
                shaderProgram.setUniform("view", new Matrix4d());
            } else {
                shaderProgram.setUniform("projection", Camera.Main_Camera.GetProjection());

                if(DevCamera.ENABLED) {
                    mesh.material.shaderProgram.setUniform("view", DevCamera.DEV_CAMERA.GetViewMatrix());
                } else {
                    mesh.material.shaderProgram.setUniform("view", Camera.Main_Camera.GetViewMatrix());
                }
            }

            shaderProgram.setUniform("model", child.GetTransformMatrix());

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
                shaderProgram.setUniform("projection", new Matrix4d().ortho2D(0, Display.mainDisplay.getWidth(), Display.mainDisplay.getHeight(), 0));
                shaderProgram.setUniform("view", new Matrix4d());
            } else {
                shaderProgram.setUniform("projection", Camera.Main_Camera.GetProjection());

                if(DevCamera.ENABLED) {
                    mesh.material.shaderProgram.setUniform("view", DevCamera.DEV_CAMERA.GetViewMatrix());
                } else {
                    mesh.material.shaderProgram.setUniform("view", Camera.Main_Camera.GetViewMatrix());
                }
            }

            mesh.renderModel.PrepareRender();

            for(Object obj : mesh.renderModel.GetTransforms()) {
                Matrix4d transform = obj.GetTransformMatrix();
                mesh.material.shaderProgram.setUniform("model", transform);

                glDrawElements(GL_LINES, mesh.IndicesCount, GL_UNSIGNED_INT, 0);
            }
        }

        glLineWidth(1);
    }

    public static void RegisterGameObject(GameObject obj){
        Debug.log("Renderer -> Registering GameObject: " + obj.Name);
        if(MeshRenderList.containsKey(obj.GetMesh().Mesh_Name)){
            RenderModel model = MeshRenderList.get(obj.GetMesh().Mesh_Name).renderModel;
            model.AddTransform(obj);
            Debug.log("Renderer -> Mesh('" + obj.GetMesh().Mesh_Name + "') already registered. Adding transform.");
        }else{
            obj.GetMesh().renderModel.AddTransform(obj);
            MeshRenderList.put(obj.GetMesh().Mesh_Name, obj.GetMesh());
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
