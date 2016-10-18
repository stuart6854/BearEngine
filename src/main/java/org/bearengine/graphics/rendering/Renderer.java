package main.java.org.bearengine.graphics.rendering;

import jdk.internal.dynalink.beans.StaticClass;
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
import main.java.org.joml.Matrix3d;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Matrix4f;
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

    //TODO: Stop GameObjects, UI and Debug Meshes from Different Scenes being rendered/put together(Possible Solution?: make class non-static)
    //TODO: Test Directional, Point and Spot Lights(Shaders)
    //TODO: Keep reference to all lights in Scene here, to pass to lighting shaders
    
    private static Map<String, Mesh> MeshRenderList = new HashMap<>();
    private static List<Canvas> UIRenderList = new ArrayList<>();
    private static List<DebugMesh> DebugMeshes = new ArrayList<>();

    private static Skybox SkyBox;
    
	private static Vector3f lightPos = new Vector3f(-2f, 152, 0f);

    public static Light LightSource;
    private static ArrayList<Light> ScenesLights = new ArrayList<>();

    public static void RenderObjects(){
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        
        RenderSkybox();
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        for(Mesh mesh : MeshRenderList.values()){
	        ShaderProgram shaderProgram = mesh.material.shaderProgram;
            shaderProgram.Bind();
            if(mesh.material.GetDiffuseTexture() != null)
                mesh.material.GetDiffuseTexture().Bind();

            if(DevCamera.ENABLED) {
                shaderProgram.SetUniform("projection", DevCamera.DEV_CAMERA.GetProjection());
                shaderProgram.SetUniform("view", DevCamera.DEV_CAMERA.GetViewMatrix());
            } else {
                shaderProgram.SetUniform("projection", mesh.material.RenderCamera.GetProjection());
                shaderProgram.SetUniform("view", mesh.material.RenderCamera.GetViewMatrix());
            }

            //Lighting Shader Test
	        if(shaderProgram.ProgramID == ShaderProgram.DEFAULT_LIGHTING.ProgramID) {
		        ShaderProgram.DEFAULT_LIGHTING.SetUniform("viewPosition", Camera.Main_Camera.GetPosition());
		        ShaderProgram.DEFAULT_LIGHTING.SetUniform("material", mesh.material);
                ShaderProgram.DEFAULT_LIGHTING.SetUniform("light", LightSource);
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
            if(material.GetDiffuseTexture() != null)
                material.GetDiffuseTexture().Bind();

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

            child.PreRender();
            
            mesh.renderModel.PrepareRender();
            
            glDrawElements(GL_TRIANGLES, mesh.IndicesCount, GL_UNSIGNED_INT, 0);
            
            RenderUIObjectChildren(child, canvas);
            
            child.PostRender();
            
        }
    }

    private static void RenderSkybox(){
        if(SkyBox == null) return;
        
        glDepthFunc(GL_LEQUAL);
        
        Mesh mesh = SkyBox.GetMesh();
        
        ShaderProgram shaderProgram = mesh.material.shaderProgram;
        shaderProgram.Bind();
        
        SkyBox.GetCubemap().Bind();
        
        Matrix3d tempView = new Matrix3d();
        
        if(DevCamera.ENABLED) {
            shaderProgram.SetUniform("projection", DevCamera.DEV_CAMERA.GetProjection());
            DevCamera.DEV_CAMERA.GetViewMatrix().get3x3(tempView);
        } else {
            shaderProgram.SetUniform("projection", mesh.material.RenderCamera.GetProjection());
            mesh.material.RenderCamera.GetViewMatrix().get3x3(tempView);
        }
    
        Matrix4d view = new Matrix4d(tempView);
        shaderProgram.SetUniform("view", view);
        
        mesh.renderModel.PrepareRender();
        
        glDrawElements(GL_TRIANGLES, mesh.IndicesCount, GL_UNSIGNED_INT, 0);
        
        glDepthFunc(GL_LESS);
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

    public static void RenderDebugMeshes(List<DebugMesh> debugMeshes){
        glEnable(GL11.GL_DEPTH_TEST);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        glLineWidth(2);

        for(DebugMesh mesh : debugMeshes){
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

            Matrix4d transform = mesh.transform.GenerateMatrix();
            mesh.material.shaderProgram.SetUniform("model", transform);

            mesh.renderModel.PrepareRender();

            glDrawElements(GL_TRIANGLES, mesh.IndicesCount, GL_UNSIGNED_INT, 0);
        }

        glLineWidth(1);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    public static void RegisterGameObject(GameObject obj){
        Debug.log("Renderer -> Registering GameObject: " + obj.Name);
	    Mesh mesh = MeshRenderList.get(obj.GetMesh().Mesh_Name + obj.GetMesh().material.shaderProgram.ProgramID);

        if(mesh != null && obj.GetMesh().equals(mesh)){
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
            Debug.log("Renderer -> Unregistering GameObject: " + obj.Name);
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

    public static void SetSkybox(Skybox skyBox){
        Renderer.SkyBox = skyBox;
        Debug.log("Renderer -> Skybox Set.");
    }
    
    public static void RegisterLight(Light light){
        Debug.log("Renderer -> Registering Light: " + light.Name);
        if(!ScenesLights.contains(light)){
            ScenesLights.add(light);
        }else{
            Debug.log("Renderer -> Light(" + light.Name + ") already registered!");
        }
    }

    public static void UnregisterLight(Light light){
        Debug.log("Renderer -> Unregistering Light: " + light.Name);
        if(ScenesLights.contains(light)){
            ScenesLights.remove(light);
        }else{
            Debug.log("Renderer -> Light(" + light.Name + ") already Unregistered!");
        }
    }

}
