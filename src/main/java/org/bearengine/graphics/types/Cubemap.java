package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.debug.Debug;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.*;

/**
 * Created by Stuart on 25/09/2016.
 */
public class Cubemap{
    
    public static Cubemap CURRENT;
    
    public final int ID;
    
    public int TextureUnit = 0;
    
    public boolean Disposed = false;
    
    public Texture[] CubemapTextures;
    
    public Cubemap(){
        this.ID = GenTextureID();
        this.CubemapTextures = new Texture[6];
    }
    
    private int GenTextureID(){
        return glGenTextures();
    }
    
    public Cubemap UploadCubemap(Image[] skyboxImages){
        return UploadCubemap(skyboxImages, 0);
    }
    
    public Cubemap UploadCubemap(Image[] skyboxImages, int textureUnit){
        if(Disposed) return null; //On the off-chance Cleanup() was called
        
        Debug.log("Cubemap -> Creating Cubemap");
        
        this.TextureUnit = textureUnit;
        
        Bind();
        for(int i = 0; i < 6; i++) {
            Image image = skyboxImages[i];
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, image.Width, image.Height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.ImageData);
        }
    
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
    
        Debug.log("Cubemap -> Cubemap Created.");
        
        return this;
    }
    
    public void Bind(){
        if(Disposed) return;
        
        if(CURRENT == this) return;
        
        glActiveTexture(GL_TEXTURE0 + TextureUnit);
        glBindTexture(GL_TEXTURE_CUBE_MAP, ID);
        CURRENT = this;
    }
    
    public void Cleanup(){
        Disposed = true;
        glDeleteTextures(ID);
        for(Texture texture : CubemapTextures){
            texture.Cleanup();
        }
        
        CubemapTextures = null;
    }
    
    
}
