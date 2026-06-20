package za.co.wethinkcode.robots.robot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Tests robot behaviour
public class RobotTest {

    //Robot should start at default position
    @Test
    void shouldStartAtOrigin() {

        Robot robot = new Robot("HAL");

        assertEquals(0, robot.getX());
        assertEquals(0, robot.getY());
    }

    //Robot should move forward
    @Test
    void shouldMoveForward() {

        Robot robot = new Robot("HAL");

        robot.forward(3);
        assertEquals(3, robot.getY());
    }

    //Robot should move backward
    @Test
    void shouldMoveBackward() {

        Robot robot = new Robot("HAL");

        robot.back(2);
        assertEquals(-2, robot.getY());
    }

    //Robot should turn right correctly.
    @Test
    void shouldTurnRight() {

        Robot robot = new Robot("HAL");

        robot.turnRight();

        assertEquals(Direction.EAST, robot.getDirection());
    }

    //Robot should turn left.
    @Test
    void shouldTurnLeft() {

        Robot robot = new Robot("HAL");

        robot.turnLeft();
        assertEquals(Direction.WEST, robot.getDirection());
    }

    // Should return to original position after 4 turns.
    @Test
    void shouldReturnToOriginalDirectionAfterFourRightTurns() {

        Robot robot = new Robot("HAL");

        robot.turnRight();
        robot.turnRight();
        robot.turnRight();
        robot.turnRight();

        assertEquals(Direction.NORTH, robot.getDirection());
    }

    // Should be able to move to the East.
    @Test
    void shouldMoveEastCorrectly() {

        Robot robot = new Robot("HAL");

        robot.turnRight();
        robot.forward(5);

        assertEquals(5, robot.getX());
    }

    // Robot should stay still if 0 steps are input.
    @Test
    void shouldNotMoveWhenStepsAreZero() {

        Robot robot = new Robot("HAL");

        robot.forward(0);

        assertEquals(0, robot.getX());
        assertEquals(0, robot.getY());
    }

    // Robot status should be updated.
    @Test
    void shouldUpdateRobotStatus() {

        Robot robot = new Robot("HAL");

        robot.setStatus("DAMAGED");

        assertEquals("DAMAGED", robot.getStatus());
    }

    // Robot shield value should be updated.
    @Test
    void shouldUpdateShieldValue() {

        Robot robot = new Robot("HAL");

        robot.setShields(1);

        assertEquals(1, robot.getShields());
    }

    @Test
    void shouldSetAndGetXCoordinate() {

        Robot robot = new Robot("HAL");

        robot.setX(10);

        assertEquals(10, robot.getX());
    }

    @Test
    void shouldSetAndGetYCoordinate() {

        Robot robot = new Robot("HAL");

        robot.setY(20);

        assertEquals(20, robot.getY());
    }

    @Test
    void shouldSetDirection() {

        Robot robot = new Robot("HAL");

        robot.setDirection(Direction.SOUTH);

        assertEquals(Direction.SOUTH,
                robot.getDirection());
    }

    @Test
    void shouldSetStatus() {

        Robot robot = new Robot("HAL");

        robot.setStatus("REPAIRING");

        assertEquals("REPAIRING",
                robot.getStatus());
    }

    @Test
    void shouldSetShots() {

        Robot robot = new Robot("HAL");

        robot.setShots(3);

        assertEquals(3, robot.getShots());
    }

    @Test
    void shouldGenerateToStringContainingRobotName() {

        Robot robot = new Robot("HAL");

        assertTrue(robot.toString()
                .contains("HAL"));
    }

    @Test
    void shouldCreateRobotWithMake(){

        Robot robot = new Robot("HAL", "tank");

        assertEquals("tank", robot.getMake());
    }

    @Test
    void shouldCreateRobotWithPositionAndMake(){

        Robot robot = new Robot(
                "HAL",
                "tank",
                5,
                6);

        assertEquals(5, robot.getX());
        assertEquals(6, robot.getY());
        assertEquals("tank", robot.getMake());
    }

    @Test
    void shouldCreateRobotWithPosition(){

        Robot robot =
                new Robot("HAL",
                        3,
                        4);

        assertEquals(3, robot.getX());
        assertEquals(4, robot.getY());
    }
}
