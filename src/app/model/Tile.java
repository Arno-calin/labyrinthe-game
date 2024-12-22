    package app.model;

import java.util.ArrayList;
import java.util.Stack;

public class Tile {
    private final String _pathImg;
    private final ArrayList<Direction> _possibleDirections = new ArrayList<>();
    private Goal _goal = null;

    /**
     *
     * @param form The different directions allowed by the tile
     * @param path The path of the image corresponding to the tile
     */
    public Tile(Stack<Direction> form, String path)
    {
        _pathImg = path;
        while (!form.empty())
            _possibleDirections.add(form.pop());
    }

    public void rotate() {
        _possibleDirections.replaceAll(Direction::next);
    }

    /**
     *
     * @return if this Tile have a Goal
     */
    public boolean existGoal()
    {
        return _goal != null;
    }

    public ArrayList<Direction> getDirection()
    {
        return _possibleDirections;
    }

    public void setGoal(Goal goal) 
    {
        _goal = goal;
    }

    public Goal getGoal()
    {
        if (existGoal())
            return _goal;
        else return null;
    }

    public String getPathImg()
    {
        return _pathImg;
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for (Direction possibleDirection : _possibleDirections){ str.append(possibleDirection.toString()); str.append(" ");}
        return str.toString();
    }
}
