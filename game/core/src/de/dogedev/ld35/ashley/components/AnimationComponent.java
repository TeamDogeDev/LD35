package de.dogedev.ld35.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Furuha on 28.01.2016.
 */
public class AnimationComponent implements Component, Pool.Poolable {

    public Animation currentAnimation;
    public Animation jumpAnimation;
    public Animation fallAnimation;
    public Animation walkRightAnimation;
    public Animation idleAnimation;
    public float currentAnimationTime;
    public boolean center;
    public Animation walkLeftAnimation;

    @Override
    public void reset() {
        center = false;
        currentAnimation = null;
        currentAnimationTime = 0;
    }
}
