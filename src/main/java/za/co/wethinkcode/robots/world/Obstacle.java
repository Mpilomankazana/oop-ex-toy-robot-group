package za.co.wethinkcode.robots.world;

/**
 * Represents a rectangular obstacle occupying a region of the world grid.
 * Concrete subclasses (e.g. Mountain) define the specific obstacle type
 * and any type-specific behaviour.
 */
public abstract class Obstacle {

    protected final int x;
    protected final int y;
    protected final int width;
    protected final int height;

    protected Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns true if the given coordinate falls anywhere inside
     * this obstacle's rectangular footprint.
     */
    public boolean occupies(int px, int py) {
        return px >= x && px < x + width
            && py >= y && py < y + height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public abstract String getType();
}