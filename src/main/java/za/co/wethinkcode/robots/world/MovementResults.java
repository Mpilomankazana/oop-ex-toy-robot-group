package za.co.wethinkcode.robots.world;
//import za.co.wethinkcode.robots.robot.Robot;

public class MovementResults {

    private final boolean success;

    private final String message;

    public MovementResults(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess(){
        return success;
    }


    public String getMessage(){
        return message;
    }

}
