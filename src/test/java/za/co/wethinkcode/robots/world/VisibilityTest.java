package za.co.wethinkcode.robots.world;

import org.junit.jupiter.api.Test;

import static  org.junit.jupiter.api.Assertions.*;

public class VisibilityTest {

    @Test
    void testObstacleWithinVisibilityRange() {

        World world = new World(20, 20);

        world.addObstacle(2, 2);

        assertTrue(world.hasObstacle(2, 2));
    }

    @Test
    void testObstacleOutsideVisibilityRange() {

        World world = new World(20, 20);

        assertFalse(world.hasObstacle(10, 10));
    }
}
