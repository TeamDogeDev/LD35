package de.dogedev.ld35.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.components.PlayerComponent;
import de.dogedev.ld35.ashley.components.PositionComponent;
import de.dogedev.ld35.ashley.components.SpriteComponent;
import de.dogedev.ld35.ashley.components.VelocityComponent;
import de.dogedev.ld35.ashley.systems.ControllSystem;
import de.dogedev.ld35.ashley.systems.MovementSystem;
import de.dogedev.ld35.ashley.systems.RenderSystem;
import de.dogedev.ld35.michelangelo.ScreenshotFactory;
import de.dogedev.ld35.overlays.AbstractOverlay;
import de.dogedev.ld35.overlays.DebugOverlay;

/**
 * Created by Furuha on 16.04.2016.
 */
public class GameScreen implements Screen {

    private final OrthographicCamera camera;
    private final Array<AbstractOverlay> overlays;
    private final TiledMap demoMap;
    private final OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(){
        overlays = new Array<>();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.5f;
        camera.translate(320, 180);

        demoMap = new TmxMapLoader().load("level/basic.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(demoMap);

        Statics.ashley.addSystem(new RenderSystem(camera));
        Statics.ashley.addSystem(new ControllSystem());
        Statics.ashley.addSystem(new MovementSystem((TiledMapTileLayer) demoMap.getLayers().get(0)));

        demoEntity();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(int amount) {
//                if(amount < 0){
//                    camera.zoom /= 2f;
//                } else {
//                    camera.zoom *= 2f;
//                }
//                if (camera.zoom > 1) {
//                    camera.zoom = 1;
//                }
//                if (camera.zoom < 0.25f) {
//                    camera.zoom = 0.25f;
//                }
                return super.scrolled(amount);
            }

            boolean fullscreen = false;

            @Override
            public boolean keyDown(int keycode) {

//                if (keycode == Input.Keys.F1) {
//                    map.getLayers().get("debug").setVisible(!map.getLayers().get("debug").isVisible());
//                }

                if (keycode == Input.Keys.F12) {
                    ScreenshotFactory.saveScreenshot();
                }
                if (keycode == Input.Keys.F11) {
                    fullscreen = !fullscreen;
                    return true;
                } else if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                }

                return super.keyDown(keycode);
            }
        });

        overlays.add(new DebugOverlay(camera, Statics.ashley));
    }

    private void demoEntity() {
        Entity entity = Statics.ashley.createEntity();
        PositionComponent pc = Statics.ashley.createComponent(PositionComponent.class);
        pc.set(160, 160);
        entity.add(pc);
        VelocityComponent vc = Statics.ashley.createComponent(VelocityComponent.class);
        vc.set(0, 0);
        entity.add(vc);
        PlayerComponent plc = Statics.ashley.createComponent(PlayerComponent.class);
        entity.add(plc);
        SpriteComponent sc = Statics.ashley.createComponent(SpriteComponent.class);
        sc.textureRegion = new TextureRegion(new Texture("john.png"));
        entity.add(sc);
        Statics.ashley.addEntity(entity);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        input();

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
        Statics.ashley.update(delta);

        //Render Overlays
        for (AbstractOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                overlay.render();
            }
        }
    }

    private void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            camera.translate(-10 * camera.zoom, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            camera.translate(10 * camera.zoom, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            camera.translate(0, -10 * camera.zoom);
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            camera.translate(0, 10 * camera.zoom);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
