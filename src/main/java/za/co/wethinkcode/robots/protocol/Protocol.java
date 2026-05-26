package za.co.wethinkcode.robots.protocol;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Protocol {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static   Request parseRequest(String json) {
        try {
            return mapper.readValue(json, Request.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static String buildOkResponse(Object data, StateData state) {
        try {
            Response response = new Response("OK", data, state);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"result\":\"ERROR\",\"data\":{\"message\":\"Could not build response\"}}";
        }
    }

    public static String buildErrorResponse(String message) {
        try {
            Response response = new Response("ERROR", message);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"result\":\"ERROR\",\"data\":{\"message\":\"Unknown error\"}}";
        }
    }
}
