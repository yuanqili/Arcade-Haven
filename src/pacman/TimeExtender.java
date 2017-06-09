package pacman;


public class TimeExtender extends Item {

    /**
     * This variable decides how much the time limit is extended by.
     */
    int timeBoost = 10;

    /**
     * Activates the item by setting its point worth and duration. It increments the speed of the character by the value of speedIncrement.
     * @param grid Is used to increment the current score.
     * @param pac Not used in this function, but it is a parameter required by the Item class that TimeExtender extends.
     * @param timeLimit It gets incremented by the amount timeBoost designates.
     */
    @Override
    void activate(GridReadCreate grid, Pacman pac, int[] timeLimit)
    {
        pointWorth = 50;
        duration = 0;
        grid.score += pointWorth;
        timeLimit[0] += timeBoost;
    }

    /**
     * There are no variables to restore for this function, but it is needed to extend the Item class.
     * @param grid Not used in this function, but it is a parameter required by the Item class that TimeExtender extends.
     * @param pac Not used in this function, but it is a parameter required by the Item class that TimeExtender extends.
     */
    @Override
    public void deactivate(GridReadCreate grid, Pacman pac)
    {
    }
}
