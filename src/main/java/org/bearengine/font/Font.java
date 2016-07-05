package main.java.org.bearengine.font;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.utils.File;

import java.io.InputStream;

/**
 * Created by Stuart on 08/06/2016.
 */
public class Font {

    public final Texture FontAtlas;
    public final FontMetaData FontFile;

    private float FontSize = 1;

    private boolean Bold = false;
    private boolean Italic = false;

    public Font(int fontSize, Texture fontAtlas, File fontFile){
        this.FontAtlas = fontAtlas;
        this.FontFile = new FontMetaData(fontFile);
        SetFontSize(fontSize);
    }

    public void SetFontSize(int fontSize){
        this.FontSize = (float)fontSize / (float)FontFile.FontSize;
    }

    public float GetRelativeFontSize(){
        return this.FontSize;
    }

    public int GetActualFontSize(){
        return (int)(this.FontSize * FontFile.FontSize);
    }

}
