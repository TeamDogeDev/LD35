package de.dogedev.ld35;

import com.badlogic.ashley.core.PooledEngine;

/**
 * Created by Furuha on 28.01.2016.
 */
public class Statics {

    public static PooledEngine ashley;

    public static void initCat(){
        ashley = new PooledEngine();
    }
}
