package za.co.wethinkcode.robots.world;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovementResultsTest {

    @Test
    void shouldStoreSuccessFlag(){

        MovementResults result =
                new MovementResults(true, "Moved");
    }

    @Test
    void shouldStoreMessage(){
        MovementResults result =
                new MovementResults(false, "Blocked");

        assertEquals("Blocked", result.getMessage());
    }
}
