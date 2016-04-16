package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.assets.enums.ShaderPrograms;
import de.dogedev.ld35.assets.enums.Textures;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.systems
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public class MapRenderSystem extends EntitySystem {

    private final OrthographicCamera camera;
    private final TextureRegion occludersTexture;
    private Batch tmpBatch;
    private TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthogonalTiledMapRenderer shadowMapRenderer;
    private FrameBuffer occludersFBO;
    private FrameBuffer shadowMapFBO;
    private Batch batch;
    private ShaderProgram shadowMapShader;
    private ShaderProgram shadowRenderShader;
    private TextureRegion shadowMap1D;

    public MapRenderSystem(TiledMap map, OrthographicCamera camera) {
        this.map = map;
        // this.camera = new OrthographicCamera();
        this.camera = camera;
        shadowMapShader = Statics.asset.getShader(ShaderPrograms.SHADOWMAP);
        shadowRenderShader = Statics.asset.getShader(ShaderPrograms.SHADOWRENDER);
        batch = new SpriteBatch();
        tmpBatch = new SpriteBatch();

        occludersFBO = new FrameBuffer(Pixmap.Format.RGBA8888, 512, 512, false);
        occludersTexture = new TextureRegion(occludersFBO.getColorBufferTexture());
        occludersTexture.flip(false, true);
        shadowMapFBO = new FrameBuffer(Pixmap.Format.RGBA8888, 512, 1, false);
        Texture shadowMapTexture = shadowMapFBO.getColorBufferTexture();
        shadowMapTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shadowMapTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        shadowMap1D = new TextureRegion(shadowMapTexture);
        shadowMap1D.flip(false, true);

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        shadowMapRenderer = new OrthogonalTiledMapRenderer(map, batch);
        mapRenderer.setView(camera);
    }

    @Override
    public void update(float deltaTime) {
        renderLight();
        // mapRenderer.render();
        batch.begin();
        batch.draw(occludersTexture, 100, 100);
        // batch.setShader(null);
        // mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("deco"));
        // batch.draw(Statics.asset.getTexture(Textures.JOHN), 200, 400);

        batch.end();
        //
        //
        // //STEP 4. render sprites in full colour
        //
        // tmpBatch.begin();
        // tmpBatch.setShader(null); //default shader
        // shadowMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("deco"));
        // // tmpBatch.draw(casterSprites, 0, 0);
        //
        // //DEBUG RENDERING -- show occluder map and 1D shadow map
        // tmpBatch.setColor(Color.BLACK);
        // tmpBatch.draw(occludersTexture, Gdx.graphics.getWidth()-256, 0);
        // tmpBatch.setColor(Color.WHITE);
        // tmpBatch.draw(shadowMap1D, Gdx.graphics.getWidth()-256, 256+5);
        //
        // tmpBatch.end();
    }

    private void renderLight() {
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();

        occludersFBO.begin();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.setToOrtho(false, occludersFBO.getWidth(), occludersFBO.getHeight());
        camera.translate(mx - (512.f / 2.f), my - (512.f / 2.f));

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null);
        batch.begin();
        shadowMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("deco"));
        batch.draw(Statics.asset.getTexture(Textures.JOHN), 200, 400);
        batch.end();
        occludersFBO.end();

        batch.setShader(shadowMapShader);

        shadowMapFBO.begin();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setShader(shadowMapShader);
        batch.begin();
        shadowMapShader.setUniformf("resolution", 512, 512);
        shadowMapShader.setUniformf("upScale", 1);

        camera.setToOrtho(false, shadowMapFBO.getWidth(), shadowMapFBO.getHeight());
        batch.setProjectionMatrix(camera.combined);

        batch.draw(occludersFBO.getColorBufferTexture(), 0, 0, 512, shadowMapFBO.getHeight());

        batch.end();
        shadowMapFBO.end();


        //STEP 3. render the blurred shadows

        //reset projection matrix to screen
        camera.setToOrtho(false);
        batch.setProjectionMatrix(camera.combined);

        //set the shader which actually draws the light/shadow
        batch.setShader(shadowRenderShader);
        batch.begin();

        shadowRenderShader.setUniformf("resolution", 512, 512);
        shadowRenderShader.setUniformf("softShadows", false ? 1f : 0f);
        //set color to light
        batch.setColor(Color.GREEN);

        float finalSize = 512 * 1;

        //draw centered on light position
        batch.draw(shadowMap1D.getTexture(), mx - finalSize / 2f, my - finalSize / 2f, finalSize, finalSize);

        //flush the batch before swapping shaders
        batch.end();

        //reset color
        batch.setColor(Color.WHITE);
    }
}
