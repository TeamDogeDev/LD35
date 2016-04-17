package de.dogedev.ld35.michelangelo;

import de.dogedev.ld35.ashley.components.PositionComponent;

/**
 * Project: game
 * Package: de.dogedev.ld35.michelangelo
 * Date: 17.04.2016
 *
 * @author elektropapst
 */
public class Michel {

    public static double euclDist(PositionComponent pos1, PositionComponent pos2) {
        if (pos1 == null || pos2 == null) {
            return Double.MAX_VALUE;
        }
        return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) +
                Math.pow(pos1.y - pos2.y, 2));
    }
}
