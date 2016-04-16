package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.*;
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
    public void addedToEngine(Engine engine) {
        clouds = engine.getEntitiesFor(
                Family.all(PositionComponent.class, BackgroundComponent.class, VelocityComponent.class)
                        .one(SpriteComponent.class, AnimationComponent.class).get());
    }


    @Override
    public void update(float deltaTime) {
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(Statics.asset.getTexture(Textures.SKY), 0, 0);
        PositionComponent pc;
        SpriteComponent sc;
        VelocityComponent vc;
        for (Entity cloud : clouds) {
            pc = ComponentMappers.position.get(cloud);
            sc = ComponentMappers.sprite.get(cloud);
            vc = ComponentMappers.velocity.get(cloud);
            System.out.println(vc.x);
            pc.mulAdd(vc, deltaTime);

            batch.draw(sc.textureRegion, pc.x, pc.y);
            if (!isCloudInBounds(pc, sc)) {
                Statics.ashley.removeEntity(cloud);
            }
        }
        batch.end();
        System.out.println(clouds.size());
    }

    private boolean isCloudInBounds(PositionComponent pc, SpriteComponent sc) {
        return  pc.x <= Gdx.graphics.getWidth() &&
                pc.y <= Gdx.graphics.getHeight() &&
                pc.x + sc.textureRegion.getRegionWidth() >= 0 &&
                pc.y + sc.textureRegion.getRegionHeight() >= 0;
    }
}
