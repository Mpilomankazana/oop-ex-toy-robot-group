package za.co.wethinkcode.robots.robot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    void northTurnsRightToEast(){
        assertEquals(Direction.EAST, Direction.NORTH.turnRight());
    }

    @Test
    void northTurnsLeftToWest(){
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft());
    }

    @Test
    void westTurnsRightToNorth(){
        assertEquals(Direction.NORTH, Direction.WEST.turnRight());
    }

    @Test
    void eastTurnsLeftToNorth(){
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft());
    }

}
