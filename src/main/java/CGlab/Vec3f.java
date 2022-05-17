/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CGlab;

/**
 *
 * @author nx
 */
public class Vec3f {
    public float x;
    public float y;
    public float z;
    
    public Vec3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }
    
    

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }    
}
