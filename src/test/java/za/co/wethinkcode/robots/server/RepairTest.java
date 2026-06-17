package za.co.wethinkcode.robots.server;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.world.World;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Waiting for repair implementation")
public class RepairTest {

    @Test
    void testRepairRestoresShieldsToMax() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL");
        robot.setShields(1);

        // engine.repair(robot, world);

        assertEquals(
                world.getMaxShields(),
                robot.getShields(),
                "Repair should restore shields to world maximum"
        );

        assertEquals(
                "NORMAL",
                robot.getStatus(),
                "Robot should return to NORMAL after repair"
        );
    }

    @Test
    void testRepairSetsStatusToRepair() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL");

        // engine.repair(robot, world);

        assertEquals(
                "REPAIR",
                robot.getStatus(),
                "Robot status should be REPAIR while repairing"
        );
    }

    @Test
    void testRepairDoesNotExceedMaximumShields() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL");
        robot.setShields(world.getMaxShields());

        //engine.repair(robot, world);

        assertTrue(
                robot.getShields() <= world.getMaxShields(),
                "Repair must never exceed maximum shields"
        );
    }

    @Test
    void testRepairWorksWhenShieldsAreZero() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL");
        robot.setShields(0);

        // engine.repair(robot, world):

        assertEquals(
                world.getMaxShields(),
                robot.getShields(),
                "Repair should restore a robot with zero shields"
        );
    }

    @Test
    void testRepairDoesNotChangeRobotPosition() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL", 3, 4);

        int startX = robot.getX();
        int startY = robot.getY();

        // engine.repair(robot, world);

        assertEquals(startX, robot.getX());
        assertEquals(startY, robot.getY());
    }

    @Test
    void testRepairDoesNotChangeDirection() {

        World world = new World(20, 20);
        GameEngine engine = new GameEngine();

        Robot robot = new Robot("HAL");

        var originalDirection = robot.getDirection();

        // engine.repair(robot, world);

        assertEquals(
                originalDirection,
                robot.getDirection(),
                "Repair should not change robot direction"
        );
    }
}
