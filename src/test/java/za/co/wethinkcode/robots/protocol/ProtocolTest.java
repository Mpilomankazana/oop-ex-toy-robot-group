package za.co.wethinkcode.robots.protocol;

import org.junit.jupiter.api.Test;

import static  org.junit.jupiter.api.Assertions.*;

public class ProtocolTest {

    @Test
    void shouldParseValidRequest() {

        String json = """
                {
                    "robot":"HAL",
                    "command":"launch",
                    "arguments":[]
                }
                """;

        Request request = Protocol.parseRequest(json);

        assertNotNull(request);
    }

    @Test
    void shouldReturnNullForInvalidJson() {

        Request request = Protocol.parseRequest("{invalid}");

        assertNull(request);
    }

    @Test
    void shouldBuildOkResponse() {

        StateData state = new StateData();
        state.setDirection("NORTH");

        String response = Protocol.buildOkResponse("Done", state);

        assertTrue(response.contains("\"result\":\"OK\""));
        assertTrue(response.contains("Done"));
        assertTrue(response.contains("NORTH"));
    }

    @Test
    void shouldBuildErrorResponse() {

        String response = Protocol.buildErrorResponse("Unsupported command");

        assertTrue(response.contains("\"result\":\"ERROR\""));
        assertTrue(response.contains("Unsupported command"));
    }

    @Test
    void shouldNotIncludeStateInErrorResponse() {

        String response = Protocol.buildErrorResponse("Invalid command");

        assertFalse(response.contains("position"));
        assertFalse(response.contains("direction"));
    }
}
