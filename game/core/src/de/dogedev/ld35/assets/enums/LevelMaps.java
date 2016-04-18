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
    FIFTH("level/6.tmx", null),
    FOURTH("level/fourth.tmx", FIFTH),
    THIRD("level/third.tmx", FOURTH),
    SECOND("level/second.tmx", THIRD),
    FIRST("level/first.tmx", SECOND),
    BASIC("level/basic.tmx", FIRST),
    TUTORIAL3("level/tutorial_3.tmx", BASIC),
    TUTORIAL2("level/tutorial_2.tmx", TUTORIAL3),
    TUTORIAL1("level/tutorial_1.tmx", TUTORIAL2),
    TEST("level/tutorial_1.tmx", null);
    // First level

    public String name;
    public LevelMaps next;

    LevelMaps(String name, LevelMaps next) {
        this.name = name;
        this.next = next;
    }

}
