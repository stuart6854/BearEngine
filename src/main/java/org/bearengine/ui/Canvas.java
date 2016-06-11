package main.java.org.bearengine.ui;

import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.rendering.Renderer;

/**
 * Created by Stuart on 10/06/2016.
 */
public class Canvas extends UIObject{

    public enum RenderMode{ ScreenSpace, WorldSpace }
    public RenderMode renderMode;

    public Canvas(){
        this(RenderMode.ScreenSpace);
    }

    public Canvas(RenderMode renderMode){
        super();
        this.renderMode = renderMode;
        if(renderMode == RenderMode.ScreenSpace){
            this.Width = Display.mainDisplay.getWidth();
            this.Height = Display.mainDisplay.getHeight();
        }

        Renderer.RegisterUICanvas(this);
    }

    @Override
    public void BuildMesh() {
        return;
    }

    @Override
    public boolean equals(Object obj) {
        Canvas canvas = (Canvas)obj;

        if(canvas.Name != this.Name) return false;

        if(canvas.renderMode != this.renderMode) return false;

        if(canvas.Children != this.Children) return false;

        if(canvas.Children.size() != this.Children.size()) return false;

        for(int i = 0; i < Children.size(); i++){
            if(!canvas.Children.get(i).equals(this.Children.get(i))){
                return false;
            }
        }

        return true;
    }

}
