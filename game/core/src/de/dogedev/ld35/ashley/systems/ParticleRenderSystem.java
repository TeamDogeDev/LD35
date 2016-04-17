package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.dogedev.ld35.Statics;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.systems
 * Date: 17.04.2016
 *
 * @author elektropapst
 */
public class ParticleRenderSystem extends EntitySystem {

    private final OrthographicCamera camera;
    private Batch effectBatch;

    public ParticleRenderSystem(OrthographicCamera camera) {
        this(0, camera);
    }

    public ParticleRenderSystem(int priority, OrthographicCamera camera) {
        super(priority);
        this.camera = camera;
        effectBatch = new SpriteBatch();
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        effectBatch.begin();
        effectBatch.setProjectionMatrix(camera.combined);
        for(ParticleEffectPool.PooledEffect effect : Statics.particle.getEffects()) {
            effect.draw(effectBatch, deltaTime);
            if(effect.isComplete()) {
                Statics.particle.removeEffect(effect, true);
                effect.free();
            }
        }

        effectBatch.end();
    }
}
