package main.java.org.bearengine.objects;

import java.util.List;

/**
 * Created by Stuart on 19/10/2016.
 */
public class GameObject_V2 {

    private List<Component> components;

    public <T> T GetComponent(Class<T> type){
        for (Component comp : components){
            if(comp instanceof Component)
                return type.cast(comp);
        }
        return null;
    }

}
