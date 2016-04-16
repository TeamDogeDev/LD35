package de.dogedev.ld35;

import com.badlogic.ashley.core.PooledEngine;
import de.dogedev.ld35.assets.AssetLoader;

/**
 * Created by Furuha on 28.01.2016.
 */
public class Statics {

    public static PooledEngine ashley;
    public static AssetLoader asset;

    public static void initCat(){
        asset = new AssetLoader();
        ashley = new PooledEngine();
    }
}
