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
                default -> throw new IllegalArgumentException("Le jeu n'est pas prévu pour autant de joueur max : "+PLAYER_NUMBER);
            }

            for(int j = 0; j < Goal.values().length/PLAYER_NUMBER; j++) {
                _goals.push(goals.get(0));
                goals.remove(0);
            }

            Player player = new Player(_goals, NAME[i]);
            addPlayer(player, position);
        }
        nextPlayer(); // pour retourner sur le premier joueur sinon c'est le joueur 4 qui joue, mais il existe pas
    }
    private void addPlayer(Player player, Vector2D position) {
        if (_currentPlayer == PLAYER_NUMBER)
            throw new IllegalArgumentException("Il y a trop de joueurs");
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

    public void movePlayer(Direction direction) {
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

    public Goal getCurrentGoalCurrentPlayer()
    {
        return getCurrentPlayer().getCurrentGoal();
    }
    public void nextGoalCurrentPlayer()
    {
        getCurrentPlayer().nextGoal();
    }
    public boolean ifCurrentPlayerRestGoal()
    {
        return !getCurrentPlayer().isRestGoal();
    }
    public boolean ifCurrentPlayerHasComeBack()
    {
        return switch (getCurrentPlayer().getName())
                {
                    case "jaune"-> _playersPositions.get(getCurrentPlayer()).getX() == 0 && _playersPositions.get(getCurrentPlayer()).getY() == 0;
                    case "bleu"-> _playersPositions.get(getCurrentPlayer()).getX() == 0 && _playersPositions.get(getCurrentPlayer()).getY() == 6;
                    case "vert"-> _playersPositions.get(getCurrentPlayer()).getX() == 6 && _playersPositions.get(getCurrentPlayer()).getY() == 0;
                    case "rouge"-> _playersPositions.get(getCurrentPlayer()).getX() == 6 && _playersPositions.get(getCurrentPlayer()).getY() == 6;
                    default -> throw new IllegalArgumentException("fuck");
                };
    }
}
