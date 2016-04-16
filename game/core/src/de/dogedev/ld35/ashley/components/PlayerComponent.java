package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Furuha on 28.01.2016.
 */
public class PlayerComponent implements Component, Pool.Poolable {

    public String name;

    @Override
    public void reset() {
        name = null;
    }
}
