package app.model;

import java.util.Stack;

public class Player {

    private final Stack<Goal> _goalsStack;
    private int _goalSuccessful = 0;
    private int _goalMax;

    private final String _name;

    /**
     *
     * @param goals The player's goal stack
     * @param name the player's name
     */
    public Player(Stack<Goal> goals, String name)
    {
        _goalMax = goals.size();
        _goalsStack = goals;
        _name = name;

    }
    public int getGoalSuccessful()
    {
        return _goalSuccessful;
    }
    public int getGoalMaxNumber()
    {
        return _goalMax;
    }

    /**
     *
     * @return If the player still has objectives
     */
    public boolean isRestGoal()
    {
        return !_goalsStack.isEmpty();
    }

    /**
     * When a goal is successful the player moves on to the next objective
     */
    public void nextGoal()
    {
        if (isRestGoal()) {
            _goalsStack.pop();
            _goalSuccessful++;
            _goalMax--;
        }
    }

    public Goal getCurrentGoal()
    {
        return _goalsStack.peek();
    }

    public String getName()
    {
        return _name;
    }
    @Override
    public String toString()
    {
        return "Le joueur "+_name+" a "+_goalSuccessful+" succès réussi(s) sur "+ _goalMax +" succès";
    }
}
