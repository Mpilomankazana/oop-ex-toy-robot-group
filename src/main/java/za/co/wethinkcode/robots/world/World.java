package za.co.wethinkcode.robots.world;

import za.co.wethinkcode.robots.world.robot.Robot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class World {

    private final int width;
    private final int height;
    private final List<Robot> robots;
    private final Set<String> obstaclePositions;


    public World(int width, int height) {
        this.width              = width;
        this.height             = height;
        this.robots             = new ArrayList<>();
        this.obstaclePositions  = new HashSet<>();
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

    public int getWidth()  { return width; }
    public int getHeight() { return height; }


    private String key(int x, int y) {
        return x + "," + y;
    }
}