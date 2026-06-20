package za.co.wethinkcode.robots.protocol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StateDataTest {

    @Test
    void shouldStorePositionCorrectly() {

        StateData state = new StateData();

        state.setPosition(new int[] {2, 3});

        assertEquals(2, state.getPosition()[0]);
        assertEquals(3, state.getPosition()[1]);
    }

    @Test
    void  shouldStoreDirectionCorrectly() {

        StateData state = new StateData();

        state.setDirection("NORTH");

        assertEquals("NORTH", state.getDirection());
    }

    @Test
    void shouldStoreShieldValues() {

        StateData state = new StateData();

        state.setShields(5);

        assertEquals(5, state.getShields());
    }

    @Test
    void shouldStoreShotValues() {

        StateData state = new StateData();

        state.setShots(3);

        assertEquals(3, state.getShots());
    }

    @Test
    void shouldStoreStatusCorrectly() {

        StateData state = new StateData();

        state.setStatus("NORMAL");

        assertEquals("NORMAL", state.getStatus());
    }

    @Test
    void shouldCreateUsingConstructor(){

        StateData state =
                new StateData(
                        new int[]{1,2},
                        "NORTH",
                        3,
                        5,
                        "NORMAL"
                );

        assertEquals(1, state.getPosition()[0]);
        assertEquals(2, state.getPosition()[1]);
        assertEquals("NORTH", state.getDirection());
        assertEquals(3, state.getShields());
        assertEquals(5, state.getShots());
        assertEquals("NORMAL", state.getStatus());
    }
}
