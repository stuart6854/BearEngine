package main.java.org.bearengine.objects;

import main.java.org.bearengine.graphics.rendering.Renderer;
import main.java.org.bearengine.graphics.types.Color;

/**
 * Created by Stuart on 11/09/2016.
 */
public class Light extends GameObject{

    public Color Ambient;
    public Color Diffuse;
    public Color Specular;

    public Light(){
        super();

        Renderer.RegisterLight(this);
    }

}
