package main.java.org.bearengine.utils;

import java.util.List;

/**
 * Created by Stuart on 08/09/2016.
 */
public class Utils {
    
    public static float[] ToArrayFloat(List<Float> list){
        float[] array = new float[list.size()];
        int i = 0;
        for(float x : list){
            array[i] = x;
            i++;
        }
        
        return array;
    }
    
    public static int[] ToArrayInt(List<Integer> list){
        int[] array = new int[list.size()];
        int i = 0;
        for(int x : list){
            array[i] = x;
            i++;
        }
        
        return array;
    }
    
}
