package de.dogedev.ld35.overlays;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.KeyComponent;
import de.dogedev.ld35.ashley.components.PlayerComponent;
import de.dogedev.ld35.assets.enums.BitmapFonts;
import de.dogedev.ld35.assets.enums.Textures;

import java.text.DecimalFormat;

/**
 * Created by elektropapst on 27.12.2015.
 */
public class UiOverlay extends AbstractOverlay {

    private final ImmutableArray<Entity> players;
    private final ImmutableArray<Entity> keys;
    private BitmapFont font;
    private DecimalFormat floatFormat = new DecimalFormat("#.##");
    private PooledEngine ashley;

    public UiOverlay(OrthographicCamera camera, PooledEngine ashley) {
        init();
        this.ashley = ashley;
        players = ashley.getEntitiesFor(Family.all(PlayerComponent.class).get());
        keys = ashley.getEntitiesFor(Family.all(KeyComponent.class).get());
    }

    @Override
    public void init() {
        font = Statics.asset.getBitmapFont(BitmapFonts.GAME_BIG, true);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
        PlayerComponent player = ComponentMappers.player.get(players.get(0));
        batch.begin();
        font.draw(batch, ""+floatFormat.format(player.maxShiftTime-player.shiftTime), 10, Gdx.graphics.getHeight()-7, 200, Align.left, false);
        font.draw(batch, "Keys left: "+keys.size(), 0, Gdx.graphics.getHeight()-7, 1270, Align.right, false);
        for(int i = player.maxShiftCount; i > 0; i--){
            batch.draw(Statics.asset.getTexture(Textures.CHICKWEN_SINGLE), 50+((player.maxShiftCount-i)*20), Gdx.graphics.getHeight()-25);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
