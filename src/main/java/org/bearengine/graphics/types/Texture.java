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

    public int Width, Height;

    public Texture(){
        this.ID = GenTextureID();
    }

    private int GenTextureID(){
        return glGenTextures();
    }

    public Texture UploadTexture(Image image){
        if(Disposed) return null; //On the off-chance Cleanup() was called

        Debug.log("Texture -> Creating Texture from: " + image.Name);

        this.Width = image.Width;
        this.Height = image.Height;

        Bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.Width, image.Height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.ImageData);

        glGenerateMipmap(GL_TEXTURE_2D);

        Debug.log("Texture -> Texture Created.");

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
