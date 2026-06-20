package za.co.wethinkcode.robots.world;

import java.util.SplittableRandom;

/**
 * Represents a Lake obstacle - a rectangular water terrain feature.
 * Robots cannot move though or fire through a lake.
 */

public class Lake extends Obstacle {

    public Lake (int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String getType() {
        return "LAKE";
    }
}
