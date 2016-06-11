package main.java.org.bearengine.font;

import main.java.org.bearengine.graphics.types.Texture;

import java.io.InputStream;

/**
 * Created by Stuart on 08/06/2016.
 */
public class Font {

    public final Texture FontAtlas;
    public final FontMetaData FontFile;

    public Font(Texture fontAtlas, InputStream stream){
        this.FontAtlas = fontAtlas;
        this.FontFile = new FontMetaData(stream);
    }



}
