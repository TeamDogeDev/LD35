package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Furuha on 28.01.2016.
 */
public class SpriteComponent implements Component, Pool.Poolable {

    public TextureRegion textureRegion;
    public boolean center;

    @Override
    public void reset() {
        center = false;
        textureRegion = null;
    }
}
