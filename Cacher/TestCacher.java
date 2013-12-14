/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Cacher;

/**
 *
 * @author matth_000
 */
public class TestCacher {
    
    public static void main(String [] args) {
        
        System.out.println("Creating Logger and Cacher");
        Logger logger = new Logger();
        Cacher cache = new Cacher(logger,1);
        System.out.println("\t--: Created!");
        System.out.println();
        
        String name = "n1";
        byte[] b = new byte[1];
        b[0] = 0;
        
        String name2 = "n2";
        byte[] b2 = new byte[1];
        b2[0] = 1;
        
        System.out.println("Testing Cache Request...");
        System.out.println(cache.cacheRequest(name));
        System.out.println(cache.cacheRequest(name2));
        System.out.println();
        
        System.out.println("Adding page to Cache...");
        cache.cacheAdd(name, b);
        cache.cacheAdd(name2, b2);
        System.out.println();
        
        System.out.println("Re-trying Cache Request...");
        System.out.println(cache.cacheRequest(name));
        System.out.println(cache.cacheRequest(name2));
        System.out.println();
        
        System.out.println("Test Complete.");
        
    }
    
}
