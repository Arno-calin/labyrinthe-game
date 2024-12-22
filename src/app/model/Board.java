package app.model;

import java.util.*;

public class Board {

    private final int SIZE = 7;
    private final Tile[][] _board = new Tile[SIZE][SIZE];

    public Board()
    {

    }

    public void initBoard()
    {
        createBoard(generateTiles());
    }

    /**
     * This function creates the tiles that will be used during the game
     * @return a table of Tiles
     */
    private Tile[] generateTiles() {
        Tile[] set = new Tile[49];
        TileFactory ft = new TileFactory();

        ArrayList<Tile> tiles = new ArrayList<>();

        // Random tiles
        for (int i = 0; i < 16; i++) {
            tiles.add(ft.createL());
        }
        for (int i = 0; i < 12; i++) {
            tiles.add(ft.createI());
        }
        for (int i = 0; i < 6; i++) {
            tiles.add(ft.createT());
        }

        Collections.shuffle(tiles);
        // A random orientation for a random tile
        for (Tile t : tiles) {
            int rotations = new Random().nextInt(4);
            for (int i = 0; i < rotations; i++) {
                t.rotate();
            }
        }

        // A stack enable to not manage the index and just use pop
        Stack<Tile> randomTiles = new Stack<>();
        randomTiles.addAll(tiles);

        // Fixed tiles
        set[0] = ft.createL();
        set[2] = ft.createT();
        set[4] = ft.createT();
        set[6] = ft.createL();
        set[6].rotate();
        set[14] = ft.createT();
        set[14].rotate();
        set[14].rotate();
        set[14].rotate();
        set[16] = ft.createT();
        set[16].rotate();
        set[16].rotate();
        set[16].rotate();
        set[18] = ft.createT();
        set[20] = ft.createT();
        set[20].rotate();
        set[28] = ft.createT();
        set[28].rotate();
        set[28].rotate();
        set[28].rotate();
        set[30] = ft.createT();
        set[30].rotate();
        set[30].rotate();
        set[32] = ft.createT();
        set[32].rotate();
        set[34] = ft.createT();
        set[34].rotate();
        set[42] = ft.createL();
        set[42].rotate();
        set[42].rotate();
        set[42].rotate();
        set[44] = ft.createT();
        set[44].rotate();
        set[44].rotate();
        set[46] = ft.createT();
        set[46].rotate();
        set[46].rotate();
        set[48] = ft.createL();
        set[48].rotate();
        set[48].rotate();

        // Fill the table with tiles that will use
        for (int i = 0; i < set.length; i++) {
            if (set[i] == null) {
                set[i] = randomTiles.pop();
            }
        }

        // Create a stack with all goals
        // When a goal is given this goal is removed
        Stack<Goal> goals = new Stack<>();
        for(Goal g : Goal.values())
            goals.push(g);

        // Give a goal to a random Tile only one time
        for(int i = 0; i < 24; i++) {
            int rdm = new Random().nextInt(49);
            if(set[rdm].existGoal() || rdm == 0 || rdm == 6 || rdm == 42 || rdm == 48) {
                i--;
            }
            else {
                set[rdm].setGoal(goals.pop());
            }
        }

        return set;
    }

    /**
     * Fill the board with a set of tiles, every 7 tiles we change lines
     * @param setTiles the tiles that will use
     */
    private void createBoard(Tile[] setTiles)
    {
        for (int i = 0; i < SIZE * SIZE; i++)
        {
            _board[i/ SIZE][i% SIZE] = setTiles[i];
        }
    }

    /**
     * When we insert a Tile we have to move all tiles in the corresponding column and in the corresponding order
     * @param numCol The column where we want to insert
     * @param newTile The tile we want to insert on the board
     * @return The next single tile, the tile that comes out of the board
     */
    public Tile changeByNorth(int numCol, Tile newTile)
    {
        Tile tempRetour = _board[numCol][SIZE -1];
        for (int i = SIZE -1; i > 0; i--)
        {
            _board[numCol][i] = _board[numCol][i-1];
        }
        _board[numCol][0] = newTile;
        return tempRetour;
    }
    /**
     * When we insert a Tile we have to move all tiles in the corresponding column and in the corresponding order
     * @param numCol The column where we want to insert
     * @param newTile The tile we want to insert on the board
     * @return The next single tile, the tile that comes out of the board
     */
    public Tile changeBySouth(int numCol, Tile newTile)
    {
        Tile tempRetour = _board[numCol][0];
        for (int i = 0; i < SIZE -1; i++)
        {
            _board[numCol][i] = _board[numCol][i+1];
        }
        _board[numCol][SIZE -1] = newTile;
        return tempRetour;
    }
    /**
     * When we insert a Tile we have to move all tiles in the corresponding row and in the corresponding order
     * @param numRow The row where we want to insert
     * @param newTile The tile we want to insert on the board
     * @return The next single tile, the tile that comes out of the board
     */
    public Tile changeByEast(int numRow, Tile newTile)
    {
        Tile tempRetour = _board[0][numRow];
        for (int i = 0; i < SIZE -1; i++)
        {
            _board[i][numRow] = _board[i+1][numRow];
        }
        _board[SIZE -1][numRow] = newTile;
        return tempRetour;
    }
    /**
     * When we insert a Tile we have to move all tiles in the corresponding row and in the corresponding order
     * @param numRow The row where we want to insert
     * @param newTile The tile we want to insert on the board
     * @return The next single tile, the tile that comes out of the board
     */
    public Tile changeByWest(int numRow, Tile newTile)
    {
        Tile tempRetour = _board[SIZE -1][numRow];
        for (int i = SIZE -1; i > 0; i--)
        {
            _board[i][numRow] = _board[i-1][numRow];
        }
        _board[0][numRow] = newTile;
        return tempRetour;
    }

    /**
     *
     * @param numRowCol the number of the row or of the column
     * @return if this row or column is movable
     */
    public boolean isMovable(int numRowCol)
    {
        return numRowCol % 2 == 1;
    }

    public Tile getTileAtPosition(int x, int y)
    {
        return _board[x][y];
    }
    public Tile[][] getBoard(){
        return _board;
    }

    public int getSize() {
        return SIZE;
    }
}
