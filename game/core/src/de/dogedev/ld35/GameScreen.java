package de.dogedev.ld35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
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
