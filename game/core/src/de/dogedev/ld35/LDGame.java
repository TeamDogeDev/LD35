package de.dogedev.ld35;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import de.dogedev.ld35.screens.LoadingScreen;

public class LDGame extends Game {

    public static LDGame game;

    @Override
    public void create() {
        game = this;
        setScreen(new LoadingScreen());
    }

    public void setCurrentScreen(Screen screen) {
        this.setScreen(screen);
    }

    @Override
    public void render() {
        super.render();
    }
}
