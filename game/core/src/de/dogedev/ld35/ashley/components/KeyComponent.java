package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.components
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public class KeyComponent implements Component, Pool.Poolable {

    public int keyId;

    @Override
    public void reset() {
        keyId = -1; // not usable
    }
}
