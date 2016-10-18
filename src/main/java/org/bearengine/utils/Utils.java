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
    
    public static float[] StringArrayToFloat(String[] strings){
        float[] floats = new float[strings.length];
        
        for(int i = 0; i < strings.length; i++){
            floats[i] = Float.parseFloat(strings[i]);
        }
        
        return floats;
    }
    
    public static int[] StringArrayToInt(String[] strings){
        int[] ints = new int[strings.length];
        
        for(int i = 0; i < strings.length; i++){
            ints[i] = Integer.parseInt(strings[i]);
        }
        
        return ints;
    }

    public static String ArrayToString(String[] array){
        if(array == null)
            return "NULL";

        String s = "";
        for(String string : array){
            s += string + ", ";
        }

        return s.substring(0, s.lastIndexOf(","));
    }

}
