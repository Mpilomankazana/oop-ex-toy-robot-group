package za.co.wethinkcode.robots.protocol;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
public class Request {
    private String robot;
    private String command;
    private Object[] arguments;

    public Request() {}

    public String getRobot() {
        return  robot;
    }
    public String getCommand(){
        return command;
    }

    public Object[] getArguments() {
        return arguments;
    }
    public void setRobot(String robot) {
        this.robot = robot;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
