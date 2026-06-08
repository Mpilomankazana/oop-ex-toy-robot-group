package za.co.wethinkcode.robots.server;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.robot.Direction;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.world.World;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {

    @Test
    void shouldLaunchRobotSuccessfully() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = engine.launch("HAL", world);

        assertNotNull(robot);
        assertEquals("HAL", robot.getName());
    }

    @Test
    void shouldLaunchRobotInsideWorldBounds() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = engine.launch("HAL", world);

        assertTrue(world.isInsideWorld(robot.getX(), robot.getY()));
    }

    @Test
    void shouldMoveForwardCorrectly() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL", 0, 0);

        int moved = engine.moveForward(robot, 3, world);

        assertEquals(3, moved);
        assertEquals(0, robot.getX());
        assertEquals(3, robot.getY());
    }

    @Test
    void shouldStopAtObstacle() {

        World world = new World(20, 20);
        world.addObstacle(0, 3);

        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL", 0, 0);

        int moved = engine.moveForward(robot, 10, world);

        assertEquals(2, moved);
        assertEquals(2, robot.getY());
    }

    @Test
    void shouldMoveBackCorrectly() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL", 0, 3);

        int moved = engine.moveBack(robot, 2, world);

        assertEquals(2, moved);
        assertEquals(1, robot.getY());
    }


}
