package de.dogedev.ld35;

import com.badlogic.gdx.Game;

public class LDGame extends Game {
	
	@Override
	public void create () {
		this.setScreen(new GameScreen());
	}

	@Override
	public void render () {
		super.render();
	}
}
