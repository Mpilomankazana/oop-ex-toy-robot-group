package za.co.wethinkcode.robots.world;

import za.co.wethinkcode.robots.robot.Robot;
import java.util.ArrayList;
import java.util.List;


public class World {

    private final int width;
    private final int height;
    private int visibility;
    private int repairTime;
    private int reloadTime;
    private int maxShields;
    private int maxShots;

    private final List<Robot> robots;
    private final List<Obstacle> obstacles;


    public World(int width, int height){
        this.width = width;
        this.height = height;
        this.visibility = 5;
        this.repairTime = 5;
        this.reloadTime = 5;
        this.maxShields = 3;
        this.maxShots = 5;
        this.robots = new ArrayList<>();
        this.obstacles = new ArrayList<>();
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
        this.repairTime = Integer.parseInt(config.getProperty("repair.time", "5"));
        this.reloadTime = Integer.parseInt(config.getProperty("reload.time", "5"));
        this.maxShields = Integer.parseInt(config.getProperty("max.shields", "3"));
        this.maxShots   = Integer.parseInt(config.getProperty("max.shots", "5"));
        this.robots     = new ArrayList<>();
        this.obstacles  = new ArrayList<>();
        loadObstacles(config);
    }

    /**
     * Reads obstacle positions from the config file and adds them to the world.
     * Supports an optional width and height per obstacle (defaults to 1x1
     * if not specified) so obstacles can be rectangular. All config-loaded
     * obstacles are created as Mountains by default.
     * @param config the loaded Properties object
     */
    private void loadObstacles(java.util.Properties config) {
        int count = Integer.parseInt(config.getProperty("obstacle.count", "0"));
        for (int i = 1; i <= count; i++) {
            String[] parts = config.getProperty("obstacle." + i).split(",");
            int ox = Integer.parseInt(parts[0].trim());
            int oy = Integer.parseInt(parts[1].trim());
            int w  = parts.length > 2 ? Integer.parseInt(parts[2].trim()) : 1;
            int h  = parts.length > 3 ? Integer.parseInt(parts[3].trim()) : 1;
            addObstacle(new Mountain(ox, oy, w, h));
        }
    }

    /**
     * Returns the visibility range — how many steps a robot can see.
     * @return visibility in steps
     */
    public int getVisibility() { return visibility;}

    /**
     * Returns the repair time in seconds - how long repair blocks movement.
     * @return repair time in seconds
     */
    public int getRepairTime() { return repairTime; }

    /**
     * Returns the reload time in seconds — how long reload blocks movement.
     * @return reload time in seconds
     */
    public int getReloadTime() { return reloadTime; }

    /**
     * Returns the maximum shield strength a robot can have.
     * @return maximum shield strength
     */
    public int getMaxShields() { return maxShields; }

    /**
     * Returns the maximum number of shots a robot can have.
     * @return maximum shots
     */
    public int getMaxShots() { return maxShots; }

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

    /**
     * Adds a single-point (1x1) Mountain obstacle at the given coordinate.
     * Kept for backward compatibility with existing callers.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public synchronized void addObstacle(int x, int y) {
        addObstacle(new Mountain(x, y, 1, 1));
    }

    /**
     * Adds an already-constructed Obstacle (Mountain, Lake, or Pit) to the world.
     * @param obstacle the obstacle to add
     */
    public synchronized void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    /**
     * Returns true if the given coordinate falls inside any obstacle's
     * rectangular footprint.
     * @param x x-coordinate to check
     * @param y y-coordinate to check
     * @return true if an obstacle occupies that coordinate
     */
    public boolean hasObstacle(int x, int y) {
        for (Obstacle o : obstacles) {
            if (o.occupies(x, y)) return true;
        }
        return false;
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

    public synchronized MovementResults moveForward(Robot robot, int steps){

        int newX = robot.getX();
        int newY = robot.getY();
        switch (robot.getDirection()){

            case NORTH -> newY += steps;
            case SOUTH -> newY -= steps;
            case EAST -> newX += steps;
            case WEST -> newX -= steps;
        }
        if (isBlocked(newX, newY)) {
            return  new MovementResults(false, "Obstructed");
        }

        robot.setX(newX);
        robot.setY(newY);

        return new MovementResults(true, "Moved");

    }

    public  synchronized MovementResults moveBack(Robot robot, int steps){
        return  moveForward(robot, -steps);
    }
}
