package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.PlayerComponent;
import de.dogedev.ld35.ashley.components.PositionComponent;
import de.dogedev.ld35.ashley.components.TextboxComponent;
import de.dogedev.ld35.assets.enums.BitmapFonts;
import de.dogedev.ld35.assets.enums.Textures;
import de.dogedev.ld35.michelangelo.Michel;

/**
 * Project: game
 * Package: de.dogedev.ld35.ashley.systems
 * Date: 17.04.2016
 *
 * @author elektropapst
 */
public class TextboxSystem extends EntitySystem {

    private ImmutableArray<Entity> textboxes;
    private Batch batch;
    private BitmapFont font;
    private PositionComponent localPc;
    private PositionComponent playerPc;
    private TextboxComponent localTc;
    private Texture textboxTexture;
    private float time;
    private OrthographicCamera camera;
    private ImmutableArray<Entity> player;

    public TextboxSystem(int priority, OrthographicCamera camera) {
        super(priority);
        this.camera = camera;
        batch = new SpriteBatch();
        font = Statics.asset.getBitmapFont(BitmapFonts.GAME, true);
        textboxTexture = Statics.asset.getTexture(Textures.BUBBLE);
    }

    @Override
    public void addedToEngine(Engine engine) {
        textboxes = engine.getEntitiesFor(
                Family.all(PositionComponent.class, TextboxComponent.class).get());

        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }


    @Override
    public void update(float deltaTime) {
        time += deltaTime;
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        font.setColor(Color.BLACK);
        for (Entity e : textboxes) {
            playerPc = ComponentMappers.position.get(player.get(0));
            localPc = ComponentMappers.position.get(e);
            localTc = ComponentMappers.textbox.get(e);

            localTc.visible = Michel.euclDist(playerPc, localPc) < 10*Statics.settings.tileSize;

            if (localTc.visible) {
                float yScale = 1 + (MathUtils.sin(4*time)*.05f);
                batch.draw(textboxTexture,
                            localPc.x, localPc.y,
                            textboxTexture.getWidth()>>1, 0,
                            textboxTexture.getWidth(), textboxTexture.getHeight(),
                            1, yScale,
                            0,
                            0, 0,
                            textboxTexture.getWidth(), textboxTexture.getHeight(),
                            localTc.alignRight, false
                            );

                font.draw(batch, localTc.text,
                        localPc.x + 4, localPc.y + (textboxTexture.getHeight()*yScale) - 4,
                        textboxTexture.getWidth() - 8, Align.topLeft, true
                );
                if (localTc.visTime > 0 && localTc.elapsedTime >= localTc.visTime) {
                    Statics.ashley.removeEntity(e);
                }
                localTc.elapsedTime += deltaTime;
            }
        }
        batch.end();
    }

}
