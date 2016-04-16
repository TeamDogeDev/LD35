package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.*;
import de.dogedev.ld35.assets.enums.ShaderPrograms;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.systems
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public class LightRenderSystem extends EntitySystem {

    private final OrthographicCamera camera;
    private final TextureRegion occludersTexture;
    private TiledMap map;
    private FrameBuffer occludersFBO;
    private FrameBuffer shadowMapFBO;
    private Batch batch;
    private ShaderProgram shadowMapShader;
    private ShaderProgram shadowRenderShader;
    private TextureRegion shadowMap1D;

    private ImmutableArray<Entity> lightSources;
    private ImmutableArray<Entity> entities;

    public LightRenderSystem(TiledMap map, OrthographicCamera camera) {
        this.map = map;
        this.camera = camera;
        shadowMapShader = Statics.asset.getShader(ShaderPrograms.SHADOWMAP);
        shadowRenderShader = Statics.asset.getShader(ShaderPrograms.SHADOWRENDER);
        batch = new SpriteBatch();

        occludersFBO = new FrameBuffer(Pixmap.Format.RGBA8888, 512, 512, false);
        occludersTexture = new TextureRegion(occludersFBO.getColorBufferTexture());
        occludersTexture.flip(false, true);
        shadowMapFBO = new FrameBuffer(Pixmap.Format.RGBA8888, 512, 1, false);
        Texture shadowMapTexture = shadowMapFBO.getColorBufferTexture();
        shadowMapTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shadowMapTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        shadowMap1D = new TextureRegion(shadowMapTexture);
        shadowMap1D.flip(false, true);
    }

    @Override
    public void addedToEngine(Engine engine) {
        lightSources = engine.getEntitiesFor(
            Family.all(PositionComponent.class, LightComponent.class).get()
        );
        entities = engine.getEntitiesFor(
            Family.all(PositionComponent.class)
                  .one(SpriteComponent.class, AnimationComponent.class)
                  .exclude(BackgroundComponent.class).get()
        );
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        PositionComponent pc;
        LightComponent lc;
        for(Entity e : lightSources) {
            pc = ComponentMappers.position.get(e);
            lc = ComponentMappers.light.get(e);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            renderLight(pc, lc);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    private void renderLight(PositionComponent pc, LightComponent lc) {
        float mx = pc.x;
        float my = pc.y;

        occludersFBO.begin();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.setToOrtho(false, occludersFBO.getWidth(), occludersFBO.getHeight());
        camera.translate(mx - (lc.lightSize / 2.f), my - (lc.lightSize / 2.f));

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null);
        batch.begin();
        TiledMapTileLayer collision = (TiledMapTileLayer) map.getLayers().get("collision");
        for (int x = 0; x < collision.getWidth(); x++) {
            for (int y = 0; y < collision.getHeight(); y++) {
                if (collision.getCell(x, y) != null && collision.getCell(x, y).getTile() != null)
                    batch.draw(collision.getCell(x, y).getTile().getTextureRegion(), x * 16, y * 16);
            }
        }
        PositionComponent epc;
        SpriteComponent esc;
        AnimationComponent eac;
        for(Entity e : entities) {
            epc = ComponentMappers.position.get(e);
            esc = ComponentMappers.sprite.get(e);

            if(esc != null) {
                batch.draw(esc.textureRegion, epc.x, epc.y);
            } else {
                eac = ComponentMappers.animation.get(e);
                batch.draw(eac.currentAnimation.getKeyFrame(eac.currentAnimationTime).getTexture(), epc.x, epc.y);
            }
        }
        batch.end();
        occludersFBO.end();

        batch.setShader(shadowMapShader);

        shadowMapFBO.begin();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setShader(shadowMapShader);
        batch.begin();
        shadowMapShader.setUniformf("resolution", lc.lightSize, lc.lightSize);
        shadowMapShader.setUniformf("upScale", 1);

        camera.setToOrtho(false, shadowMapFBO.getWidth(), shadowMapFBO.getHeight());
        batch.setProjectionMatrix(camera.combined);

        batch.draw(occludersFBO.getColorBufferTexture(), 0, 0, lc.lightSize, shadowMapFBO.getHeight());

        batch.end();
        shadowMapFBO.end();

        camera.setToOrtho(false);
        batch.setProjectionMatrix(camera.combined);
        batch.setShader(shadowRenderShader);
        batch.begin();

        shadowRenderShader.setUniformf("resolution", lc.lightSize, lc.lightSize);
        shadowRenderShader.setUniformf("softShadows", lc.softShadows ? 1f : 0f);
        batch.setColor(lc.color);

        float finalSize = lc.lightSize * 1;

        batch.draw(shadowMap1D.getTexture(), mx - finalSize / 2f, my - finalSize / 2f, finalSize, finalSize);
        batch.end();

        batch.setColor(Color.WHITE);
    }
}
