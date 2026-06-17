package za.co.wethinkcode.robots.server;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.robot.Direction;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.robot.RobotTest;
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
        world.addRobot(robot);

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
        world.addRobot(robot);

        int moved = engine.moveForward(robot, 10, world);

        assertEquals(2, moved);
        assertEquals(2, robot.getY());
    }

    @Test
    void shouldMoveBackCorrectly() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL", 0, 3);
        robot.setDirection(Direction.NORTH);
        world.addRobot(robot);

        int moved = engine.moveBack(robot, 2, world);

        assertEquals(2, moved);
        assertEquals(1, robot.getY());
    }

    @Test
    void shouldMoveBackWithoutChangingDirection() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL", 0, 3);
        robot.setDirection(Direction.NORTH);
        world.addRobot(robot);

        engine.moveBack(robot, 2, world);

        assertEquals(Direction.NORTH, robot.getDirection());
    }

    @Test
    void shouldTurnRight() {

        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL");

        Direction result = engine.turn(robot, "right");

        assertEquals(Direction.EAST, result);
    }

    @Test
    void shouldTurnLeft() {

        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL");

        Direction result = engine.turn(robot, "left");

        assertEquals(Direction.WEST, result);
    }

    @Test
    void shouldReturnNullForInvalidTurnDirection() {

        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL");

        Direction result = engine.turn(robot, "spin");

        assertNull(result);
    }

    @Test
    void shouldHitRobotInRange() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot shooter = new Robot("HAL", 0, 0);
        shooter.setDirection(Direction.NORTH);

        Robot target = new Robot("EVA", 0, 3);

        world.addRobot(shooter);
        world.addRobot(target);

        GameEngine.FireResult result = engine.fire(shooter, world);

        assertEquals("HIT", result.outcome);
    }

    @Test
    void shouldMissWhenNoRobotInRange() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot shooter = new Robot("HAL", 0, 0);
        shooter.setDirection(Direction.NORTH);

        world.addRobot(shooter);

        GameEngine.FireResult result = engine.fire(shooter, world);

        assertEquals("MISS", result.outcome);
    }

    @Test
    void shouldDecreaseShotsAfterFire() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot shooter = new Robot("HAL", 0, 0);

        int before = shooter.getShots();

        engine.fire(shooter, world);

        assertEquals(before - 1, shooter.getShots());
    }

    @Test
    void shouldDetectObstacleInLook() {

        World world = new World(20, 20);
        world.addObstacle(0, 3);

        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL", 0, 0);
        world.addRobot(robot);

        List<Map<String, Object>> result = engine.look(robot, world);

        assertTrue(result.stream().anyMatch(item -> item.get("type").equals("OBSTACLE")));
    }

    @Test
    void shouldDetectRobotInLook() {

        World world = new World(20, 20);

        Robot robot = new Robot("HAL", 0, 0);
        Robot enemy = new Robot("EVA", 0, 2);

        world.addRobot(robot);
        world.addRobot(enemy);

        GameEngine engine = new GameEngine();

        List<Map<String, Object>> result = engine.look(robot, world);

        assertTrue(result.stream().anyMatch(item -> item.get("type").equals("ROBOT")));

    }

    @Test
    void shouldRejectDuplicateRobotNames() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot first = engine.launch("HAL", world);
        Robot second = engine.launch("HAL", world);

        assertNotNull(first);
        assertNull(second);
    }

    @Test
    void shouldStopAtWorldEdge() {

        World world = new World(5, 5);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL", 0, 2);
        robot.setDirection(Direction.NORTH);

        world.addRobot(robot);

        int moved = engine.moveForward(robot, 10, world);

        assertTrue(moved < 10);
        assertTrue(world.isInsideWorld(robot.getX(), robot.getY()));
    }

    @Test
    void shouldStopAtAnotherRobot() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot hal = new Robot("HAL", 0, 0);
        hal.setDirection(Direction.NORTH);

        Robot eva = new Robot("EVA", 0, 3);

        world.addRobot(hal);
        world.addRobot(eva);

        int moved = engine.moveForward(hal, 10, world);

        assertEquals(2, moved);
        assertEquals(2, hal.getY());
    }

    @Test
    void shouldKillRobotWhenShieldsReachZero() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot shooter = new Robot("HAL", 0, 0);
        shooter.setDirection(Direction.NORTH);

        Robot target = new Robot("EVA", 0, 2);
        target.setShields(1);

        world.addRobot(shooter);
        world.addRobot(target);

        GameEngine.FireResult result = engine.fire(shooter, world);

        assertEquals("KILL", result.outcome);
        assertEquals("DEAD", target.getStatus());
        assertNull(world.getRobot("EVA"));
    }

    @Test
    void shouldRespectVisibilityRange() {

        World world = new World(20, 20);

        // Obstacle beyond visibility range
        world.addObstacle(0, 10);

        Robot robot = new Robot("HAL", 0, 0);
        world.addRobot(robot);

        GameEngine engine = new GameEngine();

        List<Map<String, Object>> result = engine.look(robot, world);

        boolean obstacleSeen = result.stream()
                .anyMatch(item -> item.get("type").equals("OBSTACLE")
                        && ((Integer) item.get("distance")) == 10);

        assertFalse(obstacleSeen);
    }

    @Test
    void shouldNotHitDeadRobotAgain() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot shooter = new Robot("HAL", 0, 0);
        shooter.setDirection(Direction.NORTH);

        Robot target = new Robot("EVA", 0, 2);
        target.setShields(1);

        world.addRobot(shooter);
        world.addRobot(target);

        engine.fire(shooter, world);

        assertNull(world.getRobotAt(0, 2));
    }

    @Test
    void shouldReturnErrorWhenOutOfShots() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot shooter = new Robot("HAL");
        shooter.setShots(0);

        GameEngine.FireResult result = engine.fire(shooter, world);

        assertEquals("ERROR", result.outcome);
    }

    @Test
    void shouldNotFireThroughObstacle() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot shooter = new Robot("HAL", 0, 0);
        shooter.setDirection(Direction.NORTH);

        Robot target = new Robot("EVA", 0, 4);

        world.addObstacle(0, 2);

        world.addRobot(shooter);
        world.addRobot(target);

        GameEngine.FireResult result = engine.fire(shooter, world);

        assertEquals("MISS", result.outcome);
    }
}
