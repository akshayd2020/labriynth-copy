To: Professor Felleisen

From: Jamie Lin, Jessica Su

Date: 10/7/2022

Subject: Design Task for Player (Milestone 3)

# Data Representation
A player must know:
1. An integer id that represents the player's id.
2. A string name that represents the player's name.
3. An integer age that represents the player's age.
4. A referee that is the referee for the current game the player is playing.

# Wish List
## Color getAvatar();
### Purpose
Get the avatar of this player
### Description
- Consumes: No parameters
- Computes: Nothing
- Knowledge it retrieves :This player's avatar

## P getHomePosition();
### Purpose
Get the home position of this player
### Description
- Consumes: No parameters
- Computes: Nothing
- Knowledge it retrieves: This player's home position

## P getGoalPosition();
### Purpose
Get the goal position of this player
### Description
- Consumes: No parameters
- Computes: Nothing
- Knowledge it retrieves: This player's goal position

## P getCurrentPosition();
### Purpose
Get the current position of this player
### Description
- Consumes: No parameters 
- Computes: Nothing
- Knowledge it retrieves: This player's current position

## boolean getGoalReached();
### Purpose
Get the status of whether this player's goal was reached
### Description
- Consumes: No parameters
- Computes: Nothing
- Knowledge it retrieves: Status of whether this player's goal was reached

## T[][] getBoard();
### Purpose
Get the board of the ongoing game
### Description
- Consumes: No parameters
- Computes: Nothing
- Knowledge it retrieves: Board of the ongoing game

## T getSpare();
### Purpose
Get the spare tile of the ongoing game
### Description
- Consumes: No parameters
- Computes: Nothing
- Knowledge it retrieves: Spare tile of ongoing game

## void playTurn(IPlayerAction pa);
### Purpose
Request a turn to play to the referee which either is one of:
- pass (skips this player)
- move (rotate tile, insert and slide, and move avatar)
Throws an error if the given action is not valid.

### Description
- Consumes: Action that a player wants to perform
- Computes: Tells the referee to update the state of the game
- Knowledge it retrieves: Nothing

## Note:
IPlayerAction is an interface that describes an action a player can perform (e.g. pass or make a move). 
An IAction has a function that performs the action by delegating to the referee.