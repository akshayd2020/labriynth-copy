# Player-Referee Protocol

The player-referee protocol facilitates communication between a player and a referee. The referee will call the protocol's start up function that returns the player order. The protocol will prompt players to take turns with the information of the current game state that they are allowed to see. 
The protocol will have a play turn function that executes a player move. Lastly, the protocol will expose a function that returns whether a game is over and a function that 
gets the winner of the game.

# PlayerState startUp()

Return the game state that represents the start of the game.


# GameState takeTurn(GameState gs)

The protocol will prompt a player to take their turn with the information about the current game state without other player's private information. The protocol will execute the player's move and return the updated game state.

# Optional\<Color\> getWinner();

Return the winner of the game if it exists.

# boolean isGameOver();

Return whether the game is over.

