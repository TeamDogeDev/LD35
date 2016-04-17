package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.*;
import de.dogedev.ld35.assets.enums.Textures;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.systems
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public class BackgroundRenderSystem extends EntitySystem {

    private OrthographicCamera camera;
    private Batch batch;
    private ImmutableArray<Entity> clouds;
    private Texture background;
    private Array<Texture> parallaxTextures;
    private Texture parallax1;
    private Texture parallax2;
    private float cloudDarkness = 0;

    private static final int MAXCLOUDS = 30;
    private TiledMap map;

    public BackgroundRenderSystem(int priority, OrthographicCamera camera, TiledMap map) {
        super(priority);
        parallaxTextures = new Array<>();
        setMap(map);
        batch = new SpriteBatch();
        this.camera = camera;

        background = Statics.asset.getTexture(Textures.SKY);
        // parallax1 = Statics.asset.getTexture(Textures.PARALLAX_1);
        //
        // parallax2 = Statics.asset.getTexture(Textures.PARALLAX_2);
        // parallax2.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.ClampToEdge);
    }
    public BackgroundRenderSystem(OrthographicCamera camera, TiledMap map) {
        this(0, camera, map);
    }

    @Override
    public void addedToEngine(Engine engine) {
        clouds = engine.getEntitiesFor(
                Family.all(PositionComponent.class, BackgroundComponent.class, VelocityComponent.class)
                        .one(SpriteComponent.class, AnimationComponent.class).get());
        // initial clouds
        do {
            spawnRandomCloud();
        } while(clouds.size() < MAXCLOUDS);
    }

    private double rescale(double val, double minIn, double maxIn, double minOut, double maxOut) {
        return ((maxOut - minOut) * (val - minIn) / (maxIn - minIn)) + minIn;
    }

    int s;
    @Override
    public void update(float deltaTime) {
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.setColor(1, 1, 1, 1);
        // batch.draw(background, 0, 0);
        float focalPointSpeed = 0.07f;
        float layerDifference = 0.02f;
        float parallaxScale = focalPointSpeed + (layerDifference*parallaxTextures.size);
        for (int i = 0; i < parallaxTextures.size; i++) {

            Texture tr = parallaxTextures.get(i);
            if(i == parallaxTextures.size) parallaxScale = 0;
            batch.draw(tr, 0, 0, (int)(camera.position.x*parallaxScale), (int)-(camera.position.y*parallaxScale), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            parallaxScale -= layerDifference;
        }
        // batch.draw(parallax2, 0, 0, (int)(camera.position.x*0.05), (int)-(camera.position.y*0.05), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // batch.draw(parallax1, 0, 0, (int)(camera.position.x*0.1), (int)-(camera.position.y*0.1), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        PositionComponent pc;
        SpriteComponent sc;
        VelocityComponent vc;

        for (Entity cloud : clouds) {
            pc = ComponentMappers.position.get(cloud);
            sc = ComponentMappers.sprite.get(cloud);
            vc = ComponentMappers.velocity.get(cloud);
            pc.mulAdd(vc, deltaTime);
            batch.setColor(1-cloudDarkness, 1-cloudDarkness, 1-cloudDarkness, MathUtils.clamp(pc.z-0.6f, 0.05f, 0.6f));
            batch.draw(sc.textureRegion, pc.x, pc.y, sc.textureRegion.getRegionWidth()*pc.z,sc.textureRegion.getRegionHeight()*pc.z);
            if (!isCloudInBounds(pc, sc)) {
                Statics.ashley.removeEntity(cloud); // TODO RECYCLE
                spawnNewCloud();
            }
        }
        batch.end();
    }

    private void spawnNewCloud() {
        spawnCloud(Gdx.graphics.getWidth(), MathUtils.random(Gdx.graphics.getBackBufferHeight()>>1, Gdx.graphics.getHeight()), MathUtils.random(0.2f, 1f));
    }
    private void spawnRandomCloud() {
        spawnCloud(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(Gdx.graphics.getBackBufferHeight()>>1, Gdx.graphics.getHeight()), MathUtils.random(0.2f, 1f));
    }

    private void spawnCloud(int x, int y, float z) {
        Entity entity = Statics.ashley.createEntity();
        SpriteComponent sc = Statics.ashley.createComponent(SpriteComponent.class);
        sc.textureRegion = new TextureRegion(Statics.asset.getTexture(Textures.CLOUD));
        entity.add(sc);
        PositionComponent pc = Statics.ashley.createComponent(PositionComponent.class);
        // pc.x = Gdx.graphics.getWidth();
        pc.x = x;
        pc.y = y;
        pc.z = z;
        entity.add(pc);
        VelocityComponent vc = Statics.ashley.createComponent(VelocityComponent.class);
        vc.x = -z*Statics.settings.tileSize;
        vc.y = 0;
        entity.add(vc);
        entity.add(Statics.ashley.createComponent(BackgroundComponent.class));
        Statics.ashley.addEntity(entity);
    }

    private boolean isCloudInBounds(PositionComponent pc, SpriteComponent sc) {
        return  pc.x <= Gdx.graphics.getWidth() &&
                pc.y <= Gdx.graphics.getHeight() &&
                pc.x + sc.textureRegion.getRegionWidth() >= 0 &&
                pc.y + sc.textureRegion.getRegionHeight() >= 0;
    }

    public void setMap(TiledMap map) {
        this.map = map;
        parallaxTextures.clear();

        Array<TiledMapImageLayer> imageLayers = map.getLayers().getByType(TiledMapImageLayer.class);

        for(TiledMapImageLayer imageLayer : imageLayers) {
            Texture texture = imageLayer.getTextureRegion().getTexture();
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
            parallaxTextures.add(texture);
        }
    }
}
