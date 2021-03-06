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
 * Date: 17.04.2016
 *
 * @author elektropapst
 */
public class CollisionRenderSystem extends EntitySystem {

    private final OrthogonalTiledMapRenderer mapRenderer;
    private Batch mapBatch;
    private TiledMap map;
    private TiledMapTileLayer localTiledMapTileLayer;
    private OrthographicCamera camera;


    public CollisionRenderSystem(TiledMap map, OrthographicCamera camera) {
        this(0, map, camera);
    }

    public CollisionRenderSystem(int priority, TiledMap map, OrthographicCamera camera) {
        super(priority);
        this.map = map;
        this.camera = camera;
        mapBatch = new SpriteBatch();
        mapRenderer = new OrthogonalTiledMapRenderer(map, mapBatch);
        mapRenderer.setView(camera);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        mapBatch.begin();
        mapRenderer.setView(camera);
        for (MapLayer tiledMap : map.getLayers()) {
            if (tiledMap.getName().equals("collision")) {
                if (tiledMap instanceof TiledMapTileLayer) {
                    localTiledMapTileLayer = (TiledMapTileLayer) tiledMap;
                    mapRenderer.renderTileLayer(localTiledMapTileLayer);
                }
            }
        }
        mapBatch.end();
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }
}
