package main.java.org.bearengine.font;

import java.util.Map;

/**
 * Created by Stuart on 08/06/2016.
 */
public class FontCharMetaData {
    
    public int id;
    public char character;
    public double xUV;
    public double yUV;
    public double xMaxUV;
    public double yMaxUV;
    public double xOffset;
    public double yOffset;
    public double xAdvance;

    public FontCharMetaData(Map<String, String> data, int textureWidth, int textureHeight){
        this.id = Integer.valueOf(data.get("id"));
        this.character = (char)this.id;
        this.xUV = Double.valueOf(data.get("x")) / (float)textureWidth;
        this.yUV = Double.valueOf(data.get("y")) / (float)textureHeight;

        int width = Integer.valueOf(data.get("width"));
        int height = Integer.valueOf(data.get("height"));
        this.xMaxUV = (Double.valueOf(data.get("x")) + (float)width) / (float)textureWidth;
        this.yMaxUV = (Double.valueOf(data.get("y")) + (float)height) / (float)textureHeight;
        this.xOffset = Double.valueOf(data.get("xoffset"));
        this.yOffset = Double.valueOf(data.get("yoffset"));
        this.xAdvance = Double.valueOf(data.get("xadvance"));
    }

}
