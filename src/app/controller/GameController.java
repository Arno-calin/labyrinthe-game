package app.controller;

import app.model.*;

public class GameController {
    private final Game _game;

    private boolean _testIfTilePushed = false;

    /**
     *
     * @param game it's controlled game
     */
    public GameController(Game game)
    {
        _game = game;
    }

    /**
     * Initialize the model
     */
    public void initGame()
    {
        _game.initGame();
    }

    public void rotateLeft()
    {
        _game.rotateAloneTile();
    }
    public void rotateRight()
    {
        _game.rotateAloneTile();
        _game.rotateAloneTile();
        _game.rotateAloneTile();
    }

    /**
     * It is not possible to insert twice in the same turn.
     * @param dir is the direction in which the tile will be inserted
     * @param numRowCol is the nth column or row used
     */
    public void pushTileOnBoard(Direction dir, int numRowCol)
    {
        if (!_testIfTilePushed)
            _game.setAloneTile(_game.changeByDirection(dir, numRowCol, _game.getAloneTile()));
        _testIfTilePushed = true;
    }

    /**
     *
     * @param direction is the direction in which the player is heading
     */
    public void movePlayer(Direction direction)
    {
            _game.movePlayer(direction);
    }

    /**
     * We must push a tile onto the board before the end of the turn.
     */
    public void endTurn() {

        if (_testIfTilePushed) {
            if (_game.ifCurrentPlayerWin()) {
                _game.endGame();
            } else {
                _game.nextTurn();
            }
            _testIfTilePushed = false;
        }
    }

    public boolean isMovable(int index)
    {
        return _game.isMovable(index);
    }
}
