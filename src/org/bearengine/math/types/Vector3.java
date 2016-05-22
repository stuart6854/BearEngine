package org.bearengine.math.types;

/**
 * Created by Stuart on 21/05/2016.
 */
public class Vector3 {

    public float x, y, z;

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(Vector3 v){
        this(v.x, v.y, v.z);
    }

    public Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 set(Vector3 v){
        return this.set(v.x, v.y, v.z);
    }

    public Vector3 set(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 add(Vector3 v){
        return this.add(v.x, v.y, v.z);
    }

    public Vector3 add(float x, float y, float z){
        return this.set(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 sub(Vector3 v){
        return this.sub(v.x, v.y, v.z);
    }

    public Vector3 sub(float x, float y, float z){
        return this.set(this.x - x, this.y - y, this.z - z);
    }

    public Vector3 mul(float scalar){
        return  this.mul(scalar, scalar, scalar);
    }

    public Vector3 mul(Vector3 v){
        return this.mul(v.x, v.y, v.z);
    }

    public Vector3 mul(float x, float y, float z){
        return this.set(this.x * x, this.y * y, this.z * z);
    }

    public Vector3 div(float scalar){
        return this.set(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public Vector3 normalise(){
        float mag = this.magnitude();
        return  this.div(mag);
    }

    public float magnitude(){
        double x = Math.pow(this.x, 2);
        double y = Math.pow(this.y, 2);
        double z = Math.pow(this.z, 2);
        return (float)Math.sqrt(x + y + z);
    }

    public float dot(Vector3 v){
        return (x * v.x) + (y * v.y) + (z * v.z);
    }

    public Vector3 cross(Vector3 v){
        float x = y * v.z - z * v.y;
        float y = z * v.x - x * v.z;
        float z = x * v.y - y * v.x;
        return new Vector3(x, y, z);
    }

    public static Vector3 Add(Vector3 a, Vector3 b){
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector3 Sub(Vector3 a, Vector3 b){
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector3 Mul(Vector3 a, Vector3 b){
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z);
    }

}
