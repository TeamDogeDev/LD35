package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.components
 * Date: 17.04.2016
 *
 * @author elektropapst
 */
public class TextboxComponent implements Component, Pool.Poolable{

    public String text;
    public float visTime;
    public float elapsedTime;
    public boolean visible;
    public boolean alignRight;
    public boolean lastVisible;

    @Override
    public void reset() {
        text = "";
        visTime = 5;
        elapsedTime = 0;
        visible = false;
        alignRight = false;
    }
}
