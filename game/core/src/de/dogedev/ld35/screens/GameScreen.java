package de.dogedev.ld35.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
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
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld35.LDGame;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.*;
import de.dogedev.ld35.ashley.systems.*;
import de.dogedev.ld35.assets.enums.LevelMaps;
import de.dogedev.ld35.assets.enums.Textures;
import de.dogedev.ld35.michelangelo.ScreenshotFactory;
import de.dogedev.ld35.overlays.AbstractOverlay;
import de.dogedev.ld35.overlays.UiOverlay;

import static de.dogedev.ld35.Statics.*;

/**
 * Created by Furuha on 16.04.2016.
 */
public class GameScreen implements Screen {

    private final OrthographicCamera camera;
    private final Array<AbstractOverlay> overlays;
    private TiledMap currentMap;
    private LevelMaps currentLevel;
    private Batch batch;

    public GameScreen() {
        batch = new SpriteBatch();
        overlays = new Array<>();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = .5f;
        camera.translate(1280 >> 2, 720 >> 2);
        camera.update();

        currentLevel = LevelMaps.TUTORIAL;
        loadLevel(currentLevel);

        // demoMap.getLayers().add(new DebugTileLayer(16, 16, "debug"));

        ashley.addSystem(new BackgroundRenderSystem(0, camera, currentMap));
        // Statics.ashley.addSystem(new LightRenderSystem(1, demoMap, camera));
        ashley.addSystem(new CollisionRenderSystem(2, currentMap, camera));
        ashley.addSystem(new BackDecoRenderSystem(3, currentMap, camera));
        ashley.addSystem(new EntityRenderSystem(4, camera));
        ashley.addSystem(new ParticleRenderSystem(5, camera));
        ashley.addSystem(new FrontDecoRenderSystem(6, currentMap, camera));
        ashley.addSystem(new ItemSystem(7, currentMap, camera));
        ashley.addSystem(new TextboxSystem(8, camera));
        ashley.addSystem(new ControllSystem());
        ashley.addSystem(new AccelerationSystem());
        ashley.addSystem(new MovementSystem((TiledMapTileLayer) currentMap.getLayers().get("collision"), this));

        Entity e = ashley.createEntity();
        LightComponent lc = ashley.createComponent(LightComponent.class);
        lc.color = new Color(1f, 1f, .87f, 0.6f);
        lc.lightSize = 2048;
        lc.softShadows = true;
        PositionComponent pc = ashley.createComponent(PositionComponent.class);
        pc.x = Gdx.graphics.getWidth() - settings.tileSize * 8;
        pc.y = Gdx.graphics.getHeight() - settings.tileSize;

        e.add(pc);
        e.add(lc);
        ashley.addEntity(e);

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

        // demoEntity();

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

                if (keycode == Input.Keys.BACKSPACE) {
                    GameScreen.this.loadLevel(currentLevel);
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

         overlays.add(new UiOverlay(camera, ashley));
//        overlays.add(new DebugOverlay(camera, ashley));
    }

    public void nextLevel() {
        loadLevel(currentLevel.next);
    }

    public void loadLevel(LevelMaps levelMap) {
        if (levelMap != null) {

            clearMap();

            currentLevel = levelMap;
            System.out.println("Load" + currentLevel);
            currentMap = asset.getLevelMap(levelMap);
            updateMapInSystems();
            demoEntity();

            // create exit
            TiledMapTileLayer items = (TiledMapTileLayer) currentMap.getLayers().get("items");
            for (int x = 0; x < items.getWidth(); x++) {
                for (int y = 0; y < items.getHeight(); y++) {
                    if (items.getCell(x, y) != null && items.getCell(x, y).getTile() != null) {
                        System.out.println(items.getCell(x, y).getTile().getId());
                        if (items.getCell(x, y).getTile().getId() == 3) {
                            Entity e = Statics.ashley.createEntity();
                            PositionComponent pc = Statics.ashley.createComponent(PositionComponent.class);
                            pc.set(x * Statics.settings.tileSize, y * Statics.settings.tileSize);
                            e.add(pc);
                            e.add(Statics.ashley.createComponent(ExitComponent.class));
                            Statics.ashley.addEntity(e);
                        }
                    }
                }
            }

            if(currentMap.getLayers().get("text") != null) {
                MapObjects objects = currentMap.getLayers().get("text").getObjects();

                for(int i = 0; i < objects.getCount(); i++) {
                    TiledMapTileMapObject textObj = (TiledMapTileMapObject) objects.get(i);
                    int x = (int) textObj.getX() / Statics.settings.tileSize;
                    int y = (int) textObj.getY() / Statics.settings.tileSize;
                    if(textObj.getProperties().get("text") != null) {
                        String text = textObj.getProperties().get("text").toString();
                        if (text != null && !text.isEmpty()) {
                            textbox(text, x, y, -1);
                        }
                    }
                }
            }
        } else {
            LDGame.game.setScreen(new FinishScreen());
        }
    }

    private void clearMap() {
        ImmutableArray<Entity> players = Statics.ashley.getEntitiesFor(Family.all(PlayerComponent.class).get());
        ImmutableArray<Entity> keys = Statics.ashley.getEntitiesFor(Family.all(KeyComponent.class).get());
        ImmutableArray<Entity> exits = Statics.ashley.getEntitiesFor(Family.all(ExitComponent.class).get());
        ImmutableArray<Entity> textboxes = Statics.ashley.getEntitiesFor(Family.all(TextboxComponent.class).get());
        for(Entity e : players) {
            Statics.ashley.removeEntity(e);
        }
        for(Entity e : keys) {
            Statics.ashley.removeEntity(e);
        }
        for(Entity e : exits) {
            Statics.ashley.removeEntity(e);
        }
        for(Entity e : textboxes) {
            Statics.ashley.removeEntity(e);
        }
    }

    public void updateMapInSystems() {
        CollisionRenderSystem s = Statics.ashley.getSystem(CollisionRenderSystem.class);
        if (s != null) {
            s.setMap(currentMap);
        }

        BackDecoRenderSystem s1 = Statics.ashley.getSystem(BackDecoRenderSystem.class);
        if (s1 != null) {
            s1.setMap(currentMap);
        }

        FrontDecoRenderSystem s2 = Statics.ashley.getSystem(FrontDecoRenderSystem.class);
        if (s2 != null) {
            s2.setMap(currentMap);
        }
        ItemSystem s3 = Statics.ashley.getSystem(ItemSystem.class);
        if (s3 != null) {
            s3.setMap(currentMap);
        }
        MovementSystem s4 = Statics.ashley.getSystem(MovementSystem.class);
        if (s4 != null) {
            s4.setCollisionlayer((TiledMapTileLayer) currentMap.getLayers().get("collision"));
        }
        BackgroundRenderSystem s5 = Statics.ashley.getSystem(BackgroundRenderSystem.class);
        if(s5 != null) {
            s5.setMap(currentMap);
        }
    }

    public void textbox(String text, int x, int y, int i) {
        Entity e = Statics.ashley.createEntity();
        PositionComponent pc = Statics.ashley.createComponent(PositionComponent.class);
        TextboxComponent tc = Statics.ashley.createComponent(TextboxComponent.class);
        pc.x = x*Statics.settings.tileSize;
        pc.y = y*Statics.settings.tileSize;
        tc.text = text;
        tc.visTime = i;
        tc.visible = true;
        tc.alignRight = false;

        e.add(pc);
        e.add(tc);
        Statics.ashley.addEntity(e);
    }

    private void demoEntity() {

        Entity entity = ashley.createEntity();
        PositionComponent pc = ashley.createComponent(PositionComponent.class);
        pc.set(30 * settings.tileSize, 20 * settings.tileSize);
        TiledMapTileLayer items = (TiledMapTileLayer) currentMap.getLayers().get("items");

        for (int x = 0; x < items.getWidth(); x++) {
            for (int y = 0; y < items.getHeight(); y++) {
                if (items.getCell(x, y) != null && items.getCell(x, y).getTile() != null) {
                    System.out.println(items.getCell(x, y).getTile().getId());
                    if (items.getCell(x, y).getTile().getId() == 2) {
                        System.out.println("SPAWN @ " + x + ", " + y);
                        pc.set(x * settings.tileSize, y * settings.tileSize);
                    }
                }
            }
        }
        TiledMapTileLayer main = (TiledMapTileLayer) currentMap.getLayers().get("collision");
        int maxShiftCount = Integer.valueOf((String) main.getProperties().get("maxShiftCount"));
        float maxShiftTime = Float.valueOf((String) main.getProperties().get("maxShiftTime"));

        entity.add(pc);
        VelocityComponent vc = ashley.createComponent(VelocityComponent.class);
        vc.set(0, 0);
        entity.add(vc);
        AccelerationComponent ac = ashley.createComponent(AccelerationComponent.class);
        ac.set(0, 0);
        ac.maxVelocityX = 3;
        entity.add(ac);
        SizeComponent sc = ashley.createComponent(SizeComponent.class);
        sc.height = 2;
        sc.width = 1;
        entity.add(sc);
        PlayerComponent plc = ashley.createComponent(PlayerComponent.class);
        plc.maxShiftTime = maxShiftTime;
        plc.maxShiftCount = maxShiftCount;
        entity.add(plc);
//        SpriteComponent sc = Statics.ashley.createComponent(SpriteComponent.class);
//        sc.center = false;
//        sc.textureRegion = new TextureRegion(new Texture("entities/playerDemo.png"));
//        entity.add(sc);
        AnimationComponent anc = ashley.createComponent(AnimationComponent.class);
        TextureRegion[][] split = TextureRegion.split(asset.getTexture(Textures.JOHN), 16, 32);
        TextureRegion[][] chickenSplit = TextureRegion.split(asset.getTexture(Textures.CHICKEN), 16, 16);

        anc.idleAnimation = new Animation(2f, new Array<>(new TextureRegion[]{split[4][0], split[4][1], split[4][2], split[4][3], split[4][4], split[4][5]}), Animation.PlayMode.LOOP);
        anc.walkRightAnimation = new Animation(0.1f, new Array<>(split[0]), Animation.PlayMode.LOOP);
        anc.walkLeftAnimation = new Animation(0.1f, new Array<>(split[1]), Animation.PlayMode.LOOP);
        anc.chickenWalkRight = new Animation(0.1f, new Array<>(chickenSplit[0]), Animation.PlayMode.LOOP);
        anc.chickenWalkLeft = new Animation(0.1f, new Array<>(chickenSplit[1]), Animation.PlayMode.LOOP);
        anc.jumpAnimation = new Animation(0.1f, new Array<>(new TextureRegion[]{split[2][0], split[2][1], split[2][2], split[2][3], split[2][4], split[2][5]}), Animation.PlayMode.LOOP);
        anc.fallAnimation = new Animation(0.3f, new Array<>(new TextureRegion[]{split[0][0]}), Animation.PlayMode.LOOP);
        anc.currentAnimation = anc.idleAnimation;

        entity.add(anc);
        ashley.addEntity(entity);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 3f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        input();

        // quick n dirty camera following
        Entity player = ashley.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0);
        PositionComponent pc = ComponentMappers.position.get(player);
        float objX = MathUtils.clamp(pc.x, Gdx.graphics.getWidth() / 4,
                Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4);
        float objY = MathUtils.clamp(pc.y, Gdx.graphics.getHeight() / 4,
                Gdx.graphics.getWidth() - Gdx.graphics.getHeight() / 4);
        float lerp = 4f;

        Vector3 position = camera.position;
        position.x += (objX - position.x) * lerp * delta;
        position.y += (objY - position.y) * lerp * delta;

        camera.update();
        ashley.update(MathUtils.clamp(delta, 0, 0.020f));

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
