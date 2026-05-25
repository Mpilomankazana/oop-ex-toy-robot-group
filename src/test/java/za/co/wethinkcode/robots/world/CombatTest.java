package za.co.wethinkcode.robots.world;

import org.junit.jupiter.api.Test;
import  za.co.wethinkcode.robots.robot.Robot;

import static org.junit.jupiter.api.Assertions.*;

public class CombatTest {

    @Test
    void testRobotShieldCanDecrease() {

        Robot robot = new Robot("HAL");

        robot.setShields(5);

        robot.setShields(robot.getShields() -1);

        assertEquals(4, robot.getShields());
    }

    @Test
    void testRobotShotsCanDecrease() {

        Robot robot = new Robot("HAL");

        robot.setShields(5);

        robot.setShields(robot.getShots() - 1);

        assertEquals(4, robot.getShots());
    }

    @Test
    void testRobotStatusChangesWhenDestroyed() {

        Robot robot = new Robot("HAL");

        robot.setStatus("DEAD");

        assertEquals("DEAD", robot.getStatus());
    }
}