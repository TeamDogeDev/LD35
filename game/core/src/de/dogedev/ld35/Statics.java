package de.dogedev.ld35;

import com.badlogic.ashley.core.PooledEngine;
import de.dogedev.ld35.assets.AssetLoader;
import de.dogedev.ld35.assets.ParticlePool;
import de.dogedev.ld35.assets.SoundManager;
import de.dogedev.ld35.assets.enums.GameSettings;

/**
 * Created by Furuha on 28.01.2016.
 */
public class Statics {

    public static PooledEngine ashley;
    public static AssetLoader asset;
    public static ParticlePool particle;
    public static GameSettings settings;
    public static SoundManager sound;

    public static void initCat(){
        settings = new GameSettings();
        asset = new AssetLoader();
        ashley = new PooledEngine();
        particle = new ParticlePool();
        sound = new SoundManager();
    }
}
