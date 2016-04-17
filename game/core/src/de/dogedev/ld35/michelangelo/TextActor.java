package de.dogedev.ld35.michelangelo;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.assets.enums.BitmapFonts;

/**
 * Created by Furuha on 07.11.2015.
 */
public class TextActor extends Actor {

    private final String text;
    private final BitmapFont font;
    private final float width;
    private final int align;
    private final boolean wrap;

    public TextActor(String text, float width, int align) {
        this(text, width, align, true);
    }

    public TextActor(String text, float width, int align, boolean wrap) {
        super();
        this.text = text;
        this.wrap = wrap;
        font = Statics.asset.getBitmapFont(BitmapFonts.GAME, true);
        this.setColor(1,1,1,1);
        this.width = width;
        this.align = align;
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        font.getData().setScale(getScaleX(), getScaleY());
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        font.getData().setScale(getScaleX(), getScaleY());
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        font.getData().setScale(getScaleX(), getScaleY());
    }

    @Override
    public void scaleBy(float scale) {
        super.scaleBy(scale);
        font.getData().setScale(getScaleX(), getScaleY());
    }

    @Override
    public void scaleBy(float scaleX, float scaleY) {
        super.scaleBy(scaleX, scaleY);
        font.getData().setScale(getScaleX(), getScaleY());
    }

    public void setScale(float scaleXY){
        font.getData().setScale(scaleXY);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        font.setColor(getColor());
        font.draw(batch, text, getX(), getY(), width, align, this.wrap);
    }



}
