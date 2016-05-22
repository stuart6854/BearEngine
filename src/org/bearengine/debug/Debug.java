package org.bearengine.debug;

public class Debug {
	
	public static void log(Object output){
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

	public static void exception(Object output){
		System.out.println("[EXCEPTION] " + output);
	}

}
