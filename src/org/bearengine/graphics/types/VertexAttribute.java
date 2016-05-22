package org.bearengine.graphics.types;

/**
 * Created by Stuart on 20/05/2016.
 */
public class VertexAttribute {

    public final int Location;

    public final String Name;

    public final int NumOfDataComponents;

    public VertexAttribute(int location, String name, int numOfDataComponents){
        this.Location = location;
        this.Name = name;
        this.NumOfDataComponents = numOfDataComponents;
    }

}
