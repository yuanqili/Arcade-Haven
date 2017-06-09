package pacman;


public class SpeedUp extends Item {

    /**
     * This value decides how much the character's speed is incremented by.
     */
    int speedIncrement = 1;

    /**
     * Activates the item by setting its point worth and duration. It increments the speed of the character by the value of speedIncrement.
     * @param grid Is used to increment the current score.
     * @param pac Used to access the step variable in the pacman character.
     * @param timeLimit Not used in this function, but it is a parameter required by the Item class that SpeedUp extends.
     */
    @Override
    void activate(GridReadCreate grid, Pacman pac, int[] timeLimit)
    {
        pointWorth = 50;
        duration = 10000;
        grid.score += pointWorth;
        pac.step += speedIncrement;
    }

    /**
     * Deactivates the item by restoring any values it has originally modified.
     * @param grid Not used in this function, but it is a parameter required by the Item class that SpeedUp extends.
     * @param pac Used to access the step variable to restore it to the default quantity.
     */
    @Override
    public void deactivate(GridReadCreate grid, Pacman pac)
    {
        pac.step -= speedIncrement;
    }
}
