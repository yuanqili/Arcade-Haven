package pacman;


public class DoublePoints extends Item {

    /**
     * Activates the item by setting its point worth and duration. It then doubles the amount of points gained by doubling the variable associated with that in the grid file
     * @param grid Is used to access the GridReadCreate variable that decided the worth of each dot.
     * @param pac Not used in this function, but it is a parameter by the Item class that DoublePoints extends.
     * @param timeLimit Not used in this function, but it is a parameter required by the Item class that DoublePoints extends.
     */
    @Override
    void activate(GridReadCreate grid, Pacman pac, int[] timeLimit) {
        pointWorth = 50;
        duration = 10000;
        grid.score += pointWorth;
        grid.pointWorth = grid.pointWorth *2;
    }

    /**
     * Deactivates the item by restoring any values it has originally modified.
     * @param grid Is used to access the GridReadCreatevariable that decided the worth of each dot.
     * @param pacman Not used in this function, but it is a parameter required by the Item class that DoublePoints extends.
     */
    @Override
    public void deactivate(GridReadCreate grid, Pacman pacman) {
        grid.pointWorth = grid.pointWorth / 2;
    }
}
