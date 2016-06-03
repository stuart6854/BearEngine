package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.debug.Debug;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Created by Stuart on 21/05/2016.
 */
public class Texture {

    public static Texture CURRENT;

    public final int ID;

    public boolean Disposed = false;

    public Texture(){
        this.ID = GenTextureID();
    }

    private int GenTextureID(){
        return glGenTextures();
    }

    public Texture UploadTexture(Image image){
        if(Disposed) return null; //On the off-chance Cleanup() was called

        Debug.log("Texture -> Creating Texture from: " + image.Name);

        Bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        if(image.Components == 4)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.Width, image.Height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.ImageData);
        else
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, image.Width, image.Height, 0, GL_RGB, GL_UNSIGNED_BYTE, image.ImageData);

        glGenerateMipmap(GL_TEXTURE_2D);

        return this;
    }

    public void Bind(){
        if(Disposed) return;

        if(CURRENT == this) return;

        glBindTexture(GL_TEXTURE_2D, ID);
        CURRENT = this;
    }

    public void Cleanup(){
        Disposed = true;
        glDeleteTextures(ID);
    }

}