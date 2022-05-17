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
public class Vec3i {
    public int x;
    public int y;
    public int z;
    
    public Vec3i(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int get(int a){
        if(a == 0){
            return x;
        }else if(a == 1){
            return y;
        }else if(a == 2){
            return z;
        }
        return 0;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }    
}
