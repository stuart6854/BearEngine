package main.java.org.bearengine.math;

/**
 * Created by Stuart on 21/05/2016.
 */
public class MathUtils {

    public static int Clamp(int val, int min, int max){
        return Math.max(min, Math.min(max, val));
    }

    public static float Clamp(float val, float min, float max){
        return Math.max(min, Math.min(max, val));
    }

    public static double Clamp(double val, double min, double max){
        return Math.max(min, Math.min(max, val));
    }

}
