package za.co.wethinkcode.robots.robot;


public class Robot {

    private final String name;
    private final String make;
    private int x;
    private int y;
    private Direction direction;
    private int shields;
    private int shots;
    private String status;


    public Robot(String name) {
        this(name, "sniper");
    }

    public Robot(String name, String make) {
        this.name      = name;
        this.make      = make;
        this.x         = 0;
        this.y         = 0;
        this.direction = Direction.NORTH;
        this.status    = "NORMAL";

        switch (make.toLowerCase()) {
            case "tank" -> {
                this.shields = 6;
                this.shots   = 2;
            }
            case "scout" -> {
                this.shields = 0;
                this.shots   = 1;
            }
            default -> {
                this.shields = 3;
                this.shots   = 6;
            }
        }
    }

    public Robot(String name, int x, int y) {
        this(name, "sniper", x, y);
    }

    public Robot(String name, String make, int x, int y) {
        this(name, make);
        this.x = x;
        this.y = y;
    }


    public void forward(int steps) {
        switch (direction) {
            case NORTH -> y += steps;
            case SOUTH -> y -= steps;
            case EAST  -> x += steps;
            case WEST  -> x -= steps;
        }
    }

    public void back(int steps) {
        forward(-steps);
    }

    public void turnRight() {
        direction = direction.turnRight();
    }


    public void turnLeft() {
        direction = direction.turnLeft();
    }

    public String getName()      {
        return name;
    }
    public String getMake()      {
        return make;
    }
    public int    getX()         {
        return x;
    }
    public int    getY()         {
        return y;
    }
    public Direction getDirection() {
        return direction;
    }
    public int    getShields()   {
        return shields;
    }
    public int    getShots()     {
        return shots;
    }
    public String getStatus()    {
        return status;
    }

    public void setX(int x)              {
        this.x = x;
    }
    public void setY(int y)              {
        this.y = y;
    }
    public void setShields(int shields)  {
        this.shields = shields;
    }
    public void setShots(int shots)      {
        this.shots = shots;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setDirection(Direction d){
        this.direction = d;
    }

    @Override
    public String toString() {
        return name + " at (" + x + "," + y + ") facing "
                + direction + " | shields:" + shields
                + " shots:" + shots + " [" + status + "]";
    }
}


