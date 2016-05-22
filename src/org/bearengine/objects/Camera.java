package org.bearengine.objects;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Camera extends Object{

    public static Camera Main_Camera;

    public Camera(){
        super();

        if(Main_Camera == null) Main_Camera = this;
    }

    public void SetAsMain(){
        Main_Camera = this;
    }

}
