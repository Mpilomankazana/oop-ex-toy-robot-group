package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.robot.Direction;
import za.co.wethinkcode.robots.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameEngine {

    public int moveForward(Robot robot, int steps, World world) {

        int moved = 0;

        for (int i = 0; i < steps; i++) {
            int nextX = robot.getX();
            int nextY = robot.getY();

            switch (robot.getDirection()) {
                case NORTH ->
                    nextY += 1;
                case SOUTH ->
                    nextY -= 1;
                case EAST ->
                    nextX += 1;
                case WEST ->
                    nextX -= 1;

            }
            if (world.isBlocked(nextX, nextY)) {
                break;
            }
            robot.setX(nextX);
            robot.setY(nextY);
            moved++;

        }
        world.moveRobot(robot.getName(), robot.getX(), robot.getY());
        return moved;
    }

    public int moveBack(Robot robot, int steps, World world) {
        int moved = 0;

        for (int i = 0; i < steps; i++) {
            int nextX = robot.getX();
            int nextY = robot.getY();

            switch (robot.getDirection()) {
                case NORTH ->
                    nextY -= 1;
                case SOUTH ->
                    nextY += 1;
                case EAST ->
                    nextX -= 1;
                case WEST ->
                    nextX += 1;
            }

            if (world.isBlocked(nextX, nextY)) {
                break;
            }

            robot.setX(nextX);
            robot.setY(nextY);
            moved++;
        }

        world.moveRobot(robot.getName(), robot.getX(), robot.getY());
        return moved;
    }

    public Direction turn(Robot robot, String turnDir) {
        if (turnDir == null) {
            return null;
        }

        switch (turnDir.trim().toLowerCase()) {
            case "right" ->
                robot.setDirection(robot.getDirection().turnRight());
            case "left" ->
                robot.setDirection(robot.getDirection().turnLeft());
            default -> {
                return null;
            }
        }
        return robot.getDirection();

    }
    private static final int DEFAULT_SHOT_DISTANCE = 5;

    public FireResult fire(Robot shooter, World world) {
        return fire(shooter, world, DEFAULT_SHOT_DISTANCE);
    }

    public FireResult fire(Robot shooter, World world, int shotDistance) {
        if (shooter.getShots() <= 0) {
            return new FireResult("ERROR", -1, 0);
        }

        shooter.setShots(shooter.getShots() - 1);

        for (int dist = 1; dist <= shotDistance; dist++) {
            int tx = shooter.getX();
            int ty = shooter.getY();

            switch (shooter.getDirection()) {
                case NORTH ->
                    ty += dist;
                case SOUTH ->
                    ty -= dist;
                case EAST ->
                    tx += dist;
                case WEST ->
                    tx -= dist;
            }

            Robot target = world.getRobotAt(tx, ty);
            if (target != null) {
                target.setShields(target.getShields() - 1);
                if (target.getShields() <= 0) {
                    target.setStatus("DEAD");
                    world.removeRobot(target.getName());
                    return new FireResult("KILL", dist, shooter.getShots());
                }
                return new FireResult("HIT", dist, shooter.getShots());
            }

            if (world.hasObstacle(tx, ty)) {
                break;
            }
        }

        return new FireResult("MISS", -1, shooter.getShots());
    }

    public void repair(Robot robot, World world) {
        robot.setStatus("REPAIR");
        try {
            Thread.sleep(world.getRepairTime() * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        robot.setShields(world.getMaxShields());
        robot.setStatus("NORMAL");
    }

    public List<Map<String, Object>> look(Robot robot, World world) {
        List<Map<String, Object>> objects = new ArrayList<>();
        int visibility = world.getVisibility();

        for (Direction dir : Direction.values()) {
            boolean found = false;

            for (int dist = 1; dist <= visibility; dist++) {
                int cx = robot.getX();
                int cy = robot.getY();

                switch (dir) {
                    case NORTH ->
                        cy += dist;
                    case SOUTH ->
                        cy -= dist;
                    case EAST ->
                        cx += dist;
                    case WEST ->
                        cx -= dist;
                }

                if (!world.isInsideWorld(cx, cy)) {
                    objects.add(sighting(dir.name(), "EDGE", dist));
                    found = true;
                    break;
                }
                if (world.hasObstacle(cx, cy)) {
                    objects.add(sighting(dir.name(), "OBSTACLE", dist));
                    found = true;
                    break;
                }
                Robot other = world.getRobotAt(cx, cy);
                if (other != null && !other.getName().equals(robot.getName())) {
                    objects.add(sighting(dir.name(), "ROBOT", dist));
                    found = true;
                    break;
                }
            }

            if (!found) {
                objects.add(sighting(dir.name(), "NONE", -1));
            }
        }

        return objects;
    }

    private Map<String, Object> sighting(String direction, String type, int distance) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("direction", direction);
        entry.put("type", type);
        entry.put("distance", distance);
        return entry;
    }

    public static class FireResult {

        public final String outcome;
        public final int distance;
        public final int shotleft;

        public FireResult(String outcome, int distance, int shotleft) {
            this.outcome = outcome;
            this.distance = distance;
            this.shotleft = shotleft;
        }
    }

    public Robot launch(String name, World world) {
        return launch(name, "sniper", world);
    }

    public Robot launch(String name, String make, World world) {

        if (world.getRobot(name) != null) {
            return null;
        }
        Random random = new Random();
        int halfWidth = world.getWidth() / 2;
        int halfHeight = world.getHeight() / 2;
        int x, y;
        int attempts = 0;

        do {
            x = random.nextInt(world.getWidth()) - halfWidth;
            y = random.nextInt(world.getHeight()) - halfHeight;
            attempts++;
            if (attempts > 1000) {
                return null;
            }
        } while (world.isBlocked(x, y));
        Robot robot = new Robot(name, make, x, y);
        world.addRobot(robot);
        return robot;
    }

}
