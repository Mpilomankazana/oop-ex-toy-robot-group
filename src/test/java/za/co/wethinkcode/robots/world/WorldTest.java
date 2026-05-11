package za.co.wethinkcode.robots.world;
import org.junit.jupiter.api.Test;
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
}