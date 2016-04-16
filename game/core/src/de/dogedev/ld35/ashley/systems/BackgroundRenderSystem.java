package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.AnimationComponent;
import de.dogedev.ld35.ashley.components.BackgroundComponent;
import de.dogedev.ld35.ashley.components.PositionComponent;
import de.dogedev.ld35.ashley.components.SpriteComponent;
import de.dogedev.ld35.assets.enums.Textures;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.systems
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public class BackgroundRenderSystem extends EntitySystem {

    private OrthographicCamera camera;
    private Batch batch;
    private ImmutableArray<Entity> clouds;

    public BackgroundRenderSystem(OrthographicCamera camera) {
        batch = new SpriteBatch();
        this.camera = camera;
    }

    @Override
    public void addedToEngine (Engine engine) {
        clouds = engine.getEntitiesFor(
            Family.all(PositionComponent.class, BackgroundComponent.class)
                .one(SpriteComponent.class, AnimationComponent.class).get());
    }


    @Override
    public void update(float deltaTime) {
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(Statics.asset.getTexture(Textures.SKY), 0, 0);
        PositionComponent pc;
        SpriteComponent sc;
        for(Entity cloud : clouds) {
            pc = ComponentMappers.position.get(cloud);
            sc = ComponentMappers.sprite.get(cloud);
            batch.draw(sc.textureRegion, pc.x, pc.y);
        }
        batch.end();

    }
}
