package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.systems
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public class MapRenderSystem extends EntitySystem {

    private final OrthogonalTiledMapRenderer mapRenderer;
    private Batch mapBatch;
    private TiledMap map;
    private TiledMapTileLayer localTiledMapTileLayer;


    public MapRenderSystem(TiledMap map, OrthographicCamera camera) {
        this.map = map;
        mapBatch = new SpriteBatch();
        mapRenderer = new OrthogonalTiledMapRenderer(map, mapBatch);
        mapRenderer.setView(camera);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        mapBatch.begin();
        for (MapLayer tiledMap : map.getLayers()) {
            if(!tiledMap.getName().equals("items")) {
                if (tiledMap instanceof TiledMapTileLayer) {
                    localTiledMapTileLayer = (TiledMapTileLayer) tiledMap;
                    mapRenderer.renderTileLayer(localTiledMapTileLayer);
                }
            }
        }
        mapBatch.end();
    }


}
