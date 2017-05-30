package pacman;


public abstract class Item {
    int pointWorth;
    int duration;

    abstract void activate(GridReadCreate grid, Pacman pac);

    public abstract void deactivate(GridReadCreate grid, Pacman pacman);
}