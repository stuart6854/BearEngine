package main.java.org.bearengine.physics.boundingvolumes;

import main.java.org.joml.Vector3f;

/**
 * Created by 1622565 on 28/09/2016.
 */
public class AABB {

    private Vector3f Min, Max;
    private Vector3f Sizes;

    public AABB(Vector3f min, Vector3f max, Vector3f sizes){
        this.Min = min;
        this.Max = max;
        this.Sizes = sizes;
    }

    public AABB(){
        Min = new Vector3f();
        Max = new Vector3f();
        Sizes = new Vector3f();
    }

    public Vector3f GetMin() {
        return Min;
    }

    public void SetMin(Vector3f min) {
        Min = min;
        UpdateSize();
    }

    public Vector3f GetMax() {
        return Max;
    }

    public void SetMax(Vector3f max) {
        Max = max;
        UpdateSize();
    }

    public Vector3f GetSizes() {
        return Sizes;
    }

    public void SetSizes(Vector3f sizes) {
        Sizes = sizes;
        UpdateMinMax();
    }

    protected void UpdateSize(){
        this.Sizes.x = (Max.x - Min.x);
        this.Sizes.y = (Max.y - Min.y);
        this.Sizes.z = (Max.z - Min.z);
    }

    protected void UpdateMinMax(){
        Vector3f halfSizes = new Vector3f(Sizes).mul(0.5f);

        Min.set(-halfSizes.x, -halfSizes.y, -halfSizes.z);
        Max.set(halfSizes.x, halfSizes.y, halfSizes.z);
    }

    public boolean checkAABB(AABB aabb){
        if(aabb.Min.x > this.Min.x && aabb.Max.x < this.Max.x)
            return false;
        if(aabb.Min.y > this.Min.y && aabb.Max.y < this.Max.y)
            return false;
        if(aabb.Min.z > this.Min.z && aabb.Max.z < this.Max.z)
            return false;

        return true;
    }

}
