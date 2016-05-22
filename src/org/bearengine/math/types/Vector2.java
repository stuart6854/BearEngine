package org.bearengine.math.types;

/**
 * Created by Stuart on 21/05/2016.
 */
public class Vector2 {

    public float x, y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(Vector2 v){
        this(v.x, v.y);
    }

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2 set(Vector2 v){
        return this.set(v.x, v.y);
    }

    public Vector2 set(float x, float y){
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 add(Vector2 v){
        return this.add(v.x, v.y);
    }

    public Vector2 add(float x, float y){
        return this.set(this.x + x, this.y + y);
    }

    public Vector2 sub(Vector2 v){
        return this.sub(v.x, v.y);
    }

    public Vector2 sub(float x, float y){
        return this.set(this.x - x, this.y - y);
    }

    public Vector2 mul(float scalar){
        return  this.mul(scalar, scalar);
    }

    public Vector2 mul(Vector2 v){
        return this.mul(v.x, v.y);
    }

    public Vector2 mul(float x, float y){
        return this.set(this.x * x, this.y * y);
    }

    public Vector2 div(float scalar){
        return this.set(this.x / scalar, this.y / scalar);
    }

    public double magnitude(){
        double x = Math.pow(this.x, 2);
        double y = Math.pow(this.y, 2);
        return Math.sqrt(x + y);
    }

    public float dot(Vector2 v){
        return (x * v.x) + (y * v.y);
    }

    public static Vector2 Add(Vector2 a, Vector2 b){
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    public static Vector2 Sub(Vector2 a, Vector2 b){
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    public static Vector2 Mul(Vector2 a, Vector2 b){
        return new Vector2(a.x * b.x, a.y * b.y);
    }

}
