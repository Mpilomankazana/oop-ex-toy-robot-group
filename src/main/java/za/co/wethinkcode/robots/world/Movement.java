package za.co.wethinkcode.robots.world;
//import za.co.wethinkcode.robots.robot.Robot;

public class Movement{

    private final boolean success;
    private final String message;

    public Movement(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess(){
        return success;
    }

    public String getMessege(){
        return message;
    }

}
