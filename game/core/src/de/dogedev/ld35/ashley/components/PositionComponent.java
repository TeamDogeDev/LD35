package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by meisterfuu on 16.04.2016.
 */
public class PositionComponent implements Component, Pool.Poolable {

    public float x;
    public float y;
    public float z;

    @Override
    public void reset() {
        x = 0;
        y = 0;
        z = 0;
    }
}
