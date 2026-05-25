package za.co.wethinkcode.robots.protocol;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


//Tests JSON request parsing.
public class JsonRequestTest {

    //Should parse valid JSON requests.
    @Test
    void shouldParseRequestJson() throws Exception {

        String json = """
                    {
                        "robot":"HAL",
                        "command":"launch",
                        "arguments":["sniper",5,5]
                    }
                """;

        ObjectMapper mapper = new ObjectMapper();
        Request request = mapper.readValue(json, Request.class);
        assertEquals("HAL", request.getRobot());
    }

    //Invalid JSON should fail.
    @Test
    void shouldRejectInvalidJson() {

        ObjectMapper mapper = new ObjectMapper();
        assertThrows(Exception.class, () -> {
            mapper.readValue("{invalid}", Request.class);
        });
    }

    //Command should deserialize correctly.
    @Test
    void shouldReadCommandName() throws Exception {

        String json = """
                    {
                        "robot":"HAL",
                        "command":"state",
                        "arguments":[]
                    }
                """;

        ObjectMapper mapper = new ObjectMapper();
        Request request = mapper.readValue(json, Request.class);
        assertEquals("state", request.getCommand());
    }

    // Empty arguments handled.
    @Test
    void shouldHandleEmptyArguments() throws Exception {

        String json = """
                {
                    "robot":"HAL",
                    "command":"look",
                    "arguments":[]
                }
                
                """;

        ObjectMapper mapper = new ObjectMapper();
        Request request = mapper.readValue(json, Request.class);
        assertEquals(0, request.getArguments().length);
    }

    // Multiple arguments should be parsed.
    @Test
    void shouldParseMultipleArguments() throws Exception {

        String json = """
                {
                    "robot":"HAL",
                    "command":"forward",
                    "arguments":[5]
                }
                """;

        ObjectMapper mapper = new ObjectMapper();
        Request request = mapper.readValue(json, Request.class);
        assertEquals(1, request.getArguments().length);
    }

    // Null arguments should be allowed.
    @Test
    void shouldAllowNullArguments() throws  Exception {

        String json = """
                {
                    "robot":"HAL",
                    "command":"state",
                    "arguments":null
                }
                """;

        ObjectMapper mapper = new ObjectMapper();
        Request request = mapper.readValue(json, Request.class);
        assertNull(request.getArguments());
    }

    // Robot names should be read correctly.
    @Test
    void shouldReadRobotNameCorrectly() throws Exception {

        String json = """
                {
                    "robot":"R2D2",
                    "command":"launch",
                    "arguments":[]
                }
                """;

        ObjectMapper mapper = new ObjectMapper();
        Request request = mapper.readValue(json, Request.class);
        assertEquals("R2D2", request.getRobot());
    }
}
