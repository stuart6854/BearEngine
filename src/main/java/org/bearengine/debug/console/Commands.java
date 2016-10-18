package main.java.org.bearengine.debug.console;

import main.java.org.bearengine.core.Engine;
import main.java.org.bearengine.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1622565 on 05/10/2016.
 */
public class Commands {

    private List<Command> COMMANDS = new ArrayList<>();

    public void InitialiseCommands(){
        COMMANDS.add(new Command(new String[]{ "help", "?", "cmds" }, new String[]{ "command_alias" }, "Prints all Command Aliases if not passed a parameter. When passed a Command Alias as a parameter, the Commands Help text is printed to screen", args -> {
            if(args.length == 0 || args == null) {
                String[] output = new String[GetCommandCount() + 1];
                output[0] = "Commands";
                int i = 1;
                for (Command cmd : COMMANDS) {
                    output[i] = AliasesToString(cmd.CommandAliases);
                    i++;
                }

                return output;
            }else{
                String[] output = new String[1];
                output[0] = "help -> " + args[0] + " -> " + FindCommand(args[0]).CommandHelp;
                return output;
            }
        }));

        COMMANDS.add(new Command(new String[]{ "print", "log" }, new String[]{ "output" }, "Prints String to Console", args -> {
            if(args == null || args.length == 0) return null;

            String[] output = new String[1];

            output[0] = args[0];

            return output;
        }));

        COMMANDS.add(new Command(new String[]{ "exit" }, null, "Tells the Engine to Terminate/Exit the Game", args -> {
            Engine.Instance.Exit();
            return null;
        }));

        COMMANDS.add(new Command(new String[]{ "gameobject" }, new String[]{ "name" }, "Print a GameObjects Positition, Rotation and Scale", args -> {
            if(args == null || args.length == 0) return null;

            String[] output = new String[1];

            GameObject obj = GameObject.FindGameObject(args[0]);

            if(obj == null){
                output[0] = "No GameObject with that name Found!";
                return output;
            }

            output[0] = obj.toString();

            return output;
        }));

    }

    public Command FindCommand(String wantedAlias){
        for(Command cmd : COMMANDS){
            for(String alias : cmd.CommandAliases){
                if(alias.equals(wantedAlias))
                    return cmd;
            }
        }

        return null;
    }

    public int GetCommandCount(){
        return COMMANDS.size();
    }

    private String AliasesToString(String[] aliases){
        String output = "";
        for(String alias : aliases){
            output += alias + "|";
        }

        return output.substring(0, output.lastIndexOf('|'));
    }

}


