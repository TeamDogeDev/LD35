package de.dogedev.ld35.assets.enums;

/**
 * Project: game
 * Package: de.dogedev.ld35.assets.enums
 * Date: 17.04.2016
 *
 * @author elektropapst
 */
public enum LevelMaps {
    // Last level
    LEVEL_1("level/basic.tmx", null),
    TUTORIAL("level/first.tmx", LEVEL_1);
    // First level

    public String name;
    public LevelMaps next;

    LevelMaps(String name, LevelMaps next) {
        this.name = name;
        this.next = next;
    }

}
