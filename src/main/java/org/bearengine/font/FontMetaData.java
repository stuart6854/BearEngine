package main.java.org.bearengine.font;

import main.java.org.bearengine.debug.Debug;
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

    private FontCharMetaData[] charMetaData;

    private Map<String, String> Attributes;

    //Info
    private int PaddingUp, PaddingRight, PaddingDown, PaddingLeft;

    //Common
    private int LineHeight; //Distance in Pixels between each line of text
    private int Base; //Number of Pixels from the absolute top of line to base of characters
    private int TextureWidth, TextureHeight;

    public FontMetaData(InputStream stream) {
        charMetaData = new FontCharMetaData[256];
        Attributes = new HashMap<>();
        ParseFontFile(stream);
    }

    private void ParseFontFile(InputStream stream){
        Debug.log("FontMetaData -> Parsing Font File.");

        String[] Lines = FileUtils.ReadLines(stream);
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
                    Debug.log("FontMetaData -> ParseFontFile -> Unrecognised Token!");
                    break;
            }
        }
        Debug.log("FontMetaData -> Font File Parsed.");
    }

    private void ProcessInfoLine(String[] tokens){
        Debug.log("FontMetaData -> Processing Font Info.");

        for(int i = 1; i < tokens.length; i++){
            String[] token = tokens[i].split(ATTRIBUTE_SPLITTER);
            String key = token[0];
            String value = token[1];
            Attributes.put(key, value);
            Debug.log("FontMetaData -> Font Attribute " + key + "=" + value);
        }
    }

    private void ProcessCommonLine(String[] tokens){
        Debug.log("FontMetaData -> Processing Font Common Info.");

        for(int i = 1; i < tokens.length; i++){
            String[] token = tokens[i].split(ATTRIBUTE_SPLITTER);
            String key = token[0];
            String value = token[1];
            Attributes.put(key, value);
            Debug.log("FontMetaData -> Font Attribute " + key + "=" + value);
        }
        RetrieveValues();
    }

    private void ProcessCharLine(String[] tokens){
        int Char = Integer.valueOf(tokens[1].split("=")[1]);
        Debug.log("FontMetaData -> Processing Font Char Info. Char: " + ((char)Char));
        Map<String, String> charAttribs = new HashMap<>();
        for(int i = 1; i < tokens.length; i++){
            String[] token = tokens[i].split(ATTRIBUTE_SPLITTER);
            String key = token[0];
            String value = token[1];
            charAttribs.put(key, value);
            Debug.log("FontMetaData -> Char Attribute " + key + "=" + value);
        }
        charMetaData[Char] = new FontCharMetaData(charAttribs, TextureWidth, TextureHeight);
    }

    private void RetrieveValues(){
        String[] padding = Attributes.get("padding").split(ATTRIBUTE_VALUE_SPLITTER);
        PaddingUp = Integer.valueOf(padding[0]);
        PaddingRight = Integer.valueOf(padding[1]);
        PaddingDown = Integer.valueOf(padding[2]);
        PaddingLeft = Integer.valueOf(padding[3]);

        LineHeight = Integer.valueOf(Attributes.get("lineHeight"));

        Base = Integer.valueOf(Attributes.get("base"));

        TextureWidth = Integer.valueOf(Attributes.get("scaleW"));
        TextureHeight = Integer.valueOf(Attributes.get("scaleH"));
    }

}
