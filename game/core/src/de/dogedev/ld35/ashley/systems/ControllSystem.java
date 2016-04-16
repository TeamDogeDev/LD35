package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.PlayerComponent;
import de.dogedev.ld35.ashley.components.VelocityComponent;

/**
 * Created by Furuha on 28.01.2016.
 */
public class ControllSystem extends EntitySystem {


    private ImmutableArray<Entity> entities;

    public ControllSystem() {
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PlayerComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void removedFromEngine (Engine engine) {
    }
    @Override
    public void update (float deltaTime) {

        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);
            VelocityComponent velocity = ComponentMappers.velocity.get(e);

            velocity.x = 0;
            velocity.y = 0;

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//                velocity.speed = 10;
//                velocity.direction.x = 0;
//                velocity.direction.y = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocity.x = -10;
                velocity.y = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//                velocity.speed = 10;
//                velocity.direction.x = 0;
//                velocity.direction.y = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.x = 10;
                velocity.y = 0;
            }

        }
    }
}
