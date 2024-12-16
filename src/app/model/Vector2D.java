package app.model;

public class Vector2D {
    private int _x;
    private int _y;

    public Vector2D(int x, int y)
    {
        _x = y;
        _y = x;
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

    public void initLeft()
    {
        _y = 6;
    }
    public void initRight()
    {
        _y = 0;
    }
    public void initTop()
    {
        _x = 6;
    }
    public void initBottom()
    {
        _x = 0;
    }
    public int getX(){return _x;}
    public int getY(){return _y;}
}
