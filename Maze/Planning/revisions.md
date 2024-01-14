To: Professor Felleisen

From: Akshay Dupuguntla, Eric Yip

Date: 11/28/2022

Subject: Programming Task (Milestone 9)

# Changes To PlayerState

In PlayerState, we now allow for an optional intermediary goal to be passed in, which represents the player's next goal. If it is empty, the player should be going home. We also replaced the `goalReached` boolean with an integer storing the number of goals the player has reached. We refactored `getGoalPosition()` to return the intermediary goal if it exists, or the home position if it is empty. We also created a new method `giveNewIntermediaryGoal()` which will set the player's goal and increment the number of goals reached.

# Changes To GameState

In GameState, we added a new field `goalsToDistribute` which is a queue of Positions representing the ordered sequence of goals to be assigned to players. We added a method `getNextDistributedGoal()` that will remove the next goal from the queue if it exists, returning either that next goal or `Optional.empty` if there are no more goals to distribute. When checking if the active player has won, the GameState will now check that a player has reached their home and they have no more intermediary goals to go to. We also refactored the `generateGameState()` method to return a GameState with an empty initialized sequence of goals to assign to players as the game progresses.

# Changes To Referee

In Referee, we refactored the `moveAndCheckWin()` method to redirect the active player to the next goal in the ordered sequence of potential goals instead of immediately redirecting them home. We also modified the `getWinners()` method to return a set of IPlayers that have visited the most goal tiles and are closest to their next goal tile.
