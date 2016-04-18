package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Furuha on 28.01.2016.
 */
public class PlayerComponent implements Component, Pool.Poolable {

    public String name;
    public int maxShiftCount;
    public float shiftTime;
    public float maxShiftTime;
    public boolean isTransformed = false;
    public boolean invertedGravity = false;

    @Override
    public void reset() {
        maxShiftCount = 0;
        shiftTime = 0;
        maxShiftTime = 0;
        isTransformed = false;
        invertedGravity = false;
        name = null;
    }
}
