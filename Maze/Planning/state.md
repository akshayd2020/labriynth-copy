To: Professor Felleisen

From: Jamie Lin, Jessica Su

Date: 10/7/2022

Subject: Design Task (Milestone 2)

# Data Representation
1. An instance of the game board where valid user actions mutate the board.
2. A list of players will represent players in one game of Labyrinth.
3. A list of players will represent players are on the wait-list for a game of Labyrinth.
4. A Map of IPlayer to an Integer will represent each player and their score at any point of the 
   game.
5. The spare tile will represent the spare tile at any point of the game.
6. The current player whose turn it is.

## Wish List
void establishPlayers():

* Establish the list of players (sorted by age) for one game of labyrinth and add the 
remainder of 
the 
players to the wait list.
  
void setUpBoard():

* Create the board and assign home and goal positions to each player.

IPlayer nextPlayer():

* Return the player whose turn is next.

void performAction(IAction action):

* Perform action if the given action is valid 
  and update the current player's score. Throw an error if the given action is not valid.

IAction nextAction():

* Return the next action the player should do.

void updateScore(IPlayer player):

* Update the score of the specified player.

Map<IPlayer, Integer> getScore(IPlayer player):

* Get a mapping of each player to their current score.


void endGame():

* Announce the end of the game, and the final score to the communication layer.

## Note:

IAction is an interface that describes an action a user can perform (e.g. slide, insert, 
and move). An IAction has a function to validate whether a given action is valid in a game.