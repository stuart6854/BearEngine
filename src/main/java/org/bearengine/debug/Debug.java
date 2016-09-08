package main.java.org.bearengine.debug;

public class Debug {

    public static DebugDraw DEBUGDRAW = new DebugDraw();

	public static void log(Object output){ //TODO: Allow switching debug messages off
		if(output.toString() != "")
			System.out.println("[DEBUG] " + output);
		else
			System.out.println("");
	}
	
	public static void warn(Object output){
		System.out.println("[WARN] " + output);
	}
	
	public static void error(Object output){
		System.out.println("[ERROR] " + output);
	}

    public static void exception(String msg){
        System.out.println("[EXCEPTION] " + msg);
    }

	public static void exception(String src, Exception e){
		System.out.println("[EXCEPTION] " + src + " - " + e.toString());
        e.printStackTrace();
	}

}
