package de.dogedev.ld35.assets;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.assets.enums.Particles;

/**
 * Project: game
 * Package: de.dogedev.ld35.assets
 * Date: 17.04.2016
 *
 * @author elektropapst
 */
public class ParticlePool {

    private Array<ParticleEffectPool.PooledEffect> effects = new Array<>();

    public void removeEffect(ParticleEffectPool.PooledEffect effect, boolean b) {
        effects.removeValue(effect, b);
    }

    public enum ParticleType {
        DUST(new GameEffect(Statics.asset.getParticleEffect(Particles.DUST), 25, 100)),
        DUST_L(new GameEffect(Statics.asset.getParticleEffect(Particles.DUST_L), 25, 100)),
        DUST_R(new GameEffect(Statics.asset.getParticleEffect(Particles.DUST_R), 25, 100)),
        TRANSFORM(new GameEffect(Statics.asset.getParticleEffect(Particles.TRANSFORM), 25, 100));

        final GameEffect gameEffect;

        ParticleType(GameEffect gameEffect) {
            this.gameEffect = gameEffect;
        }

        void dispose() {
            gameEffect.dispose();
        }

    }

    public void createParticleAt(ParticleType particleType, float x, float y) {
        ParticleEffectPool.PooledEffect effect = particleType.gameEffect.pool.obtain();
        effect.reset();
        effect.setPosition(x, y);
        effects.add(effect);
        effect.start();
    }

    public void createParticleAt(ParticleType particleType, float x, float y, float newAngle, float spr) {
        ParticleEffectPool.PooledEffect effect = particleType.gameEffect.pool.obtain();
        effect.reset();
        effect.setPosition(x, y);
        for (ParticleEmitter emitter : effect.getEmitters()) {
            emitter.getAngle().setHigh(newAngle - spr, newAngle + spr);
            emitter.getAngle().setLow(newAngle - spr, newAngle + spr);
        }
        effects.add(effect);
        effect.start();
    }

    public void createParticleAt(ParticleType particleType, float x, float y, float newAngle) {
        createParticleAt(particleType, x, y, newAngle, 0);
    }

    public Array<ParticleEffectPool.PooledEffect> getEffects() {
        return effects;
    }

    public void dispose() {
        for (ParticleType type : ParticleType.values()) {
            type.dispose();
        }
    }

    static class GameEffect implements Disposable {
        ParticleEffect prototype;
        ParticleEffectPool pool;

        public GameEffect(ParticleEffect particleEffect, int initialCapacity, int maxCapacity) {
            this.prototype = particleEffect;
            pool = new ParticleEffectPool(prototype, initialCapacity, maxCapacity);
        }

        @Override
        public void dispose() {
            prototype.dispose();
        }
    }

}
