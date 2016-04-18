package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.*;
import de.dogedev.ld35.assets.ParticlePool;
import de.dogedev.ld35.assets.enums.Sounds;

/**
 * Created by Furuha on 28.01.2016.
 */
public class ControllSystem extends EntitySystem {


    private ImmutableArray<Entity> entities;

    public ControllSystem() {
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PlayerComponent.class, AccelerationComponent.class, VelocityComponent.class, PositionComponent.class).get());
    }

    @Override
    public void removedFromEngine (Engine engine) {
    }
    @Override
    public void update (float deltaTime) {

        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);
            AccelerationComponent acceleration = ComponentMappers.acceleration.get(e);
            VelocityComponent velocity = ComponentMappers.velocity.get(e);
            PositionComponent pc = ComponentMappers.position.get(e);
            AnimationComponent ac = ComponentMappers.animation.get(e);
            SizeComponent sc = ComponentMappers.size.get(e);
            PlayerComponent player = ComponentMappers.player.get(e);


            //Checks and resets
            if(player.isTransformed){
                player.shiftTime += deltaTime;
                if(player.shiftTime  >= player.maxShiftTime){
                    sc.height = 2;
                    if(player.invertedGravity){
                        pc.y -= 17;
                    }
                    player.isTransformed = false;
                    player.shiftTime = 0;
                    Statics.particle.createParticleAt(ParticlePool.ParticleType.TRANSFORM, pc.x+8, pc.y+16);
                }
            }
            if(pc.isStanding){
                if(player.isTransformed){

                }else {
                    ac.currentAnimation = ac.idleAnimation;
                }
            }
            acceleration.x = 0;
            acceleration.y = 0;

            //Shift controls
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if(!player.isTransformed && player.maxShiftCount > 0){
                    sc.height = 1;
                    player.isTransformed = true;
                    player.maxShiftCount -= 1;
                    ac.currentAnimation = ac.chickenWalkLeft;
                    Statics.asset.getSound(Sounds.CHICKEN).play();
                    Statics.particle.createParticleAt(ParticlePool.ParticleType.TRANSFORM, pc.x+8, pc.y+16);
                }
            }

            //Movement controls
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                acceleration.x = -5;
                if(velocity.x > 0) {
                    if(!player.isTransformed){
                        ac.currentAnimation = ac.walkRightAnimation;
                    } else {
                        ac.currentAnimation = ac.chickenWalkRight;
                    }
                } else if(velocity.x < 0){
                    if(!player.isTransformed){
                        ac.currentAnimation = ac.walkLeftAnimation;
                    } else {
                        ac.currentAnimation = ac.chickenWalkLeft;
                    }
                }
                if(velocity.x > 1){
                    Statics.particle.createParticleAt(ParticlePool.ParticleType.DUST_R, pc.x, pc.y);
                    velocity.x = 1;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                acceleration.x = 5;
                if(velocity.x > 0) {
                    if(!player.isTransformed){
                        ac.currentAnimation = ac.walkRightAnimation;
                    } else {
                        ac.currentAnimation = ac.chickenWalkRight;
                    }
                } else if(velocity.x < 0){
                    if(!player.isTransformed){
                        ac.currentAnimation = ac.walkLeftAnimation;
                    } else {
                        ac.currentAnimation = ac.chickenWalkLeft;
                    }
                }
                if(velocity.x < -1){
                    Statics.particle.createParticleAt(ParticlePool.ParticleType.DUST_L, pc.x, pc.y);
                    velocity.x = -1;
                }
            }

            //Jump controls
            pc.lastJumpDelta += deltaTime;
            if(pc.lastJumpDelta > 0.45f){
                if (!pc.isStanding && pc.wallLeft && (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    if(!player.isTransformed){
                        ac.currentAnimationTime = 0;
                        ac.currentAnimation = ac.jumpAnimation;
                    }
                    acceleration.y = 300;
                    acceleration.x = 200;
                    Statics.sound.playSound(Sounds.JUMP);
                    pc.lastJumpDelta = 0;
                } else
                if (!pc.isStanding && pc.wallRight && (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    if(!player.isTransformed){
                        ac.currentAnimationTime = 0;
                        ac.currentAnimation = ac.jumpAnimation;
                    }
                    acceleration.y = 300;
                    acceleration.x = -200;
                    Statics.sound.playSound(Sounds.JUMP);
                    pc.lastJumpDelta = 0;
                } else
                if (pc.isStanding && (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    if(!player.isTransformed){
                        ac.currentAnimationTime = 0;
                        ac.currentAnimation = ac.jumpAnimation;
                    }
                    acceleration.y = 250;
                    Statics.sound.playSound(Sounds.JUMP);
                    pc.lastJumpDelta = 0;
                }
            }

            if(player.invertedGravity){
                acceleration.y = -1*acceleration.y;
            }

            //Don't slide
            if(pc.isStanding && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.D) && ! Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                acceleration.x = 0;
                velocity.x *= 0.3;
            }

        }
    }
}
