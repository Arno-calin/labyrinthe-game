package app.model;

import java.util.*;

public class Game {

    private final List<BoardObserver> _observers = new ArrayList<>();
    private final PlayerManagement _players = new PlayerManagement();
    private Tile _aloneTile;
    private final Board _board = new Board();
    private final ArrayList<Direction> _possibleDirectionsOfCurrentPlayer = new ArrayList<>();
    private GameState _gameState = GameState.WAITING;

    public Game()
    {

    }

    public void initGame()
    {
        // generate the board
        _board.initBoard();
        // generate the alone tile
        TileFactory ft = new TileFactory();
        _aloneTile = ft.createI();
        // generate players
        _players.generatePlayers();
        // change the status
        _gameState = GameState.PLAYING;
        // notify views
        notifyGameStatus();
        notifyObserversPlayer();
        notifyObserversCurrentPlayer();
        notifyPossibleDirections();
        notifyObserversTile();
        notifyObserversBoard();
        notifyObserversBoard();
    }

    /**
     *
     * @param dir the direction where the tile will be inserted
     * @param numRowCol the row or the column where the tile will be inserted
     * @param newTile the old alone tile, the tile to insert
     * @return the new alone tile, the ejected tile
     */
    public Tile changeByDirection(Direction dir, int numRowCol, Tile newTile)
    {
        if (!_board.isMovable(numRowCol))
            throw new IllegalArgumentException("You are not allowed to push a card to this location");

        Tile tempRetour = null;
        switch (dir){
            case NORTH -> {tempRetour = _board.changeByNorth(numRowCol, newTile);

                for (Map.Entry<Player, Vector2D> entry : _players.getPlayer().entrySet()) {
                    Vector2D vector = entry.getValue();
                    // checks whether a player should be moved with the tiles
                    if (vector.getX() == numRowCol)
                        vector.moveRight();
                    // If a player has to be teleported to the other end of the board
                    if (vector.getY() > _board.getSize()-1)
                        vector.setY(0);
                }
            }
            case EAST -> {tempRetour = _board.changeByEast(numRowCol, newTile);

                for (Map.Entry<Player, Vector2D> entry : _players.getPlayer().entrySet()) {
                    Vector2D vector = entry.getValue();
                    // checks whether a player should be moved with the tiles
                    if (vector.getY() == numRowCol)
                        vector.moveTop();
                    // If a player has to be teleported to the other end of the board
                    if (vector.getX() < 0)
                        vector.setX(_board.getSize()-1);
                }
            }
            case SOUTH ->{tempRetour = _board.changeBySouth(numRowCol, newTile);

                for (Map.Entry<Player, Vector2D> entry : _players.getPlayer().entrySet()) {
                    Vector2D vector = entry.getValue();
                    // checks whether a player should be moved with the tiles
                    if (vector.getX() == numRowCol)
                        vector.moveLeft();
                    // If a player has to be teleported to the other end of the board
                    if (vector.getY() < 0)
                        vector.setY(_board.getSize()-1);
                }
            }
            case WEST ->{tempRetour = _board.changeByWest(numRowCol, newTile);

                for (Map.Entry<Player, Vector2D> entry : _players.getPlayer().entrySet()) {
                    Vector2D vector = entry.getValue();
                    // checks whether a player should be moved with the tiles
                    if (vector.getY() == numRowCol)
                        vector.moveBottom();
                    // If a player has to be teleported to the other end of the board
                    if (vector.getX() > _board.getSize()-1)
                        vector.setX(0);
                }
            }
        }
        changePossibleDirection();
        notifyPossibleDirections();
        notifyObserversBoard();
        return tempRetour;
    }


    public Tile getAloneTile()
    {
        return _aloneTile;
    }
    public void setAloneTile(Tile tile)
    {
        _aloneTile = tile;
        notifyObserversTile();
    }

    public void addObserver(BoardObserver observer) {
        _observers.add(observer);
    }

    public void removeObserver(BoardObserver observer) {
        _observers.remove(observer);
    }
    public void removeObserver(int index) {
        _observers.remove(index);
    }

    public void notifyObserversCurrentPlayer() {
        for (BoardObserver obs : _observers) {
            obs.updateCurrentPlayer(_players.getCurrentPlayer());
        }
    }

    public void notifyObserversBoard() {
        for (BoardObserver obs : _observers) {
            obs.updateBoard(_board.getBoard(), _players.getPlayer());
        }
    }

    public void notifyObserversPlayer() {
        for (BoardObserver obs : _observers) {
            obs.updatePlayers(_players.getPlayer());
        }
    }
    public void notifyObserversTile() {
        for (BoardObserver obs : _observers) {
            obs.updateTile(this.getAloneTile());
        }
    }

    public void notifyPossibleDirections() {
        for (BoardObserver obs : _observers) {
            obs.updatePossibleDirections(this._possibleDirectionsOfCurrentPlayer);
        }
    }

    public void notifyGameStatus() {
        for (BoardObserver obs : _observers) {
            if(_gameState == GameState.ENDED) {
                obs.updateGameStatus(_gameState, _players.getCurrentPlayer());
            } else {
                obs.updateGameStatus(_gameState);
            }
        }
    }

    public void rotateAloneTile()
    {
        _aloneTile.rotate();
        notifyObserversTile();
    }

    /**
     * Changes the possible directions for the current player
     * Checks whether the player is against an edge and whether the adjacent tiles allow him to move.
     */
    public void changePossibleDirection()
    {
        _possibleDirectionsOfCurrentPlayer.clear();
        Vector2D vector2 = _players.getPositionCurrentPlayer();
        int x = vector2.getX();
        int y = vector2.getY();

        if(x != 0 && _board.getTileAtPosition(x-1, y).getDirection().contains(Direction.SOUTH) && _board.getTileAtPosition(x, y).getDirection().contains(Direction.NORTH))
            _possibleDirectionsOfCurrentPlayer.add(Direction.NORTH);
        if(x != _board.getSize()-1 && _board.getTileAtPosition(x+1, y).getDirection().contains(Direction.NORTH) && _board.getTileAtPosition(x, y).getDirection().contains(Direction.SOUTH))
            _possibleDirectionsOfCurrentPlayer.add(Direction.SOUTH);
        if(y != 0 && _board.getTileAtPosition(x, y-1).getDirection().contains(Direction.EAST) && _board.getTileAtPosition(x, y).getDirection().contains(Direction.WEST))
            _possibleDirectionsOfCurrentPlayer.add(Direction.WEST);
        if(y != _board.getSize()-1 && _board.getTileAtPosition(x, y+1).getDirection().contains(Direction.WEST) && _board.getTileAtPosition(x, y).getDirection().contains(Direction.EAST))
            _possibleDirectionsOfCurrentPlayer.add(Direction.EAST);
    }

    public void movePlayer(Direction direction)
    {
        _players.moveCurrentPlayer(direction);
        changePossibleDirection();

        notifyPossibleDirections();
        notifyObserversBoard();
        notifyObserversCurrentPlayer();
        notifyObserversPlayer();
    }

    public void isCurrentPlayerFindItsGoal()
    {
        Tile tile = getTileCurrentPlayer();
        if (tile.existGoal())
        {
            if (tile.getGoal() == _players.getCurrentGoalCurrentPlayer())
                _players.nextGoalCurrentPlayer();
        }
    }

    public boolean ifCurrentPlayerWin()
    {
        return _players.ifCurrentPlayerRestGoal() && _players.ifCurrentPlayerHasComeBack();
    }

    public Tile getTileCurrentPlayer()
    {
        Vector2D position = _players.getPositionCurrentPlayer();
        return _board.getTileAtPosition(position.getX(), position.getY());
    }

    public boolean isMovable(int numRowCol) {
        return _board.isMovable(numRowCol);
    }

    public void nextTurn()
    {
        isCurrentPlayerFindItsGoal();
        notifyObserversPlayer();
        _players.nextPlayer();
        _possibleDirectionsOfCurrentPlayer.clear();// to prevent the player from moving before inserting
        notifyPossibleDirections();
        notifyObserversCurrentPlayer();
        notifyObserversPlayer();
    }
    public void endGame()
    {
        _gameState = GameState.ENDED;
        notifyGameStatus();
        System.exit(0);
    }
}
