package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.KeyComponent;
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
public class ItemSystem extends EntitySystem {

    private ImmutableArray<Entity> items;
    private PositionComponent localPc;
    private SpriteComponent localSc;
    private Batch batch;

    public ItemSystem(TiledMap map) {
        batch = new SpriteBatch();
        // spawn Items
        for(MapLayer layer : map.getLayers()) {
            if(layer.getName().equals("items") && layer instanceof TiledMapTileLayer) {
                TiledMapTileLayer l = (TiledMapTileLayer) layer;
                for(int x = 0; x < l.getWidth(); x++) {
                    for (int y = 0; y < l.getHeight(); y++) {
                        if(l.getCell(x, y) != null && l.getCell(x, y).getTile() != null) {
                            spawnKey(l.getCell(x, y).getTile().getId(), x, y);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        items = engine.getEntitiesFor(
                Family.all(PositionComponent.class, SpriteComponent.class, KeyComponent.class).get()
        );
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        batch.begin();
        for (Entity e : items) {
            localSc = ComponentMappers.sprite.get(e);
            localPc = ComponentMappers.position.get(e);
            batch.draw(localSc.textureRegion, localPc.x, localPc.y);
        }
        batch.end();
    }

    public void spawnKey(int keyId, int tileX, int tileY) {
        Entity e = Statics.ashley.createEntity();

        PositionComponent pc = Statics.ashley.createComponent(PositionComponent.class);
        SpriteComponent sc = Statics.ashley.createComponent(SpriteComponent.class);
        KeyComponent kc = Statics.ashley.createComponent(KeyComponent.class);

        pc.x = tileX*Statics.tileSize;
        pc.y = tileY*Statics.tileSize;
        kc.keyId = keyId;

        sc.textureRegion = new TextureRegion(Statics.asset.getTexture(Textures.KEY));

        e.add(pc);
        e.add(sc);
        e.add(kc);
        Statics.ashley.addEntity(e);
    }

}
