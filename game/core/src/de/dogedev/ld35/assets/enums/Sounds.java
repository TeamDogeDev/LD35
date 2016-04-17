package de.dogedev.ld35.assets.enums;

/**
 * Project: game
 * Package: de.dogedev.ld35.assets.enums
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public enum Sounds {

    JUMP("sounds/move1.mp3"),
    WALK("sounds/move2.mp3"),
    LANDING("sounds/move3.mp3");

    public String name;

    Sounds(String name) {
        this.name = name;
    }
}
