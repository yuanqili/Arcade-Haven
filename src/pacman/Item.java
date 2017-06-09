package pacman;

/**
 * An abstract class for power up items.
 */
public abstract class Item {
    /**
     * Designates the points to be added to the score once the item is picked up.
     */
    int pointWorth;

    /**
     * Designates the duration of the power-up
     */
    int duration;

    abstract void activate(GridReadCreate grid, Pacman pac, int[] timeLimit);

    public abstract void deactivate(GridReadCreate grid, Pacman pacman);
}
