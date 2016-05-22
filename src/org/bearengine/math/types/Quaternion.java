package org.bearengine.math.types;

/**
 * Created by Stuart on 22/05/2016.
 */
public class Quaternion {

    public float x;
    public float y;
    public float z;
    public float w;

    public Quaternion(float x, float y, float z, float w)
    {
        set(x, y, z, w);
    }

    public Quaternion(){
        set(0, 0, 0, 1);
    }

    public Quaternion set(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        return this;
    }

    public Quaternion set(float pitch, float yaw, float roll){
        pitch = (float)Math.toRadians(pitch) * .5f;
        yaw = (float)Math.toRadians(yaw) * .5f;
        roll = (float)Math.toRadians(roll) * .5f;

        float sinP = (float)Math.sin(pitch);
        float sinY = (float)Math.sin(yaw);
        float sinR = (float)Math.sin(roll);
        float cosP = (float)Math.cos(pitch);
        float cosY = (float)Math.cos(yaw);
        float cosR = (float)Math.cos(roll);

        x = sinP * cosY * cosR - cosP * sinY * sinR;
        y = cosP * sinY * cosR + sinP * cosY * sinR;
        z = cosP * cosY * sinR - sinP * sinY * cosR;
        w = cosP * cosY * cosR + sinP * sinY * sinR;

        return this;
    }

    public Quaternion add(Quaternion q){
        return add(q.x, q.y, q.z, q.w);
    }

    public Quaternion add(float x, float y, float z, float w){
        return copy().addSelf(x, y, z, w);
    }

    public Quaternion addSelf(Quaternion q){
        return addSelf(q.x, q.y, q.z, q.w);
    }

    public Quaternion addSelf(float x, float y, float z, float w){
        return set(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Quaternion copy(){
        return new Quaternion(x, y, z, w);
    }

}
