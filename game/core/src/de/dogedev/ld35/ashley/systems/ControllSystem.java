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
            sc.height = 2;
            if(pc.isStanding){
                ac.currentAnimation = ac.idleAnimation;
            }

            acceleration.x = 0;
            acceleration.y = 0;

            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                acceleration.x = -5;
                if(velocity.x > 0) {
                    ac.currentAnimation = ac.walkRightAnimation;
                } else if(velocity.x < 0){
                    ac.currentAnimation = ac.walkLeftAnimation;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//                velocity.speed = 10;
//                velocity.direction.x = 0;
//                velocity.direction.y = 0;
                sc.height = 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                acceleration.x = 5;
                if(velocity.x > 0) {
                    ac.currentAnimation = ac.walkRightAnimation;
                } else if(velocity.x < 0){
                    ac.currentAnimation = ac.walkLeftAnimation;
                }
            }
            if (pc.isStanding && (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))) {
                ac.currentAnimationTime = 0;
                ac.currentAnimation = ac.jumpAnimation;
                acceleration.y = 250;
                Statics.sound.playSoundPitched(Sounds.JUMP);
            }

            //Don't slide
            if(pc.isStanding && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.D) && ! Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                acceleration.x = 0;
                velocity.x *= 0.3;
            }

        }
    }
}
