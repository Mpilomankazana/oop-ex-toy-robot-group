package za.co.wethinkcode.robots.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents a JSON response sent from the server to a client.
 * Jackson serialises this object into a JSON string.
 * The state field is only included when it is not null.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private String result;
    private Object data;
    private Object state;

    /** Required by Jackson for deserialisation. */
    public Response() {}

    /**
     * Creates a response with result, data, and state.
     * @param result "OK" or "ERROR"
     * @param data   command-specific data object
     * @param state  robot state — null for ERROR responses
     */
    public Response(String result, Object data, Object state) {
        this.result = result;
        this.data   = data;
        this.state  = state;
    }

    /**
     * Creates a response with result and data only.
     * Used for ERROR responses where state is not included.
     * @param result "OK" or "ERROR"
     * @param data   data object or message string
     */
    public Response(String result, Object data) {
        this.result = result;
        this.data   = data;
        this.state  = null;
    }

    public String getResult() { return result; }
    public Object getData()   { return data; }
    public Object getState()  { return state; }

    public void setResult(String result) { this.result = result; }
    public void setData(Object data)     { this.data = data; }
    public void setState(Object state)   { this.state = state; }
}