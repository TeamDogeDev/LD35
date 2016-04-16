package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.BackgroundComponent;
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
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).exclude(BackgroundComponent.class).get());
        engine.addEntityListener(Family.all(PositionComponent.class, VelocityComponent.class).exclude(BackgroundComponent.class).get(), this);
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


            int yTile = (int)(position.y+velocity.y)/16;
            int xTile = (int)(position.x)/16;
            int xTile2 = (int)(position.x+16)/16;
            
            if(velocity.y <0){
                if(collisionlayer != null && collisionlayer.getCell(xTile, yTile) != null){
                    position.isStanding = true;
                    velocity.y = -1*(position.y-((yTile+1)*16));
                } else {
                    position.isStanding = false;
                }
            } else if(velocity.y > 0) {
                if(collisionlayer != null && collisionlayer.getCell(xTile, yTile+2) != null){
                    position.isStanding = false;
                    velocity.y = 0;
                } else {
                    position.isStanding = false;
                }
            }




            xTile = (int)(position.x+velocity.x)/16;
            yTile = (int)(position.y+velocity.y)/16;
            if(velocity.x < 0){
                if(collisionlayer != null && collisionlayer.getCell(xTile, yTile) != null){
                    velocity.x = 0;
                }
            } else if(velocity.x > 0){
                if(collisionlayer != null && collisionlayer.getCell(xTile+1, yTile) != null){
                    velocity.x = 0;

                }
            }

            yTile += 1;
            if(velocity.x < 0){
                if(collisionlayer != null && collisionlayer.getCell(xTile, yTile) != null){
                    velocity.x = 0;

                }
            } else if(velocity.x > 0){
                if(collisionlayer != null && collisionlayer.getCell(xTile+1, yTile) != null){
                    velocity.x = 0;

                }
            }

            position.add(velocity);

        }

    }

    private boolean rectCollides(float x1, float y1, float x2, float y2, float size){
//        checks++;
        if((x1-size) < x2 && x2 < (x1+size) && Math.abs(y2-y1) < size){
            return true;
        }
        if((y1-size) < y2 && y2 < (y1+size) && Math.abs(x2-x1) < size){
            return true;
        }
        return false;
    }


    private float distance(float i, float y) {
        return Math.abs(i-y);
    }
}
