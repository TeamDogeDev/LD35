package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by meisterfuu on 16.04.2016.
 */
public class SizeComponent implements Component, Pool.Poolable {

    public int width = 1;
    public int height = 1;

    @Override
    public void reset() {
        width = 1;
        height = 1;
    }
}
