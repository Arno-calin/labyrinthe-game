package app.view;

import app.controller.GameController;
import app.helpers.ImageHelper;
import app.model.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LabyrinthDisplay extends JFrame implements BoardObserver {

    // Panels for organizing the layout
    private final JPanel _pnlContentPane = new JPanel();
    private final JPanel _pnlTop = new JPanel();
    private final JPanel _pnlMiddle = new JPanel();
    private final JPanel _pnlBottom = new JPanel();
    private final JPanel _pnlPlayerInfo = new JPanel();
    private final JPanel _pnlRotateTile = new JPanel();
    private final JPanel _movementPanel = new JPanel(new GridBagLayout());
    
    // Window dimensions
    private final int WIDTH = 800;
    private final int HEIGHT = 900;

    // Current tile and controller
    private JPanel _currentTilePanel;
    private Tile _currentTile;
    private final GameController _controller;
    
    // Player labels
    private final JLabel[] _playerLabels = new JLabel[4];
    private final JLabel _lblCurrentPlayer = new JLabel("Current player: ");
    private final JLabel _lblCururentGoal = new JLabel("Current goal:");
    private final JLabel[] _lblPlayer = new JLabel[4];

    /**
     * Constructs a LabyrinthDisplay with the specified GameController.
     * Initializes the GUI components and sets up the layout.
     *
     * @param controller the GameController to manage game logic
     */
    public LabyrinthDisplay(GameController controller) {
        super("Labyrinth");
        _controller = controller;
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension((int)(WIDTH * 0.7), (int)(WIDTH * 1)));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Show the startup dialog after the main window is visible
        SwingUtilities.invokeLater(this::showStartupDialog);

        // Initialize panels
        initializeTopPanel();
        initializeMiddlePanel();
        initializeBottomPanel();
        layoutMainPanel();

        setContentPane(_pnlContentPane);
        setVisible(true);
    }

    /**
     * Initializes the top panel of the display, including the current tile display
     * and rotation buttons.
     */
    private void initializeTopPanel() {
        _pnlTop.setLayout(new BorderLayout());
        
        // Current tile display
        _currentTilePanel = new JPanel();
        _currentTilePanel.setPreferredSize(new Dimension(80, 80));

        // Rotation buttons
        JButton btnRotateTileLeft = new JButton("⤾");
        JButton btnRotateTileRight = new JButton("⤿");

        btnRotateTileLeft.addActionListener(e -> _controller.rotateLeft());
        btnRotateTileRight.addActionListener(e -> _controller.rotateRight());

        // Layout components
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        _pnlTop.setMinimumSize(new Dimension(70,70));
        _pnlRotateTile.setMinimumSize(new Dimension(70,70));
        _pnlRotateTile.setLayout(new GridBagLayout());
        constraints.gridx = 2;
        _pnlRotateTile.add(_currentTilePanel, constraints);

        constraints.gridx = 1;
        _pnlRotateTile.add(btnRotateTileLeft, constraints);
        
        constraints.gridx = 3;
        _pnlRotateTile.add(btnRotateTileRight, constraints);
        
        _pnlTop.add(_pnlPlayerInfo, BorderLayout.WEST);
        _pnlTop.add(_pnlRotateTile, BorderLayout.CENTER);
        JPanel _pnlCurrentPlayer = new JPanel();
        _pnlCurrentPlayer.setLayout(new GridLayout(2, 1));
        _pnlCurrentPlayer.add(_lblCurrentPlayer);
        _pnlCurrentPlayer.add(_lblCururentGoal);
        _pnlTop.add(_pnlCurrentPlayer, BorderLayout.EAST);
    }

    /**
     * Initializes the middle panel of the display, setting up the grid for the game board.
     */
    private void initializeMiddlePanel() {
        _pnlMiddle.setLayout(new GridBagLayout());
        _pnlMiddle.setPreferredSize(new Dimension(700, 700));
        _pnlMiddle.setMinimumSize(new Dimension((int)(WIDTH * 0.7), (int)(WIDTH * 0.7)));

        int tileSize = (int)(_pnlMiddle.getWidth() / 9);

        // Create a grid of panels for the game board
        for (int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                JPanel pnl = new JPanel();
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = i;
                gbc.gridy = j;
                pnl.setPreferredSize(new Dimension(tileSize, tileSize));
                _pnlMiddle.add(pnl, gbc);
            }
        }
    }

    /**
     * Initializes the bottom panel of the display, including player information
     * and the end turn button.
     */
    private void initializeBottomPanel() {
        _pnlBottom.setLayout(new BorderLayout());
        
        // Player information panel
        _pnlPlayerInfo.setLayout(new GridLayout(4, 1));
        for (int i = 0; i < 4; i++) {
            _playerLabels[i] = new JLabel("Player " + (i + 1));
            _pnlPlayerInfo.add(_playerLabels[i]);
        }

        // End turn button
        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.addActionListener(e -> _controller.endTurn());

        _pnlBottom.add(_movementPanel, BorderLayout.CENTER);
        _pnlBottom.add(endTurnButton, BorderLayout.SOUTH);
    }

    /**
     * Lays out the main panel by organizing the top, middle, and bottom panels.
     */
    private void layoutMainPanel() {
        _pnlContentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridy = 0;
        _pnlContentPane.add(_pnlTop, gbc);
        
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        _pnlContentPane.add(_pnlMiddle, gbc);
        
        gbc.gridy = 2;
        gbc.weighty = 0.0;
        _pnlContentPane.add(_pnlBottom, gbc);
    }

    /**
     * Draws a player image on top of a tile image.
     *
     * @param image the base tile image
     * @param player the player whose image is to be drawn
     * @return the combined image with the player overlay
     */
    public BufferedImage drawPlayerOnImage(BufferedImage image, Player player) {
        // Load player image and overlay it on the tile image
        String imagePath = "./assets/images/pion-" + player.getName() + ".png";
        BufferedImage playerImage = null;

        try {
            playerImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error: Unable to load player image from resource.");
            e.printStackTrace();
        }
        try {
            image = ImageHelper.merge(image, playerImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    /**
     * Retrieves the image for a given tile, applying necessary rotations based on its direction.
     *
     * @param tile the tile for which to retrieve the image
     * @return the BufferedImage of the tile
     */
    public BufferedImage getTileImage(Tile tile) {
        // Load and rotate tile image based on its direction
        String path = tile.getPathImg();
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Error: Unable to load image from resource.");
            e.printStackTrace();
        }

        ArrayList<Direction> direction = tile.getDirection();

        if(tile.getPathImg().contains("T")) {
            if(!direction.contains(Direction.SOUTH)) {
                image = ImageHelper.rotate(image, Math.PI);
            }
            else if(!direction.contains(Direction.EAST)) {
                image = ImageHelper.rotateClockwise(image);
            }
            else if(!direction.contains(Direction.WEST)) {
                image = ImageHelper.rotateCounterClockwise(image);
            }
        }
        else if(path.contains("I")) {
            if(direction.contains(Direction.EAST)) {
                image = ImageHelper.rotateClockwise(image);
            }
        } 
        else {
            if(direction.contains(Direction.WEST)) {
                if(direction.contains(Direction.SOUTH)) {
                    image = ImageHelper.rotateClockwise(image);
                } 
                else {
                    image = ImageHelper.rotate(image, Math.PI);
                }
            }
            else if(direction.contains(Direction.EAST) && direction.contains(Direction.NORTH)) {
                image = ImageHelper.rotateCounterClockwise(image);
            }
        }
        if (image == null) {
            System.err.println("Error: Unable to load image at path: " + path);
        }

        return image;
    }

    /**
     * Updates the board display with new tile and player positions.
     *
     * @param tiles the 2D array of tiles representing the board
     * @param players the map of players and their positions
     */
    @Override
    public void updateBoard(Tile[][] tiles, HashMap<Player, Vector2D> players) {
        // Update the board display with new tile and player positions
        GridBagConstraints constraints = new GridBagConstraints();
        _pnlMiddle.removeAll(); // Clear the panel before adding new components
        
        int tileSize = (int)(_pnlMiddle.getWidth() / 9); // Fixed size for tiles
        
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                constraints.gridx = j;
                constraints.gridy = i;
                
                if((i < 1 || i > 7) || (j < 1 || j > 7)) {
                    if(_controller.isMovable(i+1) && _controller.isMovable(j+1) && !(i == 0 && j == 0) && !(i == 0 && j == 8) &&
                       !(i == 8 && j == 0) && !(i == 8 && j == 8)) {
                        String buttonText;
                        Direction dir;
                        int numRowCol;
                        if(j == 0) {
                            buttonText = "→";
                            dir = Direction.NORTH;
                            numRowCol = i-1;
                        }
                        else if(j == 8) {
                            buttonText = "←";
                            dir = Direction.SOUTH;
                            numRowCol = i-1;
                        }
                        else if(i == 0) {
                            buttonText = "↓";
                            dir = Direction.WEST;
                            numRowCol = j-1;
                        }
                        else {
                            buttonText = "↑";
                            dir = Direction.EAST;
                            numRowCol = j-1;
                        }
    
                        JButton button = new JButton(buttonText);
                        button.setPreferredSize(new Dimension(tileSize, tileSize));
                        button.addActionListener(e -> _controller.pushCardsOnBoard(dir, numRowCol));
                        _pnlMiddle.add(button, constraints);
                    }
                } else {
                    Tile currentTile  = tiles[i - 1][j - 1];
                    BufferedImage image = getTileImage(currentTile);
                    for (Map.Entry<Player, Vector2D> entry : players.entrySet()) {
                        if(entry.getValue().getX() == i - 1 && entry.getValue().getY() == j - 1) {
                            image = drawPlayerOnImage(image, entry.getKey());
                        }
                    }
                    BufferedImage finalImage = image;

                    JPanel panel = new JPanel() {  
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g); 
                            if (finalImage != null) {
                                Graphics2D g2d = (Graphics2D) g;
                                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);
                                g2d.drawImage(finalImage, 0, 0, getWidth(), getHeight(), this);
                            }
                        }
                    };

                    if(currentTile.getGoal() != null) {
                        panel.setLayout(new BorderLayout());
                        JLabel lbl = new JLabel(currentTile.getGoal().toString());
                        lbl.setHorizontalAlignment(SwingConstants.CENTER);
                        lbl.setForeground(Color.BLACK);
                        panel.add(lbl);
                    }

                    panel.setName("tilePanel");
                    panel.setPreferredSize(new Dimension(tileSize, tileSize));
                    panel.setBackground(Color.WHITE); // Add background color
                    _pnlMiddle.add(panel, constraints);
                }
            }
        }
        _pnlMiddle.revalidate();
        _pnlMiddle.repaint();
    }

    /**
     * Updates the player information display with current goals and progress.
     *
     * @param players the map of players and their positions
     */
    @Override
    public void updatePlayer(HashMap<Player, Vector2D> players) {
        // Update player information display
        int i = 0;
        for (Map.Entry<Player, Vector2D> entry : players.entrySet()) {
            Player player = entry.getKey();
            Vector2D pos = entry.getValue();
            _playerLabels[i].setText(String.format("Player %s - Goals: %d/%d",
                    player.getName(),
                    player.getGoalSuccessful(),
                    player.getGoalSuccessful() + player.getGoalMaxNumber()));
            i++;
        }
    }

    /**
     * Updates the current tile display with the specified tile.
     *
     * @param tile the tile to display as the current tile
     */
    @Override
    public void updateTile(Tile tile) {
        // Update the current tile display
        _currentTile = tile;
        _currentTilePanel.removeAll();
        _currentTilePanel.setMinimumSize(new Dimension(_pnlRotateTile.getHeight(), _pnlRotateTile.getHeight()));
        int tileSize = (int)(_currentTilePanel.getWidth());
        
        JPanel tileDisplay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (_currentTile != null) {
                    BufferedImage image = getTileImage(_currentTile);
                    if (image != null) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                                           RenderingHints.VALUE_RENDER_QUALITY);
                        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            }
        };
        if(_currentTile.getGoal() != null) {
            tileDisplay.setLayout(new BorderLayout());
            JLabel lbl = new JLabel(_currentTile.getGoal().toString());
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setForeground(Color.BLACK);
            tileDisplay.add(lbl);
        }
        tileDisplay.setPreferredSize(new Dimension(70,70)); // Match the game board tile size
        tileDisplay.setBackground(Color.WHITE);
        
        _currentTilePanel.add(tileDisplay);
        _currentTilePanel.revalidate();
        _currentTilePanel.repaint();
    }

    /**
     * Updates the display to show the current player and their current goal.
     *
     * @param player the current player
     */
    @Override
    public void updateCurrentPlayer(Player player) {
        // Update the current player display
        setTitle("Labyrinth - Player " + player.getName());
        _lblCurrentPlayer.setText("Current player: " + player.getName());
        _lblCururentGoal.setText("Current goal: " + player.getCurrentGoal().toString());

        switch(player.getName()) {
            case "jaune" -> _lblCurrentPlayer.setForeground(Color.YELLOW);
            case "bleu" -> _lblCurrentPlayer.setForeground(Color.BLUE);
            case "vert" -> _lblCurrentPlayer.setForeground(Color.GREEN);
            case "rouge" -> _lblCurrentPlayer.setForeground(Color.RED);
        }
    }

    /**
     * Updates the movement panel with buttons for possible movement directions.
     *
     * @param possibleDirections the list of possible directions the player can move
     */
    @Override
    public void updatePossibleDirections(ArrayList<Direction> possibleDirections) {
        // Update the movement panel with possible directions
        _movementPanel.removeAll();
        String[] directions = {"↑", "→", "↓", "←"};
        Direction[] moveDirections = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        
        GridBagConstraints gbc = new GridBagConstraints();
        for (int i = 0; i < directions.length; i++) {
            JButton btn = new JButton(directions[i]);
            final Direction dir = moveDirections[i];

            gbc.gridx = i == 1 ? 2 : (i == 3 ? 0 : 1);
            gbc.gridy = i == 0 ? 0 : (i == 2 ? 2 : 1);
            if(possibleDirections.contains(dir)) {
                btn.addActionListener(e -> _controller.movePlayer(dir));
                btn.setForeground(Color.BLACK);
            } else {
                btn.setForeground(Color.RED);
            }
            _movementPanel.add(btn, gbc);
        }
        _movementPanel.repaint();
        _movementPanel.revalidate();
    }

    /**
     * Displays a startup dialog with game instructions.
     */
    private void showStartupDialog() {
        // Display a startup dialog with game instructions
        JDialog dialog = new JDialog(this, "Welcome to Labyrinth", true); // true makes it modal
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Welcome to Labyrinth!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(welcomeLabel, gbc);

        JTextArea instructions = new JTextArea(
            "Game Instructions:\n\n" +
            "1. Use arrow buttons to move your player\n" +
            "2. Rotate and insert tiles to create paths\n" +
            "3. Collect all your goals to win\n" +
            "4. End your turn when you're done moving"
        );
        instructions.setEditable(false);
        instructions.setBackground(panel.getBackground());
        instructions.setWrapStyleWord(true);
        instructions.setLineWrap(true);
        instructions.setMargin(new Insets(10, 10, 10, 10));
        panel.add(instructions, gbc);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            dialog.dispose();
        });
        panel.add(startButton, gbc);

        dialog.add(panel);
        dialog.pack();
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * Updates the game status display. Placeholder for future implementation.
     *
     * @param status the current game state
     */
    @Override
    public void updateGameStatus(GameState status) {

    }

    /**
     * Displays a dialog when the game ends, announcing the winner.
     *
     * @param status the current game state
     * @param winner the player who won the game
     */
    @Override
    public void updateGameStatus(GameState status, Player winner) {
        // Display a dialog when the game ends
        if (status == GameState.ENDED) {
            JDialog dialog = new JDialog(this, "Game Over!", true);
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel winnerLabel = new JLabel("Player " + winner.getName() + " wins!", SwingConstants.CENTER);
            winnerLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(winnerLabel, gbc);

            JLabel congratsLabel = new JLabel("Congratulations!", SwingConstants.CENTER);
            congratsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(congratsLabel, gbc);

            JButton exitButton = new JButton("Exit Game");
            exitButton.addActionListener(e -> {
                dialog.dispose();
                System.exit(0);
            });
            panel.add(exitButton, gbc);

            dialog.add(panel);
            dialog.pack();
            dialog.setSize(300, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialog.setVisible(true);
        }
    }
}



