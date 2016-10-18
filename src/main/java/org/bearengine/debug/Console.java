package main.java.org.bearengine.debug;

import main.java.org.bearengine.debug.console.Command;
import main.java.org.bearengine.debug.console.Commands;
import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.rendering.RenderSpace;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.input.Keyboard;
import main.java.org.bearengine.ui.Canvas;
import main.java.org.bearengine.ui.InputField;
import main.java.org.bearengine.ui.Label;
import main.java.org.bearengine.ui.Panel;
import main.java.org.bearengine.utils.File;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.bearengine.utils.Utils;
import org.lwjgl.ovr.OVRUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stuart on 28/09/2016.
 */
public class Console {

    private static Commands COMMANDS;

    private static Canvas CONSOLE_CANVAS;
    private static Panel CONSOLE_PANEL;
    private static Label OUTPUT_LABEL;
    private static InputField CMD_LINE;
    
    private static Font CMD_LINE_FONT;
    private static Font CMD_LINE_PLCHLDR_FONT;

    private static Queue<String> Output = new LinkedList<>();
    
    public static void Setup(){
        COMMANDS = new Commands();
        COMMANDS.InitialiseCommands();

        LoadFonts();
        SetupUI();
    }
    
    public static void Update(){
        if(CMD_LINE.IsActive() && Keyboard.isClicked(Keyboard.KEY_ENTER)){
            if(!CMD_LINE.IsEmpty()){
                //Find and Call Command
                CallCommand();
                OUTPUT_LABEL.SetText(GetOutputString());
            }

            CMD_LINE.Clear();
        }

        CONSOLE_CANVAS.Update();// Always do me last
    }

    private static void CallCommand() {
        String input = CMD_LINE.GetInputText();

        String cmdAlias = input;
        String[] args = null;
        if (cmdAlias.contains(" ")) {
            cmdAlias = cmdAlias.substring(0, input.indexOf(32));
            args = GetArguments(input.substring(input.indexOf(" ") + 1));
        }

        Debug.log("Console -> Got Command Signature -> " + cmdAlias);
        Debug.log("Console -> Got Command Args -> " + Utils.ArrayToString(args));

        Command cmd = COMMANDS.FindCommand(cmdAlias);
        if (cmd != null){
            AddOutput(cmd.CommandMethod.Execute(args));
        }else
            AddOutput("No Such Command!");
    }

    private static void SetupUI(){
        CONSOLE_CANVAS = new Canvas(RenderSpace.SCREEN_SPACE);

        CONSOLE_PANEL = new Panel();
        CONSOLE_PANEL.SetWidth(Display.mainDisplay.getWidth());
        CONSOLE_PANEL.SetHeight(256);
        CONSOLE_PANEL.SetShowDebugMesh(true);
        CONSOLE_PANEL.GetMesh().material.SetDiffuse(new Color(0, 0, 0, 0.85f));
        CONSOLE_PANEL.SetParent(CONSOLE_CANVAS);

        OUTPUT_LABEL = new Label("", CMD_LINE_FONT);
        OUTPUT_LABEL.SetParent(CONSOLE_PANEL);

        CMD_LINE = new InputField(CMD_LINE_FONT, CMD_LINE_PLCHLDR_FONT, CONSOLE_PANEL);
        CMD_LINE.SetNormalisedPosition(0f, 1f);
        CMD_LINE.SetWidth(CONSOLE_PANEL.GetPixelWidth());
        CMD_LINE.SetHeight(32);
    }

    private static void LoadFonts(){
        Image fontImage = ResourceLoader.Load("/main/java/resources/fonts/OpenSans_61_DF.png", Image.class, false);
        Texture texture = new Texture().UploadTexture(fontImage, 0);
        File fontFile = ResourceLoader.Load("/main/java/resources/fonts/OpenSans_61_DF.fnt", File.class);
        CMD_LINE_FONT = new Font(0.15f, texture, fontFile);
        
        CMD_LINE_PLCHLDR_FONT = new Font(0.1f, texture, fontFile);
    }

    private static String[] GetArguments(String string){
        List<String> args = new ArrayList<>();

        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(string);
        while(m.find()){
            args.add(m.group(1).replace("\"", ""));
        }

        return args.toArray(new String[args.size()]);
    }

    private static void AddOutput(String output){

        Output.add(output);
    }

    private static void AddOutput(String[] output){
        if(output == null) return;

        for (String s : output){
            AddOutput(s);
        }
    }

    private static String GetOutputString(){
        String s = "";

        for(String out : Output){
            s += out + "\n";
        }

        return s;
    }

}
