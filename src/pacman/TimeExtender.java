package pacman;


public class TimeExtender extends Item {

    int timeBoost = 10;

    @Override
    void activate(GridReadCreate grid, Pacman pac, int[] timeLimit)
    {
        pointWorth = 50;
        duration = 0;
        grid.score += pointWorth;
        timeLimit[0] += timeBoost;
    }

    @Override
    public void deactivate(GridReadCreate grid, Pacman pac)
    {
    }
}
