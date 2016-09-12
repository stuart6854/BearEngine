package main.java.org.bearengine.graphics.types;

import main.java.org.bearengine.math.MathUtils;
import main.java.org.joml.Vector3f;
import main.java.org.joml.Vector4f;

/**
 * Created by Stuart on 21/05/2016.
 */
public class Color {

    public static Color BLACK = new Color(0, 0, 0);
    public static Color WHITE = new Color(1, 1, 1);

    public static Color RED = new Color(1, 0, 0);
    public static Color GREEN = new Color(0, 1, 0);
    public static Color BLUE = new Color(0, 0, 1);

    public static Color MAGENTA = new Color(1, 0, 1);
    public static Color CYAN = new Color(0, 1, 1);
    public static Color YELLOW = new Color(1, 1, 0);

    public static Color SKY_BLUE = new Color(135/255f, 206f/255f, 235f/255f);

    public float r, g, b, a;

    public Color(){
        set(MAGENTA);
    }

    public Color(float r, float g, float b){
        set(r, g, b, 1.0f);
    }

    public Color(float r, float g, float b, float a){
        set(r, g, b, a);
    }

    public Color set(Color color){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;

        return this;
    }

    public Color set(float r, float g, float b, float a){
        this.r = MathUtils.Clamp(r, 0, 1);
        this.g = MathUtils.Clamp(g, 0, 1);
        this.b = MathUtils.Clamp(b, 0, 1);
        this.a = MathUtils.Clamp(a, 0, 1);

        return this;
    }

    public Vector3f GetRGB(){
        return new Vector3f(r, g, b);
    }

    public Vector4f GetRGBA(){
        return new Vector4f(r, g, b, a);
    }

    @Override
    public String toString() {
        return r + ", " + g + ", " + b + ", " + a;
    }
}
