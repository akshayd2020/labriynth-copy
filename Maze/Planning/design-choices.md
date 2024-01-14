# Design

# Point
currently have: Point(row, col)
Java Point(x, y), stick with java

Actions
Game -> moves player
Player -> moves

# Tile
- gems
- unicode character

# IPlayer
- String name
- int age
- Point position
- Point homePosition
- Point goalPosition
- boolean hasGoal

# Board
- Tile[][] grid
- Tile spare

Functions
- sliding row or column: able to slide multiple tiles- leaving multiple nulls
- inserting tile into a spot
- determing which tiles are reachable

# GameState
- Tile spare
- Board board
- Map<IPlayer, PlayerState> playersState 
  - includes:
    - avatar
    - home tile
    - goal tile
    - position

- List<IPlayer> waitList 
- List<IPlayer> players (sorted list of players by age)
- Map<IPlayer, Integer> score
- IPlayer currentPlayer

Functions:

- Tile getSpare();
- void performAction(IAction action):
  - These will be instances of IAction
    - Tile rotateSpare(int degrees)
    - void validateAndSlide(boolean isRow, int row, int column, Direction)
    - void insertSpare(int row, int col)
    - void move(int row, int col)
- boolean canReach(Tile targetTile)
- boolean reachedGoal()
- boolean reachedHome()
- void kickOutActivePlayer()


- void establishPlayers():
  - establish player list, adds remainder to wait list
  - sorts player list
- void setUpBoard():
  - creates the Board instance 
  - assign home and goal positions to players
- IPlayer nextPlayer(): 
    - returns the player whose turn is next
- IAction nextAction():
    - returns next action the player should do
- void performAction(IAction action):
    - has a timeout 
    - throws an error if the given action is not valid
    - performs action if given action is valid
    - updatesScore(player)
- void updateScore(IPlayer player): updates the score of the specified player
- Map<IPlayer, Integer> getScore: return players scores
- void endGame(): announces the end of the game and final score

IAction:
- validate()

classes that implement IAction: Slide, Insert, Move
- constructors take in a readonly board


- deciding validity of actions
    - sliding row or column (validate whether direction corresponds with row/column)
      - can't slide a row 0, size of board
      - can't slide a row up or down
    - inserting a tile into a spot
- announcing end of the game and outcome of every participant
- calculateScore
- Map<IPlayer, Integer> getScore: return players scores
- void endGame(): announces the end of the game and final score

# Player state is the state that a player must know to act

Player must know to act:
- Board
- if i can reach a tile
- home position
- goal position
- current position
- get spare

Player Interface: 
- Position getPosition(IPlayer player)
- boolean isReachable(Position pos)
- 