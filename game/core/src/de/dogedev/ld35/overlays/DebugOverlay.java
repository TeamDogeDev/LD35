package de.dogedev.ld35.overlays;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.Align;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;

import java.text.DecimalFormat;

/**
 * Created by elektropapst on 27.12.2015.
 */
public class DebugOverlay extends AbstractOverlay {
    private BitmapFont font;
    private OrthographicCamera camera;
    private DecimalFormat floatFormat = new DecimalFormat("#.##");
    private PooledEngine ashley;
    private Console console;

    public DebugOverlay(OrthographicCamera camera, PooledEngine ashley) {
        init();
        this.camera = camera;
        this.ashley = ashley;
    }

    @Override
    public void init() {
        font = new BitmapFont();
        console = new Console();
        console.setSizePercent(100, 33);
        console.setPositionPercent(0, 67);
        console.setKeyID(Input.Keys.F2);
        console.setCommandExecutor(new GameCommandExecutor(console));
        GLProfiler.enable();
    }

    @Override
    public void update(float delta) {
    }

    private static int startY = 200;
    @Override
    public void render() {
        batch.begin();

        font.draw(batch, "cam x="+floatFormat.format(camera.position.x), 1070, startY-20, 200, Align.right, false);
        font.draw(batch, "cam y="+floatFormat.format(camera.position.y) , 1070, startY-40, 200, Align.right, false);
        font.draw(batch, "entities="+ashley.getEntities().size(), 1070, startY-60, 200, Align.right, false);
        font.draw(batch, "zoom="+camera.zoom, 1070, startY-80, 200, Align.right, false);
        font.draw(batch, "x="+Math.round((camera.position.x)/32), 1070, startY-100, 200, Align.right, false);
        font.draw(batch, "y="+Math.round((camera.position.y)/32) , 1070, startY-120, 200, Align.right, false);
        font.draw(batch, "DC="+GLProfiler.drawCalls, 1070, startY-140, 200, Align.right, false);
        font.draw(batch, "TXB="+GLProfiler.textureBindings , 1070, startY-160, 200, Align.right, false);
        font.draw(batch, "FPS="+Gdx.graphics.getFramesPerSecond() , 1070, startY-180, 200, Align.right, false);
        batch.end();

        GLProfiler.reset();

        console.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        GLProfiler.disable();
    }

    private class GameCommandExecutor extends CommandExecutor {
        private Console console;

        public GameCommandExecutor(Console console) {
            this.console = console;
        }

        public void loadLevel(String levelName) {
            console.log("loading level " + levelName, Console.LogLevel.SUCCESS);
        }

        public void spawnEnemy(String type, float x, float y, float z) {
            console.log("spawning enemy " + type, Console.LogLevel.SUCCESS);
        }

        public void clear() {
            console.clear();
        }
    }
}
