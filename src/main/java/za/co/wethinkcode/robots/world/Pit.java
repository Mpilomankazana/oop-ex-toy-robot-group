package za.co.wethinkcode.robots.world;

/**
 * Represents a Pit obstacle - a rectangular hole in the terrain.
 * Robots cannot move Though or fire through a pit.
 */

public class Pit extends Obstacle {

    public Pit (int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String getType() {
        return "PIT";
    }
}
