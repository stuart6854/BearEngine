package main.java.org.bearengine.debug.console;

/**
 * Created by 1622565 on 05/10/2016.
 */
public class Command {

    public String[] CommandAliases;
    public String[] Args;
    public String CommandHelp;
    public CommandMethod CommandMethod;

    public Command(String[] cmdAliases, String[] args, String cmdHelp, CommandMethod method){
        this.CommandAliases = cmdAliases;
        this.Args = args;
        this.CommandHelp = cmdHelp;
        this.CommandMethod = method;
    }

}
