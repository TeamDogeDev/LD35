package de.dogedev.ld35.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.components.*;
import de.dogedev.ld35.ashley.systems.*;
import de.dogedev.ld35.assets.enums.Textures;
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
    private Batch batch;

    public GameScreen() {
        batch = new SpriteBatch();
        overlays = new Array<>();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1f;
        camera.translate(1280 >> 1, 720 >> 1);
        camera.update();

        demoMap = new TmxMapLoader().load("level/basic.tmx");
        // demoMap.getLayers().add(new DebugTileLayer(16, 16, "debug"));

        Statics.ashley.addSystem(new BackgroundRenderSystem(camera));
        Statics.ashley.addSystem(new LightRenderSystem(demoMap, camera));
        Statics.ashley.addSystem(new ItemSystem(demoMap));
        Statics.ashley.addSystem(new MapRenderSystem(demoMap, camera));
        Statics.ashley.addSystem(new EntityRenderSystem(camera));
        Statics.ashley.addSystem(new ControllSystem());
        Statics.ashley.addSystem(new AccelerationSystem());
        Statics.ashley.addSystem(new MovementSystem((TiledMapTileLayer) demoMap.getLayers().get("collision")));

        Entity e = Statics.ashley.createEntity();
        LightComponent lc = Statics.ashley.createComponent(LightComponent.class);
        lc.color = new Color(1f, 1f, .87f, 0.6f);
        lc.lightSize = 2048;
        lc.softShadows = true;
        PositionComponent pc = Statics.ashley.createComponent(PositionComponent.class);
        pc.x = Gdx.graphics.getWidth()-16*8;
        pc.y = Gdx.graphics.getHeight()-16;

        e.add(pc);
        e.add(lc);
        Statics.ashley.addEntity(e);

        // e = Statics.ashley.createEntity();
        // lc = Statics.ashley.createComponent(LightComponent.class);
        // lc.color = Color.GREEN;
        // lc.lightSize = 512;
        // lc.softShadows = true;
        // pc = Statics.ashley.createComponent(PositionComponent.class);
        // pc.x = 800;
        // pc.y = 160;
        //
        // e.add(pc);
        // e.add(lc);
        // Statics.ashley.addEntity(e);

        demoEntity();

        Gdx.input.setInputProcessor(new InputAdapter() {

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
                    if (fullscreen) {
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    } else {
                        Gdx.graphics.setWindowedMode(1280, 720);
                    }
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
        pc.set(320, 160);
        entity.add(pc);
        VelocityComponent vc = Statics.ashley.createComponent(VelocityComponent.class);
        vc.set(0, 0);
        entity.add(vc);
        AccelerationComponent ac = Statics.ashley.createComponent(AccelerationComponent.class);
        ac.set(0, 0);
        ac.maxVelocityX = 3;
        entity.add(ac);
        SizeComponent sc = Statics.ashley.createComponent(SizeComponent.class);
        sc.height = 2;
        sc.width = 1;
        entity.add(sc);
        PlayerComponent plc = Statics.ashley.createComponent(PlayerComponent.class);
        entity.add(plc);
//        SpriteComponent sc = Statics.ashley.createComponent(SpriteComponent.class);
//        sc.center = false;
//        sc.textureRegion = new TextureRegion(new Texture("entities/playerDemo.png"));
//        entity.add(sc);
        AnimationComponent anc = Statics.ashley.createComponent(AnimationComponent.class);
        TextureRegion[][] split = TextureRegion.split(Statics.asset.getTexture(Textures.JOHN), 16, 32);

        anc.idleAnimation = new Animation(2f, new Array<>(new TextureRegion[]{split[0][0]}), Animation.PlayMode.LOOP);
        anc.walkRightAnimation = new Animation(0.1f, new Array<>(split[0]), Animation.PlayMode.LOOP);
        anc.walkLeftAnimation = new Animation(0.1f, new Array<>(split[1]), Animation.PlayMode.LOOP);
        anc.jumpAnimation = new Animation(0.3f, new Array<>(new TextureRegion[]{split[0][0]}), Animation.PlayMode.NORMAL);
        anc.fallAnimation = new Animation(0.3f, new Array<>(new TextureRegion[]{split[0][0]}), Animation.PlayMode.LOOP);
        anc.currentAnimation = anc.idleAnimation;

        entity.add(anc);
        Statics.ashley.addEntity(entity);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 3f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        input();

        camera.update();
        Statics.ashley.update(MathUtils.clamp(delta, 0, 0.020f));

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
