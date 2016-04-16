package de.dogedev.ld35;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.dogedev.ld35.assets.enums.ShaderPrograms;

public class LDGame extends Game {
    SpriteBatch batch;
    Texture img;

    @Override
    public void create() {
        Statics.initCat();
        while(!Statics.asset.load()) {
            System.out.println("Loading " + Statics.asset.progress());
        }
        this.setScreen(new GameScreen());
    }

    @Override
    public void render() {
        super.render();
    }
}
