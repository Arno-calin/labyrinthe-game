package app.model;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    /**
     *
     * @param current the Direction to change
     * @return the next direction when you turn 90 degrees
     */
    public static Direction next(Direction current)
    {
        return switch (current)
                {
                    case NORTH -> Direction.EAST;
                    case EAST -> Direction.SOUTH ;
                    case SOUTH -> Direction.WEST ;
                    case WEST -> Direction.NORTH ;
                };
    }
}
