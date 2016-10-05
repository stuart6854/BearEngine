package main.java.org.bearengine.debug;

import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.rendering.RenderSpace;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.ui.Canvas;
import main.java.org.bearengine.ui.InputField;
import main.java.org.bearengine.ui.Panel;
import main.java.org.bearengine.utils.File;
import main.java.org.bearengine.utils.ResourceLoader;

/**
 * Created by Stuart on 28/09/2016.
 */
public class Console {
    
    private static Canvas CONSOLE_CANVAS;
    private static Panel CONSOLE_PANEL;
    private static InputField CMD_LINE;
    
    private static Font CMD_LINE_FONT;
    private static Font CMD_LINE_PLCHLDR_FONT;
    
    public static void Setup(){
        LoadFonts();
        
        CONSOLE_CANVAS = new Canvas(RenderSpace.SCREEN_SPACE);
        
        CONSOLE_PANEL = new Panel();
        CONSOLE_PANEL.SetWidth(Display.mainDisplay.getWidth());
        CONSOLE_PANEL.SetHeight(256);
        CONSOLE_PANEL.SetShowDebugMesh(true);
        CONSOLE_PANEL.GetMesh().material.SetDiffuse(new Color(0, 0, 0, 0.85f));
        CONSOLE_PANEL.SetParent(CONSOLE_CANVAS);
        
        CMD_LINE = new InputField(CMD_LINE_FONT, CMD_LINE_PLCHLDR_FONT, CONSOLE_PANEL);
        CMD_LINE.SetNormalisedPosition(0f, 1f);
        CMD_LINE.SetWidth(CONSOLE_PANEL.GetPixelWidth());
        CMD_LINE.SetHeight(32);
    }
    
    public static void Update(){
        CONSOLE_CANVAS.Update();
    }
    
    private static void LoadFonts(){
        Image fontImage = ResourceLoader.Load("/main/java/resources/fonts/OpenSans_61_DF.png", Image.class, false);
        Texture texture = new Texture().UploadTexture(fontImage, 0);
        File fontFile = ResourceLoader.Load("/main/java/resources/fonts/OpenSans_61_DF.fnt", File.class);
        CMD_LINE_FONT = new Font(0.15f, texture, fontFile);
        
        CMD_LINE_PLCHLDR_FONT = new Font(0.1f, texture, fontFile);
    }
    
}
