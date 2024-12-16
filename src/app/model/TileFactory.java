package app.model;

import java.util.Stack;

public class TileFactory {
    private final String _path = "./assets/images/";
    public TileFactory()
    {

    }
    public Tile createT()
    {
        Stack<Direction> directions = new Stack<>();
        directions.push(Direction.WEST);
        directions.push(Direction.SOUTH);
        directions.push(Direction.EAST);

        return new Tile(directions, _path + "tile_T.png");
    }
    public Tile createI()
    {
        Stack<Direction> directions = new Stack<>();
        directions.push(Direction.NORTH);
        directions.push(Direction.SOUTH);

        return new Tile(directions, _path + "tile_I.png");
    }
    public Tile createL()
    {
        Stack<Direction> directions = new Stack<>();
        directions.push(Direction.SOUTH);
        directions.push(Direction.EAST);

        return new Tile(directions, _path + "tile_L.png");
    }
}
