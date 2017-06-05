package pacman;


public class DoublePoints extends Item {

    @Override
    void activate(GridReadCreate grid, Pacman pac, int[] timeLimit) {
        pointWorth = 50;
        duration = 10000;
        grid.pointWorth = grid.pointWorth *2;
    }

    @Override
    public void deactivate(GridReadCreate grid, Pacman pacman) {
        grid.pointWorth = grid.pointWorth / 2;
    }
}
