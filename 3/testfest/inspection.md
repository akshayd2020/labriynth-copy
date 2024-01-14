Pair: jamielin-jessicasu77 \
Commit: [70af8c0ca0c25f6141765450e50dd737f01eb38b](https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/tree/70af8c0ca0c25f6141765450e50dd737f01eb38b) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7db211da4526b7deb0ed303175648cf97357250d/3/self-3.md \


Score: 53/85 \
Grader: Varsha Ramesh

#### PROGRAMMING

- [14/20] Helpful and accurate self-eval
  - Misdirection for kicking out the currently active player. `setActivePlayer()` is different from removing a player from the game.

- [26/45]  - Signatures and purpose statements for the following: 

  - [0/5] rotate the spare tile by some number of degrees
    > Represents validation and action of rotating a tile in a game of Labyrinth.
    -  RotateTile is not a clear name and the data interpretation for the class as well as the purpose statement for `perform` are unclear. Which tile is getting rotated? How much is the rotation and in what direction?
  - [5/5] shift a row/column and insert the spare tile
    
  - [3/5] move the avatar of the currently active player to a designated spot
    - Does not exist but explicitly said so.
  - [3/5] check whether the active player's move has returned its avatar home
     - Does not exist but explicitly said so.
  - [0/5] kick out the currently active player
     - Does not exist.

- Unit tests
  - [5/10] test row and column insertions
    - Tested 2 directions, did not test the other two directions.
  - [10/10] testing player movement when row/column shifts

#### DESIGN

- [13/20]
  - [3/5] player has a `take turn` function/method
    - PlayTurn is split into into move and shiftAndInsert.
  - [5/5] player's home location
  - [5/5] player's goal assignment
  - [5/5] player's current location
  - [-5] your memo should clarify when/how the player receives different pieces of data.
