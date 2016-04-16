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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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

    private static final int MAXCLOUDS = 30;

    public BackgroundRenderSystem(OrthographicCamera camera) {
        batch = new SpriteBatch();
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        clouds = engine.getEntitiesFor(
                Family.all(PositionComponent.class, BackgroundComponent.class, VelocityComponent.class)
                        .one(SpriteComponent.class, AnimationComponent.class).get());
        // initial clouds
        do {
            spawnRandomCloud();
        } while(clouds.size() < MAXCLOUDS);
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
            pc.mulAdd(vc, deltaTime);

            batch.draw(sc.textureRegion, pc.x, pc.y, sc.textureRegion.getRegionWidth()*pc.z,sc.textureRegion.getRegionHeight()*pc.z);
            if (!isCloudInBounds(pc, sc)) {
                Statics.ashley.removeEntity(cloud); // TODO RECYCLE
                spawnNewCloud();
            }
        }
        batch.end();
    }

    private void spawnNewCloud() {
        spawnCloud(Gdx.graphics.getWidth(), MathUtils.random(0, Gdx.graphics.getHeight()), MathUtils.random(0.2f, 1f));
    }
    private void spawnRandomCloud() {
        spawnCloud(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()), MathUtils.random(0.2f, 1f));
    }

    private void spawnCloud(int x, int y, float z) {
        Entity entity = Statics.ashley.createEntity();
        SpriteComponent sc = Statics.ashley.createComponent(SpriteComponent.class);
        sc.textureRegion = new TextureRegion(Statics.asset.getTexture(Textures.CLOUD));
        entity.add(sc);
        PositionComponent pc = Statics.ashley.createComponent(PositionComponent.class);
        // pc.x = Gdx.graphics.getWidth();
        pc.x = x;
        pc.y = y;
        pc.z = z;
        entity.add(pc);
        VelocityComponent vc = Statics.ashley.createComponent(VelocityComponent.class);
        vc.x = -z*16;
        vc.y = 0;
        entity.add(vc);
        entity.add(Statics.ashley.createComponent(BackgroundComponent.class));
        Statics.ashley.addEntity(entity);
    }

    private boolean isCloudInBounds(PositionComponent pc, SpriteComponent sc) {
        return  pc.x <= Gdx.graphics.getWidth() &&
                pc.y <= Gdx.graphics.getHeight() &&
                pc.x + sc.textureRegion.getRegionWidth() >= 0 &&
                pc.y + sc.textureRegion.getRegionHeight() >= 0;
    }
}
