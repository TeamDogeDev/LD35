package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Furuha on 28.01.2016.
 */
public class AnimationComponent implements Component, Pool.Poolable {

    public Animation currentAnimation;
    public float currentAnimationTime;

    @Override
    public void reset() {
        currentAnimation = null;
        currentAnimationTime = 0;
    }
}