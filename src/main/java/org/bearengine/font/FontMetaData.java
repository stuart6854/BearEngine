package main.java.org.bearengine.font;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.utils.File;
import main.java.org.bearengine.utils.FileUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stuart on 08/06/2016.
 */
public class FontMetaData {

    public static String TOKEN_SPLITTER = "\\s+";
    public static String ATTRIBUTE_SPLITTER = "=";
    public static String ATTRIBUTE_VALUE_SPLITTER = ",";

    public Character[] characters;

    private Map<String, String> Attributes;

    public float SpaceWidth = 0;

    public float HorizontalPerPixelSize;
    public float VerticalPerPixelSize;

    //Info
    public String TypeFace;
    public int FontSize;
    public int PaddingWidth, PaddingHeight;
    public int SpacingX, SpacingY;

    //Common
    public int LineHeight; //Distance in Pixels between each line of text
    public int Base; //Number of Pixels from the absolute top of line to base of characters
    public int TextureWidth, TextureHeight;

    public FontMetaData(File fontFile) {
        characters = new Character[256];
        Attributes = new HashMap<>();
        ParseFontFile(fontFile);
    }

    private void ParseFontFile(File fontFile){
        Debug.log("FontMetaData -> Parsing Font File.");

        String[] Lines = fontFile.FileContents;
        for(String line : Lines){
            String[] tokens = line.split(TOKEN_SPLITTER);

            switch(tokens[0]){
                case "info":
                    ProcessInfoLine(tokens);
                    break;
                case "common":
                    ProcessCommonLine(tokens);
                    break;
                case "char":
                    ProcessCharLine(tokens);
                    break;
                case "kernings":
                case "kerning":
                    break;
                default:
                    //Debug.log("FontMetaData -> ParseFontFile -> Unrecognised Token!");
                    break;
            }
        }
        Debug.log("FontMetaData -> Font File Parsed.");
    }

    private void ProcessInfoLine(String[] tokens){
        //Debug.log("FontMetaData -> Processing Font Info.");

        for(int i = 1; i < tokens.length; i++){
            String[] token = tokens[i].split(ATTRIBUTE_SPLITTER);
            String key = token[0];
            String value = token[1];
            Attributes.put(key, value);
            //Debug.log("FontMetaData -> Font Attribute " + key + "=" + value);
        }
    }

    private void ProcessCommonLine(String[] tokens){
        //Debug.log("FontMetaData -> Processing Font Common Info.");

        for(int i = 1; i < tokens.length; i++){
            String[] token = tokens[i].split(ATTRIBUTE_SPLITTER);
            String key = token[0];
            String value = token[1];
            Attributes.put(key, value);
            //Debug.log("FontMetaData -> Font Attribute " + key + "=" + value);
        }
        RetrieveValues();
    }

    private void ProcessCharLine(String[] tokens){
        int Char = Integer.valueOf(tokens[1].split("=")[1]);
        //Debug.log("FontMetaData -> Processing Font Char Info. Char: " + ((char)Char));
        Map<String, String> charAttribs = new HashMap<>();
        for(int i = 1; i < tokens.length; i++){
            String[] token = tokens[i].split(ATTRIBUTE_SPLITTER);
            String key = token[0];
            String value = token[1];
            charAttribs.put(key, value);
            //Debug.log("FontMetaData -> Char Attribute " + key + "=" + value);
        }
        characters[Char] = new Character(charAttribs, TextureWidth, TextureHeight);

        if(Char == 32){
            this.SpaceWidth = Integer.valueOf(charAttribs.get("xadvance")) - PaddingWidth;
        }
    }

    private void RetrieveValues(){
        TypeFace = Attributes.get("face");

        FontSize = Integer.valueOf(Attributes.get("size"));

        String[] padding = Attributes.get("padding").split(ATTRIBUTE_VALUE_SPLITTER);
        PaddingWidth = Integer.valueOf(padding[1]) + Integer.valueOf(padding[3]);
        PaddingHeight = Integer.valueOf(padding[0]) + Integer.valueOf(padding[2]);

        String[] spacing = Attributes.get("spacing").split(ATTRIBUTE_VALUE_SPLITTER);
        SpacingX = Integer.valueOf(spacing[0]);
        SpacingY = Integer.valueOf(spacing[1]);

        LineHeight = Integer.valueOf(Attributes.get("lineHeight"));
        LineHeight -= PaddingHeight;

        Base = Integer.valueOf(Attributes.get("base"));

        TextureWidth = Integer.valueOf(Attributes.get("scaleW"));
        TextureHeight = Integer.valueOf(Attributes.get("scaleH"));
    }

}
