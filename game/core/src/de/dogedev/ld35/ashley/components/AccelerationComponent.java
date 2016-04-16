package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by meisterfuu on 16.04.2016.
 */
public class AccelerationComponent extends  Vector2 implements Component, Pool.Poolable {

    public float maxVelocityX = -1;

    @Override
    public void reset() {
        maxVelocityX = -1;
        x = 0;
        y = 0;
    }
}
