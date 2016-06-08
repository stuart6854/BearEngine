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

    public static float Normalise(float value, float min, float max){
        float normalisedVal = (value - min) / (max - min);
        return normalisedVal;
    }

    public static float DeNormalise(float normalisedVal, float min, float max){
        float val = (normalisedVal * (max - min) + min);
        return val;
    }

}
