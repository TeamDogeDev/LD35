package de.dogedev.ld35;

import com.badlogic.gdx.Game;

public class LDGame extends Game {

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
