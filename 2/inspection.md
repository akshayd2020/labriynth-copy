Pair: jamielin-jessicasu77 \
Commit: [4a618328ce8e9a6c97acced32932222a958797f9](https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/tree/4a618328ce8e9a6c97acced32932222a958797f9) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7fc4185d66b2096f70f06907402cde3862238234/2/self-2.md \
Score: 0/80 \
Grader: James Packard \

Programming task:

> I had to give the programming task a 0/80 because you used `null` to represent an empty space on the board without documenting it. You would get a 20/80 if you mentioned that the `ITile[][] grid` field could contain `null`.  
> The problem with using `null` is that it disguises the fact that tiles can either be present or removed, and prevents the type checker from preventing the kinds of mistakes that arise from this.  
> But also, does it make sense for a Board to have an empty space? Or would it be better to think of the insertion of a tile as 'pushing' the new spare tile out?  
> I marked the points for the rest of this assignment as if you did not make the `null` mistake, so that you can see what score you would have received.

[30/40] an operation that determines the reachable tiles from some spot
  1. [0/10] : data definition for coordinates
     > You use java.awt.Point as a coordinate, but you don't explain what it means in the context of the Board. It's okay to use Point but you should include a data definition that describes its meaning (which should mention things like (0,0) is top-left).
  2. [+10] : signature/purpose statement
     > Your `getReachableTiles` method takes a row and column index in addition to an `ITile`. Given an `ITile`, can you determine at which row and column it is located? What would happen if you called this method with a row and column that do not match the tile's location?
  3. [+10] : method is clear about how it checks for cycles 
  4. [+10] : at least one unit test with a non-empty expected result

---

[20/30] an operation for sliding a row or column in a direction 
  - [+10] signature/purpose statement
    > What happens to the spare tile? I can see that it's set to `null`, but this should
      behavior should be mentioned in the purpose statement.
  - [0/10] the method check does not check that row/column has even index 
    > If the board is the component that knows which rows/columns are movable, it should perform this check.
  - [+10] at least one unit test for rows and one for columns

---

[5/10] an operation for rotating and inserting the spare tile
  - [0/5] the purpose statement (of `insertTile` or `slide`) does not mention what happens to the tile that's pushed out
    > The purpose statement should also explain what "cannot be placed" means, as in what precisely is the condition that causes this method to throw an error?

---

> So, you would have received a 55/80.

Some questions/thoughts for you to ponder on your code:

> Why do you have a `getGrid()` method on your `IBoard`? Does this expose too much about your implementation?  
> Interfaces should be used to represent unions. If there's only one kind of board, why use an interface? Same with tiles.  
> Additionally, why is your `IBoard` interface parameterized over type `T`? Does is make sense for a labyrinth board to use any other kinds of tiles than your `ITile` (or just `Tile` for that matter)?  
> Some of your methods are really long. Can these be split into separate methods each with one task?  

---

Design task (ungraded):

> Is score a necessary piece of information to store or can it be computed from other things in the state? Additionally, how would the consumer of the game state know when to call `updateScore`, and how could its misuse lead to invalid states?   
> The game state will need to contain the homes, goals, and current positions of the players.  
> The game state will need to keep track of the last move so that the next player can't undo it.  
> Your wish list has many `void` methods, which implies that your game state is mutable. Think carefully about whether mutation is necessary and the downsides of such an approach compared to an immutable game state.  
> Why does your game state know about the next action a player _should_ do? That sounds more like a strategy, if by _should_ you mean the 'best' move.
> Your `endGame()` method (taking no arguments are returning nothing) implies that the game state has some knowledge about the communication layer. Should communication be a concern of the game state?  
