package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
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


    public MapRenderSystem(TiledMap map, OrthographicCamera camera) {

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(camera);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        mapRenderer.render();
    }


}
