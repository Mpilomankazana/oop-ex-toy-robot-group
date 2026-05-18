package za.co.wethinkcode.robots.world;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.World;
import za.co.wethinkcode.robots.robot.Robot;


import java.nio.channels.WritePendingException;

import static org.junit.jupiter.api.Assertions.*;

//Tests world behaviour.
public class WorldTest {

    //Checks that the world is created correctly.
    @Test
    void shouldCreateWorldWithCorrectSize() {

        World world = new World(10, 10);

        assertEquals(10, world.getWidth());
        assertEquals(10, world.getHeight());
    }

    //Checks that the world contains obstacles.
    @Test
    void shouldContainObstacle() {
        World world = new World(10, 10);

        world.addObstacle(2, 2);
        assertTrue(world.hasObstacle(2, 2));
    }

    //Checks if coordinates outside world are invalid.
    @Test
    void shouldRejectOutOfBoundsPosition() {

        World world = new World(10, 10);
        assertFalse(world.isInsideWorld(20, 20));
    }

    //Checks if world coordinates are valid.
    @Test
    void shouldAllowValidWorldPosition() {

        World world = new World(10, 10);

        assertTrue(world.isInsideWorld(0, 0));
    }

    //World should not allow obstacle position from being used.
    @Test
    void shouldBlockObstaclePosition() {

        World world = new World(10, 10);

        world.addObstacle(1, 1);

        assertTrue(world.isBlocked(1, 1));
    }

    //Robot is blocked from position.
    @Test
    void shouldBlockRobotPosition() {

        World world = new World(10, 10);

        Robot robot = new Robot("HAL", 2, 2);

        world.addRobot(robot);

        assertTrue(world.isBlocked(2, 2));

    }

    // Free position should be allowed.
    @Test
    void shouldAllowFreePosition() {

        World world = new World(10, 10);

        assertFalse(world.isBlocked(0, 0));
    }

    // World should return robot name
    @Test
    void shouldReturnRobotByName() {

        World world = new World(10, 10);

        Robot robot = new Robot("HAL");

        world.addRobot(robot);

        assertEquals(robot, world.getRobot("HAL"));
    }

    // Null is returned for an unknown robot.
    @Test
    void shouldReturnNullForUnknownRobot() {

        World world = new World(10, 10);

        assertNull(world.getRobot("Unknown"));
    }

    // Remove robot from the world.
    @Test
    void shouldRemoveRobotFromWorld() {

        World world = new World(10, 10);

        Robot robot = new Robot("HAL");

        world.addRobot(robot);

        world.removeRobot("HAL");

        assertNull(world.getRobot("HAL"));
    }
}