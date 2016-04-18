package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.AccelerationComponent;
import de.dogedev.ld35.ashley.components.PlayerComponent;
import de.dogedev.ld35.ashley.components.PositionComponent;
import de.dogedev.ld35.ashley.components.VelocityComponent;
import de.dogedev.ld35.overlays.DebugOverlay;

/**
 * Created by Furuha on 28.01.2016.
 */
public class AccelerationSystem extends EntitySystem  {


    private ImmutableArray<Entity> entities;

    private Vector2 gravityChicken;
    private Vector2 gravity;
    private Vector2 temp;

    public AccelerationSystem() {
        gravity = new Vector2(0, -9.81f);
        gravityChicken = new Vector2(0, -9.81f);
        temp = new Vector2();
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(AccelerationComponent.class, VelocityComponent.class).get());
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
            PlayerComponent player = ComponentMappers.player.get(e);

            if(!player.invertedGravity){
                if(player.isTransformed){
                    acceleration.add(gravityChicken);
                } else {
                    acceleration.add(gravity);
                }
            } else {
                if(player.isTransformed){
                    acceleration.sub(gravityChicken);
                } else {
                    acceleration.sub(gravity);
                }
            }


            velocity.add(temp.set(acceleration).scl(deltaTime));

            if(acceleration.y > 0){
                DebugOverlay.console.log("Jump " + acceleration.toString());
            }



            if(player.invertedGravity){
                if(player.isTransformed){
                    velocity.y = MathUtils.clamp(velocity.y, Float.MIN_VALUE, 0.8f);
                }
            } else {
                if(player.isTransformed){
                    velocity.y = MathUtils.clamp(velocity.y, -0.8f, Float.MAX_VALUE);
                }
            }

            if(acceleration.maxVelocityX != -1){
                velocity.x = MathUtils.clamp(velocity.x, -acceleration.maxVelocityX, acceleration.maxVelocityX);
            }

            PositionComponent position = ComponentMappers.position.get(e);
            if(position != null && position.isStanding && velocity.x != 0){
                acceleration.x *= 0.2f;
            }

        }

    }
}
