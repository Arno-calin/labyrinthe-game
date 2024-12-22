package app.model;

import java.util.*;

public class PlayerManagement {

    private final int PLAYER_NUMBER = 4;
    private final HashMap<Player, Vector2D> _playersPositions = new HashMap<>();
    private final Player[] _players = new Player[PLAYER_NUMBER];
    private Integer _currentPlayer = 0;
    private final String[] NAME = {"jaune", "bleu", "vert", "rouge"};

    public PlayerManagement()
    {

    }

    /**
     * This function create the link between a player and its position
     */
    public void generatePlayers()
    {
        ArrayList<Goal> goals = new ArrayList<>(Arrays.asList(Goal.values()));
        Collections.shuffle(goals);

        for(int i = 0; i < PLAYER_NUMBER; i++) {
            Stack<Goal> _goals = new Stack<>();

            Vector2D position;
            switch (i) {
                case 0 -> position = new Vector2D(0, 0);
                case 1 -> position = new Vector2D(0, 6);
                case 2 -> position = new Vector2D(6, 0);
                case 3 -> position = new Vector2D(6, 6);
                default -> throw new IllegalArgumentException("The game is not designed for that many players max : "+PLAYER_NUMBER);
            }

            for(int j = 0; j < Goal.values().length/PLAYER_NUMBER; j++) {
                _goals.push(goals.getFirst());
                goals.removeFirst();
            }

            Player player = new Player(_goals, NAME[i]);
            addPlayer(player, position);
        }
        // To start with the first player and not the last that be created
        nextPlayer();
    }

    /**
     * This function add the new player in a table and in dictionary
     * @param player the new player
     * @param position the position of the new player
     */
    private void addPlayer(Player player, Vector2D position) {
        if (_currentPlayer == PLAYER_NUMBER)
            throw new IllegalArgumentException("There are too many players");
        _playersPositions.put(player, position);
        _players[_currentPlayer] = player;
        _currentPlayer++;
    }

    public Player getCurrentPlayer()
    {
        return _players[_currentPlayer];
    }

    public void nextPlayer()
    {
        _currentPlayer++;
        if (_currentPlayer >= 4)
            _currentPlayer = 0;
    }

    public Vector2D getPositionCurrentPlayer()
    {
        return (_playersPositions.get(getCurrentPlayer()));
    }

    /**
     *
     * @param direction where the player have to move
     */
    public void moveCurrentPlayer(Direction direction) {
        Player player = getCurrentPlayer();
        Vector2D vector = _playersPositions.get(player);
        switch (direction)
        {
            case EAST -> vector.moveRight();
            case WEST -> vector.moveLeft();
            case NORTH -> vector.moveTop();
            case SOUTH -> vector.moveBottom();
            }
    }

    public HashMap<Player, Vector2D> getPlayer()
    {
        return _playersPositions;
    }

    /**
     *
     * @return the Goal of the current player
     */
    public Goal getCurrentGoalCurrentPlayer()
    {
        return getCurrentPlayer().getCurrentGoal();
    }

    /**
     * The current player can pass to its next goal
     */
    public void nextGoalCurrentPlayer()
    {
        getCurrentPlayer().nextGoal();
    }

    /**
     *
     * @return if the current player still have goal
     */
    public boolean ifCurrentPlayerRestGoal()
    {
        return !getCurrentPlayer().isRestGoal();
    }
    /**
     *
     * @return if the player is in its starting Tile
     */
    public boolean ifCurrentPlayerHasComeBack()
    {
        return switch (getCurrentPlayer().getName())
                {
                    case "jaune"-> _playersPositions.get(getCurrentPlayer()).getX() == 0 && _playersPositions.get(getCurrentPlayer()).getY() == 0;
                    case "bleu"-> _playersPositions.get(getCurrentPlayer()).getX() == 0 && _playersPositions.get(getCurrentPlayer()).getY() == 6;
                    case "vert"-> _playersPositions.get(getCurrentPlayer()).getX() == 6 && _playersPositions.get(getCurrentPlayer()).getY() == 0;
                    case "rouge"-> _playersPositions.get(getCurrentPlayer()).getX() == 6 && _playersPositions.get(getCurrentPlayer()).getY() == 6;
                    default -> throw new IllegalArgumentException("It is not a ");
                };
    }
}
