package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.objects.Camera;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Material {

    public ShaderProgram shaderProgram = ShaderProgram.DEFAULT;

    public Camera RenderCamera = Camera.Main_Camera; //Camera used for projection and view matrices

    public Color Diffuse;
    public Color Ambient;
    public Color Specular;
    public float Shininess;

    private Texture DiffuseMap;
    private Texture SpecularMap;

    public Material(){
        this.Diffuse = Color.WHITE;
        this.Specular = Color.WHITE;
        this.Ambient = Color.WHITE;
    }

    public void SetDiffuseTexture(Texture diffuse){
        this.DiffuseMap = diffuse;
    }

    public Texture GetDiffuseTexture(){
        return this.DiffuseMap;
    }

    public void SetSpecularTexture(Texture specular) {
        SpecularMap = specular;
    }

    public Texture GetSpecularTexture() {
        return this.SpecularMap;
    }

    public void SetDiffuse(Color diffuse){
        this.Diffuse = diffuse;
    }

    public void SetAmbient(Color ambient){
        this.Ambient = ambient;
    }

    public void SetSpecular(Color specular){
        this.Specular = specular;
    }

	public void SetShininess(float shininess){
		this.Shininess = shininess;
	}

	public Color GetDiffuse() {
		return Diffuse;
	}

	public Color GetAmbient() {
		return Ambient;
	}

	public Color GetSpecular() {
		return Specular;
	}

	public float getShininess() {
		return Shininess;
	}

    @Override
    public boolean equals(Object obj) {
        Material mat = (Material)obj;

        if(mat.shaderProgram.ProgramID != this.shaderProgram.ProgramID)
            return false;
        if(mat.Ambient != this.Ambient)
            return false;
        if(mat.Diffuse != this.Diffuse)
            return false;
        if(mat.Specular != this.Specular)
            return false;
        if(mat.Shininess != this.Shininess)
            return false;

        return true;
    }
}
