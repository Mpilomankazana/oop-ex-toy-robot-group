package za.co.wethinkcode.robots.world;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldConfigTest {

    @Test
    void testConfigLoadsRepairTime() throws Exception {

        World world = new World("config.properties");

        assertEquals(
                5,
                world.getRepairTime()
        );
    }

    @Test
    void testConfigLoadsReloadTime() throws Exception {

        World world = new World("config.properties");

        assertEquals(
                5,
                world.getReloadTime()
        );
    }

    @Test
    void testConfigLoadsMaxShields() throws Exception {

        World world = new World("config.properties");

        assertEquals(
                3,
                world.getMaxShields()
        );
    }

    @Test
    void shouldLoadPositiveRepairTime() throws Exception {

        World world = new World("config.properties");

        assertTrue(
                world.getRepairTime() > 0
        );
    }

    @Test
    void shouldLoadPositiveReloadTime() throws Exception {

        World world = new World("config.properties");

        assertTrue(
                world.getReloadTime() > 0
        );
    }

    @Test
    void shouldLoadPositiveMaxShields() throws Exception {

        World world = new World("config.properties");

        assertTrue(
                world.getMaxShields() > 0
        );
    }
}
