package za.co.wethinkcode.robots.world;

/**
 * Represents a mountain obstacle - a rectengular impassable terrain feature
 * Robots cannot move through or fire though a Mountain.
 */

public class Mountain  extends Obstacle {

    public Mountain(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String getType() {
        return "MOUNTAIN";
    }



}
