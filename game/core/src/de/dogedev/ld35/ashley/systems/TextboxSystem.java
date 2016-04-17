package de.dogedev.ld35.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.ashley.ComponentMappers;
import de.dogedev.ld35.ashley.components.PositionComponent;
import de.dogedev.ld35.ashley.components.TextboxComponent;
import de.dogedev.ld35.assets.enums.BitmapFonts;
import de.dogedev.ld35.assets.enums.Textures;

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
    private TextboxComponent localTc;
    private Texture textboxTexture;
    private float time;

    public TextboxSystem(int priority) {
        super(priority);
        batch = new SpriteBatch();
        font = Statics.asset.getBitmapFont(BitmapFonts.GAME, true);
        textboxTexture = Statics.asset.getTexture(Textures.BUBBLE);
    }

    @Override
    public void addedToEngine(Engine engine) {
        textboxes = engine.getEntitiesFor(
                Family.all(PositionComponent.class, TextboxComponent.class).get());
    }


    @Override
    public void update(float deltaTime) {
        time += deltaTime;
        batch.begin();
        font.setColor(Color.BLACK);
        for (Entity e : textboxes) {
            localTc = ComponentMappers.textbox.get(e);
            if (localTc.visible) {
                localPc = ComponentMappers.position.get(e);
                float yScale = 1 + (MathUtils.sin(4*time)*.05f);
                batch.draw(textboxTexture,
                            localPc.x, localPc.y,
                            textboxTexture.getWidth()>>1, 0,
                            textboxTexture.getWidth(), textboxTexture.getHeight(),
                            1, yScale,
                            0,
                            0, 0,
                            textboxTexture.getWidth(), textboxTexture.getHeight(),
                            localTc.right, false
                            );

                localTc.elapsedTime += deltaTime;
                if (localTc.elapsedTime >= localTc.visTime) {
                    Statics.ashley.removeEntity(e);
                }
                font.draw(batch, localTc.text,
                        localPc.x + 4, localPc.y + (textboxTexture.getHeight()*yScale) - 4,
                        textboxTexture.getWidth() - 8, Align.topLeft, true
                );
            }
        }
        batch.end();
    }

}
