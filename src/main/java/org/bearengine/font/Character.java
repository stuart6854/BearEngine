package main.java.org.bearengine.font;

import java.util.Map;

/**
 * Created by Stuart on 26/06/2016.
 */
public class Character {

    public int id;
    public char character;
    public float xUV;
    public float yUV;
    public float xMaxUV;
    public float yMaxUV;
    public float xOffset;
    public float yOffset;
    public int sizeX;
    public int sizeY;
    public float xAdvance;

    public Character(Map<String, String> data, int textureWidth, int textureHeight){
        this.id = Integer.valueOf(data.get("id"));
        this.character = (char)this.id;
        this.xUV = Float.valueOf(data.get("x")) / (float)textureWidth;
        this.yUV = Float.valueOf(data.get("y")) / (float)textureHeight;

        int width = Integer.valueOf(data.get("width"));
        int height = Integer.valueOf(data.get("height"));
        this.xMaxUV = (Float.valueOf(data.get("x")) + (float)width) / (float)textureWidth;
        this.yMaxUV = (Float.valueOf(data.get("y")) + (float)height) / (float)textureHeight;
        this.xOffset = Float.valueOf(data.get("xoffset"));
        this.yOffset = Float.valueOf(data.get("yoffset"));
        this.sizeX = Integer.valueOf(data.get("width"));
        this.sizeY = Integer.valueOf(data.get("height"));
        this.xAdvance = Float.valueOf(data.get("xadvance"));
    }
    
}
