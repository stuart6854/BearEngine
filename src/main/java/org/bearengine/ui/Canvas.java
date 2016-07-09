package main.java.org.bearengine.ui;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.rendering.RenderSpace;
import main.java.org.bearengine.graphics.rendering.Renderer;

/**
 * Created by Stuart on 10/06/2016.
 */
public class Canvas extends UIObject{

    public RenderSpace Render_Space;

    public Canvas(){
        this(RenderSpace.SCREEN_SPACE);
    }

    public Canvas(RenderSpace renderMode){
        super();
        super.Name = "UI_Canvas";
        this.Owner_Canvas = this;
        this.Render_Space = renderMode;
        if(renderMode == RenderSpace.SCREEN_SPACE){
            this.PixelWidth = Display.mainDisplay.getWidth();
            this.PixelHeight = Display.mainDisplay.getHeight();
        }

        Renderer.RegisterUICanvas(this);
    }

    public void Update(){
        update();
    }

    @Override
    public void SetWidth(float width) {
        if(Render_Space == RenderSpace.SCREEN_SPACE)
            Debug.warn("Canvas -> Canvas is in SCREEN_SPACE and You are trying to adjust its Width!");

        super.SetWidth(width);
    }

    @Override
    public void SetHeight(float height) {
        if(Render_Space == RenderSpace.SCREEN_SPACE)
            Debug.warn("Canvas -> Canvas is in SCREEN_SPACE and You are trying to adjust its Height!");

        super.SetHeight(height);
    }

    @Override
    public void SetNormalisedPosition(float x, float y) {
        if(Render_Space == RenderSpace.SCREEN_SPACE)
            Debug.warn("Canvas -> Canvas is in SCREEN_SPACE and You are trying to adjust its NormalisedPosition!");

        super.SetNormalisedPosition(x, y);
    }

    @Override
    public void SetPixelOffset(float x, float y, float z) {
        if(Render_Space == RenderSpace.SCREEN_SPACE)
            Debug.warn("Canvas -> Canvas is in SCREEN_SPACE and You are trying to adjust its PixelOffset!");

        super.SetPixelOffset(x, y, z);
    }

    @Override
    public void BuildMesh() {
        super.BuildMesh();
    }

    @Override
    public boolean equals(Object obj) {
        Canvas canvas = (Canvas)obj;

        if(canvas.Name != this.Name) return false;

        if(canvas.Render_Space != this.Render_Space) return false;

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
