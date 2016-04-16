package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.components
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public class LightComponent implements Component, Pool.Poolable {

    public float x;
    public float y;
    public Color color;

    @Override
    public void reset() {
        x = 0;
        y = 0;
        color = Color.WHITE;
    }
}
