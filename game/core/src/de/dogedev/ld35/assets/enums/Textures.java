package de.dogedev.ld35.assets.enums;

/**
 * Project: game
 * Package: de.dogedev.ld35.assets.enums
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public enum Textures {

    JOHN("entities/john_walker.png"),
    SKY("textures/sky.png"),
    CLOUD("textures/cloud.png"),
    KEY("entities/key.png"),
    BUBBLE("textures/bubble.png");

    public String name;

    Textures(String name) {
        this.name = name;
    }
}
