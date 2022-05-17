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
public class Vec2f {
    public float x;
    public float y;
    
    public Vec2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
