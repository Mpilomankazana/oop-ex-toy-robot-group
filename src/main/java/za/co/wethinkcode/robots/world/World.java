package za.co.wethinkcode.robots.world;

import za.co.wethinkcode.robots.robot.Robot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class World {

    private final int width;
    private final int height;
    private int visibility;
    private final List<Robot> robots;
    private final Set<String> obstaclePositions;


    public World(int width, int height) {
        this.width              = width;
        this.height             = height;
        this.visibility         = 5;
        this.robots             = new ArrayList<>();
        this.obstaclePositions  = new HashSet<>();
    }
    /**
     * Creates a World loaded from a config.properties file.
     * Reads world size, visibility, and obstacle positions from the file.
     * @param configPath path to the config.properties file
     * @throws Exception if the file cannot be read or parsed
     */
    public World(String configPath) throws Exception {
        java.util.Properties config = new java.util.Properties();
        config.load(new java.io.FileInputStream(configPath));
        this.width      = Integer.parseInt(config.getProperty("world.width"));
        this.height     = Integer.parseInt(config.getProperty("world.height"));
        this.visibility = Integer.parseInt(config.getProperty("visibility"));
        this.robots             = new ArrayList<>();
        this.obstaclePositions  = new HashSet<>();
        loadObstacles(config);
    }

    /**
     * Reads obstacle positions from the config file and adds them to the world.
     * @param config the loaded Properties object
     */
    private void loadObstacles(java.util.Properties config) {
        int count = Integer.parseInt(config.getProperty("obstacle.count", "0"));
        for (int i = 1; i <= count; i++) {
            String[] parts = config.getProperty("obstacle." + i).split(",");
            addObstacle(Integer.parseInt(parts[0].trim()),
                    Integer.parseInt(parts[1].trim()));
        }
    }

    /**
     * Returns the visibility range — how many steps a robot can see.
     * @return visibility in steps
     */
    public int getVisibility() { return visibility; }

    /**
     * Returns the robot at the given position, or null if the position is empty.
     * @param x x-coordinate to check
     * @param y y-coordinate to check
     * @return the Robot at (x,y) or null
     */
    public synchronized Robot getRobotAt(int x, int y) {
        for (Robot r : robots) {
            if (r.getX() == x && r.getY() == y) return r;
        }
        return null;
    }

    /**
     * Updates a robot's stored position after movement.
     * @param name the robot's name
     * @param newX new x coordinate
     * @param newY new y coordinate
     */
    public synchronized void moveRobot(String name, int newX, int newY) {
        Robot r = getRobot(name);
        if (r != null) {
            r.setX(newX);
            r.setY(newY);
        }
    }


    public synchronized void addObstacle(int x, int y) {

        obstaclePositions.add(key(x, y));
    }


    public boolean hasObstacle(int x, int y) {

        return obstaclePositions.contains(key(x, y));
    }


    public boolean isInsideWorld(int x, int y) {
        return x >= -(width / 2)  && x <= (width / 2)
                && y >= -(height / 2) && y <= (height / 2);
    }


    public synchronized boolean isBlocked(int x, int y) {
        if (!isInsideWorld(x, y))       return true;
        if (hasObstacle(x, y))          return true;
        for (Robot r : robots) {
            if (r.getX() == x && r.getY() == y) return true;
        }
        return false;
    }


    public synchronized void addRobot(Robot robot) {
        robots.add(robot);
    }


    public synchronized void removeRobot(String name) {

        robots.removeIf(r -> r.getName().equals(name));
    }


    public synchronized Robot getRobot(String name) {
        for (Robot r : robots) {
            if (r.getName().equals(name)) return r;
        }
        return null;
    }


    public synchronized List<Robot> getRobots() {

        return new ArrayList<>(robots);
    }

    public int getWidth()  {
        return width;
    }
    public int getHeight() {
        return height;
    }


    private String key(int x, int y) {

        return x + "," + y;
    }

    public synchronized Movement moveForward(Robot robot,int steps){

        int newX = robot.getX();
        int newY = robot.getY();
        switch (robot.getDirection()){

            case NORTH -> newY += steps;
            case SOUTH -> newY -= steps;
            case EAST -> newX += steps;
            case WEST -> newX -= steps;
        }
        if (isBlocked(newX, newY)) {
            return  new Movement(false, "Obstructed");
        }

        robot.setX(newX);
        robot.setY(newY);

        return new Movement(true, "Moved");

    }

    public  synchronized Movement moveBack(Robot robot, int steps){
        return  moveForward(robot, -steps);
    }
}