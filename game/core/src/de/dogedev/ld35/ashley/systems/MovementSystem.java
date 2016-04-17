package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.*;
import de.dogedev.ld35.assets.ParticlePool;
import de.dogedev.ld35.assets.enums.Sounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Furuha on 28.01.2016.
 */
public class MovementSystem extends EntitySystem implements EntityListener {


    private final TiledMapTileLayer collisionlayer;
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> keys;
    private ArrayList<Entity> sortedEntities;
    private YComparator comparator = new YComparator();
    private BitmapFont font;
    private float lastStep = 10;

    public MovementSystem(TiledMapTileLayer collisionlayer) {
        this.collisionlayer = collisionlayer;
        sortedEntities = new ArrayList<>();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).exclude(BackgroundComponent.class).get());
        keys = engine.getEntitiesFor(Family.all(PositionComponent.class, KeyComponent.class).exclude(BackgroundComponent.class).get());
        engine.addEntityListener(Family.all(PositionComponent.class, VelocityComponent.class).exclude(BackgroundComponent.class).get(), this);
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

        Collections.sort(sortedEntities, comparator);

        for (int i = 0; i < sortedEntities.size(); ++i) {
            Entity e = sortedEntities.get(i);

            PositionComponent position = ComponentMappers.position.get(e);
            VelocityComponent velocity = ComponentMappers.velocity.get(e);
            SizeComponent size = ComponentMappers.size.get(e);

            int width = 1;
            int height = 1;
            if (size != null) {
                width = size.width;
                height = size.height;
            }

            int yTile = (int) (position.y + velocity.y) / Statics.settings.tileSize;


            //Entity <-> Key Collision
            for (Entity key : keys) {
                PositionComponent keyPc = ComponentMappers.position.get(key);
                if(rectCollides(position.x, position.x+(width*Statics.settings.tileSize), keyPc.x, keyPc.x+Statics.settings.tileSize, width+Statics.settings.tileSize) &&
                    rectCollides(position.y, position.y+(height*Statics.settings.tileSize), keyPc.y, keyPc.y+Statics.settings.tileSize, height+Statics.settings.tileSize)){
                    Statics.ashley.removeEntity(key);
                }
            }

            int xTile = (int) (position.x) / Statics.settings.tileSize;
            int xTile2 = (int) (position.x + Statics.settings.tileSize) / Statics.settings.tileSize;
            if(position.x%Statics.settings.tileSize < 2){
                xTile2 = xTile;
            }

            //Entity Movement <-> Map Collision
            for (int widthStep = 0; widthStep < width; widthStep++) {

                if (velocity.y < 0) {
                    if (collisionlayer != null && (collisionlayer.getCell(xTile, yTile) != null || collisionlayer.getCell(xTile2, yTile) != null)) {
                        if(!position.isStanding) {
                            Statics.particle.createParticleAt(ParticlePool.ParticleType.DUST, position.x, position.y);
                            Statics.sound.playSoundPitched(Sounds.LANDING);
                        }
                        position.isStanding = true;
                        velocity.y = -1 * (position.y - ((yTile + 1) * Statics.settings.tileSize));
                    } else {
                        position.isStanding = false;
                    }
                } else if (velocity.y > 0) {
                    if (collisionlayer != null && (collisionlayer.getCell(xTile, yTile + height) != null || collisionlayer.getCell(xTile2, yTile + height) != null)) {
                        position.isStanding = false;
                        velocity.y = 0;
                    } else {
                        position.isStanding = false;
                    }
                }
                xTile++;
                xTile2++;
            }


            xTile = (int) (position.x + velocity.x) / Statics.settings.tileSize;
            yTile = (int) (position.y + velocity.y) / Statics.settings.tileSize;
            for (int heightStep = 0; heightStep < height; heightStep++) {
                if (velocity.x < 0) {
                    if (collisionlayer != null && collisionlayer.getCell(xTile, yTile) != null) {
                        velocity.x = 0;
                        break;
                    }
                } else if (velocity.x > 0) {
                    if (collisionlayer != null && collisionlayer.getCell(xTile + width, yTile) != null) {
                        velocity.x = 0;
                        break;
                    }
                }
                yTile += 1;
            }
            lastStep += deltaTime;
            if((velocity.x > 1 || velocity.x < -1) && position.isStanding && lastStep > 0.3){
                Statics.sound.playSoundPitched(Sounds.WALK);
                lastStep = 0;
            }


            position.add(velocity);

        }

    }

    private boolean rectCollides(float x1, float y1, float x2, float y2, float size) {
//        checks++;
        if ((x1 - size) < x2 && x2 < (x1 + size) && Math.abs(y2 - y1) < size) {
            return true;
        }
        if ((y1 - size) < y2 && y2 < (y1 + size) && Math.abs(x2 - x1) < size) {
            return true;
        }
        return false;
    }


    private float distance(float i, float y) {
        return Math.abs(i - y);
    }
}
