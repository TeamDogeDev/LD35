package de.dogedev.ld35.assets;

import com.badlogic.gdx.math.MathUtils;
import de.dogedev.ld35.Statics;
import de.dogedev.ld35.assets.enums.Sounds;

/**
 * Project: game
 * Package: de.dogedev.ld35.assets
 * Date: 17.04.2016
 *
 * @author elektropapst
 */
public class SoundManager {
    public void stopSound(Sounds sound, long id) {
        Statics.asset.getSound(sound).stop(id);
    }

    public long loopSound(Sounds sound) {
        return loopSound(sound, Statics.settings.soundVolume);
    }

    public long loopSound(Sounds sound, float volume) {
        return Statics.asset.getSound(sound).loop(volume);
    }

    public long playSound(Sounds sound) {
        return playSound(sound, Statics.settings.soundVolume);
    }

    public long playSoundPitched(Sounds sound) {
        return playSoundPitched(sound, Statics.settings.soundVolume, MathUtils.random(0.7f, 1.3f));
    }

    public long playSound(Sounds sound, float volume) {
        return Statics.asset.getSound(sound).play(volume);
    }

    public long playSoundPitched(Sounds sound, float volume, float pitch) {
        return Statics.asset.getSound(sound).play(volume, pitch, 1);
    }
}
