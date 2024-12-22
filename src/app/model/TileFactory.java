package app.model;

import java.util.Stack;

public class TileFactory {
    private final String PATH = "./assets/images/";
    public TileFactory()
    {

    }

    /**
     *
     * @return an T-shaped tile with corresponding directions and its image
     */
    public Tile createT()
    {
        Stack<Direction> directions = new Stack<>();
        directions.push(Direction.WEST);
        directions.push(Direction.SOUTH);
        directions.push(Direction.EAST);

        return new Tile(directions, PATH + "tile_T.png");
    }
    /**
     *
     * @return an I-shaped tile with corresponding directions and its image
     */
    public Tile createI()
    {
        Stack<Direction> directions = new Stack<>();
        directions.push(Direction.NORTH);
        directions.push(Direction.SOUTH);

        return new Tile(directions, PATH + "tile_I.png");
    }
    /**
     *
     * @return an L-shaped tile with corresponding directions and its image
     */
    public Tile createL()
    {
        Stack<Direction> directions = new Stack<>();
        directions.push(Direction.SOUTH);
        directions.push(Direction.EAST);

        return new Tile(directions, PATH + "tile_L.png");
    }
}
