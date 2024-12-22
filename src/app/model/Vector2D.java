package app.model;

public class Vector2D {
    private int _x;
    private int _y;

    /**
     *
     * @param x starting address of x
     * @param y starting address of y
     */
    public Vector2D(int x, int y)
    {
        _y = y;
        _x = x;
    }

    public void moveLeft()
    {
        _y--;
    }
    public void moveRight()
    {
        _y++;
    }
    public void moveTop()
    {
        _x--;
    }
    public void moveBottom()
    {
        _x++;
    }

    public void setX(int val){_x = val;}
    public void setY(int val){_y = val;}
    public int getX(){return _x;}
    public int getY(){return _y;}
}
