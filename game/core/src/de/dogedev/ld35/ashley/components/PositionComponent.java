package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by meisterfuu on 16.04.2016.
 */
public class PositionComponent extends Vector2 implements Component, Pool.Poolable {

    public float z;
    public boolean isStanding = false;
    public boolean wallLeft = false;
    public boolean wallRight = false;
    public float lastJumpDelta = 10;

    @Override
    public void reset() {
        x = 0;
        y = 0;
        z = 0;
    }
}
