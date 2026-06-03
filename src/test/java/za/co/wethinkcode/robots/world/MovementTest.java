package za.co.wethinkcode.robots.world;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.robot.Direction;
import za.co.wethinkcode.robots.robot.Robot;

import static org.junit.jupiter.api.Assertions.*;

public class MovementTest {

    @Test
    void testRobotMovesForwardCorrectly() {

        Robot robot = new Robot("HAL");

        robot.forward(3);

        assertEquals(0, robot.getX());
        assertEquals(3, robot.getY());
    }

    @Test
    void testBackMovesReverseNoDirectionChange() {

        Robot robot = new Robot("HAL");

        robot.back(2);

        assertEquals(0, robot.getX());
        assertEquals(-2, robot.getY());

        assertEquals(Direction.NORTH,
                robot.getDirection());
    }

    @Test
    void testTurnRightChangesDirection() {

        Robot robot = new Robot("HAL");

        robot.turnRight();

        assertEquals(Direction.EAST,
                robot.getDirection());
    }

    @Test
    void testTurnLeftChangesDirection() {

        Robot robot = new Robot("HAL");

        robot.turnLeft();

        assertEquals(Direction.WEST,
                robot.getDirection());
    }

    @Test
    void testRobotDoesNotMoveWhenZeroSteps() {

        Robot robot = new Robot("HAL");

        robot.forward(0);

        assertEquals(0, robot.getX());
        assertEquals(0, robot.getY());
    }

    @Test
    void shouldBlockForwardMovementWhenObstacleExists(){

        World world = new World(10,10);
        Robot robot = new Robot("HAL");
        world.addRobot(robot);
        world.addObstacle(0,1);

        MovementResults result = world.moveForward(robot,1);

        assertFalse(result.isSuccess());

        assertEquals("Obstructed" , result.getMessege());

        assertEquals(0, robot.getX());
        assertEquals(0, robot.getY());
    }
}
