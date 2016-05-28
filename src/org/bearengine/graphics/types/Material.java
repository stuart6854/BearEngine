package org.bearengine.graphics.types;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Material {

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
