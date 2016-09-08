package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.objects.Camera;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Material {

    public ShaderProgram shaderProgram = ShaderProgram.DEFAULT; //TODO: Make ShaderPrograms-Per-Material Based

    public Camera RenderCamera = Camera.Main_Camera; //Camera used for projection and view matrices

    public Color Diffuse;
    public Color Specular;
    public Color Ambient;

    private Texture texture;

    public Material(){
        this.Diffuse = Color.WHITE;
        this.Specular = Color.WHITE;
        this.Ambient = Color.WHITE;
    }

    public void SetTexture(Texture texture){
        this.texture = texture;
    }

    public Texture GetTexture(){
        return this.texture;
    }

}
