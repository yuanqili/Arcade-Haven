package pacman;


public class SpeedUp extends Item {

    int speedIncrement = 1;

    @Override
    void activate(GridReadCreate grid, Pacman pac)
    {
        pointWorth = 50;
        duration = 10000;
        grid.score += pointWorth;
        pac.step += speedIncrement;
    }

    @Override
    public void deactivate(GridReadCreate grid, Pacman pac)
    {
        pac.step -= speedIncrement;
    }
}
