package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Furuha on 28.01.2016.
 */
public class EntityRenderSystem extends EntitySystem implements EntityListener {


    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private ImmutableArray<Entity> entities;
    private ArrayList<Entity> sortedEntities;
    private YComparator comparator = new YComparator();
    private BitmapFont font;

    public EntityRenderSystem(OrthographicCamera camera) {
        this(0, camera);
    }

    public EntityRenderSystem(int priority, OrthographicCamera camera) {
        super(priority);
        this.camera = camera;
        batch = new SpriteBatch();
        sortedEntities = new ArrayList<>();
        font = new BitmapFont();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class).one(SpriteComponent.class, AnimationComponent.class)
                .exclude(BackgroundComponent.class, KeyComponent.class, GravityComponent.class).get());
        engine.addEntityListener(Family.all(PositionComponent.class).one(SpriteComponent.class, AnimationComponent.class)
                .exclude(BackgroundComponent.class, KeyComponent.class, GravityComponent.class).get(), this);
        for (Entity e : entities) {
            sortedEntities.add(e);
        }
        Collections.sort(sortedEntities, comparator);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(this);
    }

    private class YComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
            return Float.compare(ComponentMappers.position.get(e2).y, ComponentMappers.position.get(e1).y);
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        sortedEntities.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        sortedEntities.remove(entity);
    }

    @Override
    public void update(float deltaTime) {

        PositionComponent position;
        PlayerComponent player;

        Collections.sort(sortedEntities, comparator);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < sortedEntities.size(); ++i) {
            Entity e = sortedEntities.get(i);
            float xOffset = 0;
            position = ComponentMappers.position.get(e);
            player = ComponentMappers.player.get(e);
            boolean flip = false;
            if (player != null && player.invertedGravity) {
                flip = true;
            }
            if (ComponentMappers.animation.has(e)) {
                AnimationComponent ac = ComponentMappers.animation.get(e);
                TextureRegion region = ac.currentAnimation.getKeyFrame(ac.currentAnimationTime);
                if (ac.center) {
                    xOffset = region.getRegionWidth() / 2;
                }
                if (flip && !region.isFlipY()) {
                    region.flip(false, true);
                } else if (!flip & region.isFlipY()) {
                    region.flip(false, true);
                }
                batch.draw(region, position.x - xOffset, position.y + position.z);
                ac.currentAnimationTime += deltaTime;
            } else {
                SpriteComponent visual = ComponentMappers.sprite.get(e);
                if (visual.center) {
                    xOffset = visual.textureRegion.getRegionWidth() / 2;
                }
                if (flip && !visual.textureRegion.isFlipY()) {
                    visual.textureRegion.flip(false, true);
                } else if (!flip & visual.textureRegion.isFlipY()) {
                    visual.textureRegion.flip(false, true);
                }

                batch.draw(visual.textureRegion, position.x - xOffset, position.y + position.z);
            }

            NameComponent nc = ComponentMappers.name.get(e);
            if (nc != null) {
                font.draw(batch, nc.name, position.x - 90, position.y + position.z + 40, 200, Align.center, false);
            }

        }

        batch.end();
    }
}
