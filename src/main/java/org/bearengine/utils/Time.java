package main.java.org.bearengine.utils;

public class Time {
	
	private static double lastLoopTime;
    
    public static int FPS, UPS;

    public static void init() {
        lastLoopTime = getTime();
    }

    public static double getTime() {
        return System.nanoTime() / 1_000_000_000f; //Nano to Sec
    }

    public static float getElapsedTime() {
    	double time = getTime();
    	float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    public static double getLastLoopTime() {
        return lastLoopTime;
    }

    public static double NanToSec(long nano){
        return nano / 1_000_000_000f;
    }

}
