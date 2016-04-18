package de.dogedev.ld35.assets.enums;

/**
 * Project: game
 * Package: de.dogedev.ld35.assets.enums
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public enum Sounds {

    JUMP("sounds/jump.wav"),
    WALK("sounds/move2.mp3"),
    KEY("sounds/key.wav"),
    ERROR("sounds/error.wav"),
    CHICKEN("sounds/chicken.wav"),
    FLAP("sounds/flap.mp3"),
    LANDING("sounds/move3.mp3"),
    TEXTBOX("sounds/textbox.mp3");

    public String name;

    Sounds(String name) {
        this.name = name;
    }
}
