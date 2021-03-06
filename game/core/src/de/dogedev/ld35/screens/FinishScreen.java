package de.dogedev.ld35.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.assets.enums.Musics;
import de.dogedev.ld35.michelangelo.TextActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

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
    private Stage stage;
    private TextActor text;
    public FinishScreen() {
        init();
    }

    private void init() {
        stage = new Stage();
        batch = new SpriteBatch();
        music = Statics.asset.getMusic(Musics.VICTORY2);//.play();
        tex = new Texture(Gdx.files.internal("textures/finishscreen.png"));


        TextActor text;

        text = new TextActor("YOU WON!", 1280, Align.center);
        text.setPosition(0, -300);
        text.setColor(Color.BLACK);
        text.setScale(5);
        text.addAction(sequence(delay(0), moveTo(0, 550, 2),delay(2), moveBy(0, 800, 2)));
        stage.addActor(text);

        text = new TextActor("Made for Ludum Dare 35 Jam", 1280, Align.center);
        text.setPosition(0, -300);
        text.setColor(Color.BLACK);
        text.setScale(3);
        text.addAction(sequence(delay(0), moveTo(0, 450, 2),delay(2), moveBy(0, 800, 2)));
        stage.addActor(text);

        text = new TextActor("By Elektropapst, Arse, Meisterfuu", 1280, Align.center);
        text.setPosition(0, -300);
        text.setColor(Color.BLACK);
        text.setScale(3);
        text.addAction(sequence(delay(0), moveTo(0, 350, 2),delay(2), moveBy(0, 800, 2)));
        stage.addActor(text);

        text = new TextActor("Made with LibGDX & Ashley", 1280, Align.center);
        text.setPosition(0, -300);
        text.setColor(Color.BLACK);
        text.setScale(5);
        text.addAction(sequence(delay(4), moveTo(0, 500, 2),delay(2), moveBy(0, 800, 2)));
        stage.addActor(text);

        text = new TextActor("Assets from Kenny.nl & Zeyu Ren", 1280, Align.center);
        text.setPosition(0, -300);
        text.setColor(Color.BLACK);
        text.setScale(5);
        text.addAction(sequence(delay(8), moveTo(0, 500, 2),delay(2), moveBy(0, 800, 2)));
        stage.addActor(text);

        text = new TextActor("Music from ourmusicbox.com", 1280, Align.center);
        text.setPosition(0, -300);
        text.setColor(Color.BLACK);
        text.setScale(5);
        text.addAction(sequence(delay(12), moveTo(0, 500, 2),delay(2), moveBy(0, 800, 2)));
        stage.addActor(text);

        text = new TextActor("Grass sound from duckduckpony (CC BY 3.0)", 1280, Align.center);
        text.setPosition(0, -300);
        text.setColor(Color.BLACK);
        text.setScale(5);
        text.addAction(sequence(delay(16), moveTo(0, 500, 2),delay(2), moveBy(0, 800, 2)));
        stage.addActor(text);

        text = new TextActor("The End!", 1280, Align.center);
        text.setPosition(0, -300);
        text.setColor(Color.BLACK);
        text.setScale(5);
        text.addAction(sequence(delay(21), moveTo(0, 500, 2),delay(10), moveBy(0, 800, 2)));
        stage.addActor(text);

        text = new TextActor("TeamDogeDev", 1280, Align.center);
        text.setPosition(0, -300);
        text.setColor(Color.BLACK);
        text.setScale(5);
        text.addAction(sequence(delay(35), moveTo(0, 500, 2)));
        stage.addActor(text);
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

        stage.act(delta);
        stage.draw();
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
