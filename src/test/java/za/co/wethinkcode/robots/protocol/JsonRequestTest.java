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
}
