package de.dogedev.ld35.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.assets.enums.Musics;

/**
 * Project: game
 * Package: de.dogedev.ld35.screens
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public class FinishScreen implements Screen {

    private Batch batch;
    private Texture tex;
    private Music music;

    public FinishScreen() {
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        music = Statics.asset.getMusic(Musics.VICTORY2);//.play();
        tex = new Texture(Gdx.files.internal("textures/titlescreen.png"));
    }

    @Override
    public void show() {
        music.stop();
        music.play();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(tex, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void hide() {
        music.pause();
    }

    @Override
    public void dispose() {
        music.stop();
    }
}
