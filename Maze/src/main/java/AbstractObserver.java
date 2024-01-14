import org.jgrapht.alg.util.Pair;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractObserver extends JFrame implements IObserver {

    protected List<GameState> gameStates;
    private int currentStateIndex;
    private boolean isGameOver;
    private JPanel grid = new JPanel();
    private static final int TILE_SIZE = 120;
    private static final int PLAYER_COMPONENT_SIZE = TILE_SIZE / 3;
    private static final int PLAYER_COMPONENT_OFFSET = TILE_SIZE * 2 / 3;
    private static final int PATH_TILE_RATIO = 4;
    private static final Font TEXT_FONT = new Font(Font.SERIF, Font.PLAIN, 13);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Rectangle TILE_BOUNDS = new Rectangle(0, 0, TILE_SIZE, TILE_SIZE);

    // width/thickness of a path
    private static final int PATH_WIDTH = TILE_SIZE / PATH_TILE_RATIO;
    private static Map<Connector, List<RectangleInfo>> connectorToRectangles = getConnectorToRectangles();

    /**
     * Constructs an observer with the given game state and renders the GUI
     * @param gameStates        the list of game states for this observer so far
     * @param currentStateIndex the index of the list of states we are currently on
     */
    public AbstractObserver(List<GameState> gameStates, int currentStateIndex) {
        super();
        this.isGameOver = false;
        this.gameStates = gameStates;
        this.currentStateIndex = currentStateIndex;
    }

    /**
     * Constructs an observer with the given game state and renders the GUI
     */
    public AbstractObserver() {
        super();
        this.isGameOver = false;
        this.gameStates = new ArrayList<>();
        this.currentStateIndex = 0;
    }

    public GameState getCurrentGameState() {
        GameState currentGameState = gameStates.get(currentStateIndex);
        return new GameState(currentGameState);
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Renders the GUI which includes
     * - the board
     * - the spare
     * - control buttons
     */
    private void renderGame() {
        JPanel gui = getBoardAndControlPanel();
        this.getContentPane().add(gui);
        pack();
        this.setVisible(true);
    }

    /**
     * Get the GUI which includes
     * - the board
     * - control panel which includes the spare and the control buttons
     * @return  the entire gui
     */
    private JPanel getBoardAndControlPanel() {
        JPanel gui = new JPanel();
        gui.setLayout(new BoxLayout(gui, BoxLayout.LINE_AXIS));
        GameState currentGameState = getCurrentGameState();
        gui.add(getBoardImage(currentGameState.getBoard(), currentGameState.getPlayerOrder()));
        gui.add(getControlPanel());
        return gui;
    }

    /**
     * Renders the given board
     * @param board  the board
     * @return       the rendering of the board
     */
    private JPanel getBoardImage(Board board, Queue<PlayerState> playerOrder) {
        // TODO update to also include home and goals
        Map<Position, List<Pair<Color, String>>> posToAvatarInfo = getCurrentAvatars(playerOrder);
        Map<Position, List<Pair<Color, String>>> posToHomeInfo = getHomeInfoMap(playerOrder);
        Map<Position, List<Pair<Color, String>>> posToGoalInfo = getGoalInfoMap(playerOrder);
        JPanel boardImage = new JPanel();
        boardImage.setLayout(new BoxLayout(boardImage, BoxLayout.PAGE_AXIS));
        Tile[][] grid = board.getGridDeepCopy();
        int numRows = board.getNumRows();
        int numCols = board.getNumColumns();
        for (int r = 0; r < numRows; r += 1) {
            JPanel rowImage = new JPanel();
            rowImage.setLayout(new BoxLayout(rowImage, BoxLayout.LINE_AXIS));
            for (int c = 0; c < numCols; c += 1) {
                Tile tile = grid[r][c];
                JPanel tileImage = getTileImage(tile);
                if (posToAvatarInfo.containsKey(new Position(r, c))) {
                    tileImage = addAvatar(posToAvatarInfo.get(new Position(r, c)), tileImage);
                }
                if (posToHomeInfo.containsKey(new Position(r, c))) {
                    tileImage = addHome(posToHomeInfo.get(new Position(r, c)), tileImage);
                }
                if (posToGoalInfo.containsKey(new Position(r, c))) {
                    tileImage = addGoal(posToGoalInfo.get(new Position(r, c)), tileImage);
                }
                rowImage.add(tileImage);
            }
            boardImage.add(rowImage);
        }
        return boardImage;
    }

    /**
     * Get the tile with the given avatar information added on top of it
     * @param avatarInfo    avatars to be added on top
     * @param tileImage     tile image to add on to
     * @return              updated image of a tile with avatars on it
     */
    private JPanel addAvatar(List<Pair<Color, String>> avatarInfo, JPanel tileImage) {
        Rectangle avatarBounds = new Rectangle(PLAYER_COMPONENT_OFFSET, 0, PLAYER_COMPONENT_SIZE, PLAYER_COMPONENT_SIZE);
        return combinePlayerInfoComponents(avatarInfo, tileImage, AvatarComponent::new, avatarBounds);
    }

    /**
     * Get the tile with the given home information added on top of it
     * @param homeInfo      homes to be added on top
     * @param tileImage     tile image to add on to
     * @return              updated image of a tile with homes on it
     */
    private JPanel addHome(List<Pair<Color, String>> homeInfo, JPanel tileImage) {
        Rectangle homeBounds = new Rectangle(0, PLAYER_COMPONENT_OFFSET, PLAYER_COMPONENT_SIZE, PLAYER_COMPONENT_SIZE);
        return combinePlayerInfoComponents(homeInfo, tileImage, HomeComponent::new, homeBounds);
    }

    /**
     * Get the tile with the given goal information added on top of it
     * @param goalInfo      goals to be added on top
     * @param tileImage     tile image to add on to
     * @return              updated image of a tile with goals on it
     */
    private JPanel addGoal(List<Pair<Color, String>> goalInfo, JPanel tileImage) {
        Rectangle goalBounds = new Rectangle(0, 0, PLAYER_COMPONENT_SIZE, PLAYER_COMPONENT_SIZE);
        return combinePlayerInfoComponents(goalInfo, tileImage, GoalComponent::new, goalBounds);
    }

    /**
     * Given a list of player information, converts that information into JComponents with the given
     * constructor and combines them with the tileImage at the specified x and y positions.
     * @param infoList      list of pairs of colors and strings for each player information component.
     * @param tileImage     the tile image to add the player information to.
     * @param constructor   a constructor that takes in a color and produces the JComponent to add.
     * @param bounds        the specified position and dimensions of the player information component.
     * @return              a JPanel containing the tile image and constructed components added.
     */
    private JPanel combinePlayerInfoComponents(List<Pair<Color, String>> infoList, JPanel tileImage,
                                               Function<Color, JComponent> constructor, Rectangle bounds) {
        tileImage.setBounds(TILE_BOUNDS);
        JPanel fullPanel = setUpFullPanel();

        int numComponents = infoList.size();
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        int width = (int) bounds.getWidth() / numComponents;
        int height = (int) bounds.getHeight() / numComponents;
        for (Pair<Color, String> infoPair : infoList) {
            bounds = new Rectangle(x, y, width, height);
            addInfoToPanel(numComponents, tileImage, constructor, bounds, fullPanel, infoPair);
            x += TILE_SIZE / 3 / numComponents;
            y += TILE_SIZE / 3 / numComponents;
        }
        return fullPanel;
    }

    private static JPanel setUpFullPanel() {
        JPanel fullPanel = new JPanel();
        fullPanel.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
        fullPanel.setLayout(null);
        return fullPanel;
    }

    /**
     * Adds the given player information and tileImage to the specified fullPanel.
     * @param tileImage     the tile image to add the player information to.
     * @param constructor   a constructor that takes in a color and produces the JComponent to add.
     * @param bounds        the specified position and dimensions of the player information component.
     * @param fullPanel     the panel that the tileImage and new components will be added to.
     * @param infoPair      the information of the component to add.
     */
    private void addInfoToPanel(int numComponents, JPanel tileImage, Function<Color, JComponent> constructor,
                                Rectangle bounds, JPanel fullPanel, Pair<Color, String> infoPair) {
        JComponent avatarImage = constructor.apply(infoPair.getFirst());
        avatarImage.setBounds(bounds);
        avatarImage.setLayout(new FlowLayout());
        JLabel name = getNameLabel(infoPair.getSecond());
        name.setBounds(bounds);
        fullPanel.add(name);
        fullPanel.add(avatarImage);
        fullPanel.add(tileImage);
    }

    private JLabel getNameLabel(String name) {
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(TEXT_FONT);
        nameLabel.setForeground(TEXT_COLOR);
        return nameLabel;
    }

    /**
     * Returns a map that keeps track of a given player Position field and the avatar and name of all players
     * whose respective Position field corresponds to that Position.
     * @param playerOrder   the given queue of players.
     * @param getPosition   a function that returns a Position field from the player.
     * @return              the map of player field Positions to all player avatars and names.
     */
    private Map<Position, List<Pair<Color, String>>> getMapOfPlayerInformation(Queue<PlayerState> playerOrder,
                                                                               Function<PlayerState, Position> getPosition) {
        List<PlayerState> playerStates = new ArrayList<>(playerOrder);
        Map<Position, List<Pair<Color, String>>> res = new HashMap<>();
        for (PlayerState ps : playerStates) {
            Position pos = getPosition.apply(ps);
            if (!res.containsKey(pos)) {
                List<Pair<Color, String>> avatarPairList = new ArrayList<>();
                avatarPairList.add(new Pair(ps.getAvatar(), ps.getPlayer().getName()));
                res.put(pos, avatarPairList);
            } else {
                res.get(pos).add(new Pair(ps.getAvatar(), ps.getPlayer().getName()));
                res.put(pos, res.get(pos));
            }
        }
        return res;
    }

    private Map<Position, List<Pair<Color, String>>> getCurrentAvatars(Queue<PlayerState> playerOrder) {
        return getMapOfPlayerInformation(playerOrder, PlayerState::getCurrentPosition);
    }

    private Map<Position, List<Pair<Color, String>>> getHomeInfoMap(Queue<PlayerState> playerOrder) {
        return getMapOfPlayerInformation(playerOrder, PlayerState::getHomePosition);
    }

    private Map<Position, List<Pair<Color, String>>> getGoalInfoMap(Queue<PlayerState> playerOrder) {
        return getMapOfPlayerInformation(playerOrder, PlayerState::getGoalPosition);
    }

    /**
     * Get the control panel which consists of
     * - the button to get the next state
     * - the button to save state
     * - the spare
     * @return  the control panel
     */
    private JPanel getControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        controlPanel.add(getNextStateButton());
        controlPanel.add(getSaveStateButton());
        GameState currentGameState = getCurrentGameState();
        JPanel spareTile = new JPanel(new FlowLayout());
        spareTile.add(getTileImage(currentGameState.getSpare()));
        controlPanel.add(spareTile);
        return controlPanel;
    }

    /**
     * Get the button with the functionality to get the next state
     * @return  the button with the functionality to get the next state
     */
    private JButton getNextStateButton() {
        JButton button = new JButton("Next State");
        button.setBounds(10, 10, 50, 40);
        button.addActionListener(new NextStateButtonListener());
        return button;
    }

    /**
     * Get the button with the functionality to save the current state in a file of the user's choice
     * @return  the button with the functionality to save the current state
     */
    private JButton getSaveStateButton() {
        JButton button = new JButton("Save State");
        button.setBounds(10, 10, 50, 40);
        button.addActionListener(new SaveButtonListener());
        return button;
    }

    /**
     * Renders the given tile
     * @param tile  the tile
     * @return      the tile
     */

    private JPanel getTileImage(Tile tile) {
        JPanel baseTile = getBaseTile(TILE_SIZE);
        baseTile.setBorder(new LineBorder(Color.BLACK, 1));
        List<JPanel> paths = getPaths(tile.getConnector());
        for (JPanel path : paths) {
            baseTile.add(path);
        }
        return baseTile;
    }

    /**
     * Get a square that acts as the base of a tile (excluding path and gems)
     * @param dimension     length of one side in pixels
     * @return              the square rendered
     */
    private JPanel getBaseTile(int dimension) {
        JPanel baseTile = new JPanel();
        baseTile.setBackground(Color.lightGray);
        baseTile.setPreferredSize(new Dimension(dimension, dimension));
        baseTile.setLayout(null);
        return baseTile;
    }

    /**
     * Get the paths corresponding to the given connector
     * @param c  the connector
     * @return   list of paths as rectangles
     */
    private List<JPanel> getPaths(Connector c) {
        List<JPanel> paths = new ArrayList<>();
        List<RectangleInfo> pathInfos = this.connectorToRectangles.get(c);
        for (RectangleInfo pathInfo: pathInfos) {
            JPanel path = getPathImage(pathInfo);
            paths.add(path);
        }
        return paths;
    }

    /**
     * Get a rectangle for the visual of the path
     * @param pathInfo  the information to make a path
     * @return          picture of a path
     */
    private static JPanel getPathImage(RectangleInfo pathInfo) {
        JPanel path = new JPanel();
        path.setBackground(Color.white);
        path.setBounds(pathInfo.getCoordinate().getLocation().x, pathInfo.getCoordinate().getLocation().y,
                pathInfo.getWidth(), pathInfo.getHeight());
        return path;
    }

    /**
     * Divide the number into 2
     * @param num   number
     * @return      half of the given number
     */
    private static int half(int num) {
        return num / 2;
    }

    /**
     * Informs this observer that there is a state change
     * @param newState     new state
     */
    public void informNewState(GameState newState) {
        this.gameStates.add(newState);
    }

    public void safeIncrementNextState() {
        if (currentStateIndex + 1 < gameStates.size()) {
            incrementNextState();
        }
    }

    /**
     * Increments the current state to the next state.
     */
    private void incrementNextState() {
        currentStateIndex++;
    }

    /**
     * Save the current state into a file specified by the filepath
     * @param filePath      file path to save into
     */
    public abstract void saveCurrentState(String filePath) throws IOException;

    /**
     * Alerts this observer that the current game is over. Renders the game with all of its states of
     * a single play.
     */
    public void gameOver() {
        this.isGameOver = true;
        this.renderGame();
    }

    private static Map<Connector, List<RectangleInfo>> getConnectorToRectangles() {
        int cornerLength = TILE_SIZE * 5 / 8;
        Map<Connector, List<RectangleInfo>> connectorToRectangles = new HashMap<>();
        connectorToRectangles.put(new Connector(Unicode.BAR, Orientation.ZERO),
                new ArrayList<>(Arrays.asList(new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                        PATH_WIDTH, TILE_SIZE))));
        connectorToRectangles.put(new Connector(Unicode.BAR, Orientation.NINETY),
                new ArrayList<>(Arrays.asList(new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                        TILE_SIZE, PATH_WIDTH))));
        connectorToRectangles.put(new Connector(Unicode.BAR, Orientation.ONE_EIGHTY),
                new ArrayList<>(Arrays.asList(new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                        PATH_WIDTH, TILE_SIZE))));
        connectorToRectangles.put(new Connector(Unicode.BAR, Orientation.TWO_SEVENTY),
                new ArrayList<>(Arrays.asList(new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                        TILE_SIZE, PATH_WIDTH))));

        connectorToRectangles.put(new Connector(Unicode.CORNER, Orientation.ZERO),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                                PATH_WIDTH, cornerLength),
                        new RectangleInfo(new Point(PATH_WIDTH * 3 / 2, PATH_WIDTH * 3 / 2),
                                cornerLength, PATH_WIDTH))));
        connectorToRectangles.put(new Connector(Unicode.CORNER, Orientation.NINETY),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                                PATH_WIDTH, cornerLength),
                        new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                                cornerLength, PATH_WIDTH))));
        connectorToRectangles.put(new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                                cornerLength, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), half(TILE_SIZE) - half(PATH_WIDTH)),
                                PATH_WIDTH, cornerLength))));
        connectorToRectangles.put(new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(PATH_WIDTH * 3 / 2, PATH_WIDTH * 3 / 2),
                                cornerLength, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), half(TILE_SIZE) - half(PATH_WIDTH)),
                                PATH_WIDTH, cornerLength))));

        connectorToRectangles.put(new Connector(Unicode.T_SHAPE, Orientation.ZERO),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                                TILE_SIZE, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), half(TILE_SIZE) - half(PATH_WIDTH)),
                                PATH_WIDTH, cornerLength))));
        connectorToRectangles.put(new Connector(Unicode.T_SHAPE, Orientation.NINETY),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), half(TILE_SIZE) - half(PATH_WIDTH)),
                                cornerLength, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                                PATH_WIDTH, TILE_SIZE))));
        connectorToRectangles.put(new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                                TILE_SIZE, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                                PATH_WIDTH, cornerLength))));
        connectorToRectangles.put(new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                                cornerLength, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                                PATH_WIDTH, TILE_SIZE))));

        connectorToRectangles.put(new Connector(Unicode.PLUS, Orientation.ZERO),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                                TILE_SIZE, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                                PATH_WIDTH, TILE_SIZE))));
        connectorToRectangles.put(new Connector(Unicode.PLUS, Orientation.NINETY),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                                TILE_SIZE, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                                PATH_WIDTH, TILE_SIZE))));
        connectorToRectangles.put(new Connector(Unicode.PLUS, Orientation.ONE_EIGHTY),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                                TILE_SIZE, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                                PATH_WIDTH, TILE_SIZE))));
        connectorToRectangles.put(new Connector(Unicode.PLUS, Orientation.TWO_SEVENTY),
                new ArrayList<>(Arrays.asList(
                        new RectangleInfo(new Point(0, half(TILE_SIZE) - half(PATH_WIDTH)),
                                TILE_SIZE, PATH_WIDTH),
                        new RectangleInfo(new Point(half(TILE_SIZE) - half(PATH_WIDTH), 0),
                                PATH_WIDTH, TILE_SIZE))));
        return connectorToRectangles;
    }

    /**
     * Performs the action to save the current state when there is an action
     */
    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            final JFileChooser fchooser = new JFileChooser();
            int returnValue = fchooser.showOpenDialog(AbstractObserver.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File f = fchooser.getSelectedFile();
                try {
                    saveCurrentState(f.getPath());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(AbstractObserver.this, e.getMessage());
                }
            }
        }
    }

    /**
     * Performs the action to get the next state when there is an action
     */
    private class NextStateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            safeIncrementNextState();
            redraw();
        }
    }

    /**
     * Redraws the GUI when called.
     */
    private void redraw() {
        this.getContentPane().removeAll();
        renderGame();
    }
}
