package za.co.wethinkcode.robots.world.robot;
import org.junit.jupiter.api.Test;

import java.awt.*;

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

    //Robot should turn right correctly.
    @Test
    void shouldTurnRight() {

        Robot robot = new Robot("HAL");

        robot.turnRight();

        assertEquals(Direction.EAST, robot.getDirection());
    }
}
