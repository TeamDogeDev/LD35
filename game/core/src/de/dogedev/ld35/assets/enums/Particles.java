package de.dogedev.ld35.assets.enums;

/**
 * Project: game
 * Package: de.dogedev.ld35.assets.enums
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public enum Particles {

    DUST("effects/dust.p", "effects/images");

    public String effectFile;
    public String imageDir;

    Particles(String effectFile, String imageDir) {
        this.effectFile = effectFile;
        this.imageDir = imageDir;
    }

}
