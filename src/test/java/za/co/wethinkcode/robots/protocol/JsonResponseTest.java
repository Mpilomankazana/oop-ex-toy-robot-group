package za.co.wethinkcode.robots.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import  static  org.junit.jupiter.api.Assertions.*;


// Tests JSON response serialization.
public class JsonResponseTest {

    //Response should serialize to JSON correclty.
    @Test
    void shouldSerializeResponseToJson() throws  Exception {

        Response response = new Response(
                "OK",
                "Robot launched successfully"
        );

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);
        assertTrue(json.contains("OK"));
    }

    //Response should contain message field.
    @Test
    void shouldContainMessageField() throws  Exception {

        Response response = new Respons(
                "OK",
                "Ready"
        );

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);
        assertTrue(json.contains("Ready"));
    }

    // Error responses should serialize correctly.
    @Test
    void shouldSerializeErrorResponse() throws  Exception {

        Response response = new Response(
                "ERROR",
                "Unsupported command"
        );

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);
        assertTrue(json.contains("ERROR"));
    }

    // Response should deserialize correctly.
    @Test
    void shouldDeserializeResponseJson() throws  Exception {

        String json = """
                {
                    "result":"OK,
                    "message":"Done"
                }
                """;

        ObjectMapper mapper = new ObjectMapper();
        Response response = mapper.readValue(json, Response.class);
        assertEquals("OK", response.getResult());
    }

    // Invalid response JSON should fail.
    @Test
    void shouldRejectInvalidResponseJson() {

        ObjectMapper mapper = new ObjectMapper();
        assertThrows(Exception.class, () -> {
            mapper.readValue("{invalid}", Response.class);
        });
    }
}
