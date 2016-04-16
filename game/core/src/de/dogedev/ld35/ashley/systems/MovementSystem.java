package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.PositionComponent;
import de.dogedev.ld35.ashley.components.VelocityComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Furuha on 28.01.2016.
 */
public class MovementSystem extends EntitySystem implements EntityListener {


    private final TiledMapTileLayer collisionlayer;
    private ImmutableArray<Entity> entities;
    private ArrayList<Entity> sortedEntities;
    private YComparator comparator = new YComparator();
    private BitmapFont font;

    public MovementSystem(TiledMapTileLayer collisionlayer) {
        this.collisionlayer = collisionlayer;
        sortedEntities = new ArrayList<>();
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
        engine.addEntityListener(Family.all(PositionComponent.class, VelocityComponent.class).get(), this);
        for(Entity e: entities){
            sortedEntities.add(e);
        }
        Collections.sort(sortedEntities, comparator);
    }

    @Override
    public void removedFromEngine (Engine engine) {
        engine.removeEntityListener(this);
    }

    private class YComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
            return Float.compare(ComponentMappers.position.get(e2).y, ComponentMappers.position.get(e1).y);
        }
    }

    @Override
    public void entityAdded (Entity entity) {
        sortedEntities.add(entity);
    }

    @Override
    public void entityRemoved (Entity entity) {
        sortedEntities.remove(entity);
    }

    @Override
    public void update (float deltaTime) {


        Collections.sort(sortedEntities, comparator);

        for (int i = 0; i < sortedEntities.size(); ++i) {
            Entity e = sortedEntities.get(i);

            PositionComponent position = ComponentMappers.position.get(e);
            VelocityComponent velocity = ComponentMappers.velocity.get(e);


            int xTile = (int)((position.x+velocity.x))/16;
            int yTile = (int)((position.y+velocity.y)-1)/16;
            if(collisionlayer != null && collisionlayer.getCell(xTile, yTile) != null){
                position.isStanding = true;
                velocity.y = -1*(position.y-((yTile+1)*16));
            } else {
                position.isStanding = false;
            }

            yTile = (int)(position.y+velocity.y)/16;
            if(velocity.x < 0){
                if(collisionlayer != null && collisionlayer.getCell(xTile, yTile) != null){
//                    velocity.x = position.x-(xTile*16+16);
                    velocity.x = -1*(position.x-((xTile+1)*16));
                }
            } else if(velocity.x > 0){
                if(collisionlayer != null && collisionlayer.getCell(xTile, yTile) != null){
//                    velocity.x = position.x-(xTile*16);
                    velocity.x = -1*(position.x-((xTile)*16));
                }
            }
            position.add(velocity);

        }

    }
}
