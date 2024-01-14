**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 9

Indicate below each bullet which file/unit takes care of each task.

Getting the new scoring function right is a nicely isolated design
task, ideally suited for an inspection that tells us whether you
(re)learned the basic lessons from Fundamentals I, II, and III. 

This piece of functionality must perform the following four tasks:

- find the player(s) that has(have) visited the highest number of goals (key auxiliary 1)
https://github.khoury.northeastern.edu/CS4500-F22/cheerful-rabbits/blob/90ebb44028404c0d75d422f2cab796afff475806/Maze/src/main/java/Referee.java#L254-L263
- compute their distances to the home tile (key auxiliary 2)
https://github.khoury.northeastern.edu/CS4500-F22/cheerful-rabbits/blob/90ebb44028404c0d75d422f2cab796afff475806/Maze/src/main/java/Referee.java#L265-L275
- pick those with the shortest distance as winners (key auxiliary 3)
https://github.khoury.northeastern.edu/CS4500-F22/cheerful-rabbits/blob/90ebb44028404c0d75d422f2cab796afff475806/Maze/src/main/java/Referee.java#L265-L275
- subtract the winners from the still-active players to determine the losers
https://github.khoury.northeastern.edu/CS4500-F22/cheerful-rabbits/blob/90ebb44028404c0d75d422f2cab796afff475806/Maze/src/main/java/Referee.java#L302-L321

The scoring function per se should compose these functions,
with the exception of the last, which can be accomplised with built-ins. 

1. Point to the scoring method plus the three key auxiliaries in your code.
  - Main scoring method:
https://github.khoury.northeastern.edu/CS4500-F22/cheerful-rabbits/blob/90ebb44028404c0d75d422f2cab796afff475806/Maze/src/main/java/Referee.java#L243-L252
  - Pointers to the three key auxiliaries are above.
2. Point to the unit tests of these four functions.
  - This unit test tests when all players are equidistant from their target goal and have the same number of goals visited:
    - This will cover the case in auxiliary 1 when multiple players have the same highest number of goals
    - This will cover the case in auxiliary 2 when multiple players are in different positions but have the same distance from their goal
    - This will cover the case in auxiliary 3 when multiple players have the same shortest distance and should all be considered winners
    - This will test that all 2 players are winners, and therefore no losers
https://github.khoury.northeastern.edu/CS4500-F22/cheerful-rabbits/blob/90ebb44028404c0d75d422f2cab796afff475806/Maze/src/test/java/RefereeTest.java#L163-L192

- This unit test tests when one player wins because they have visited more goals than any other player when the game ends:
    - This will cover the case in auxiliary 1 when a single player have the highest number of goals
    - This will cover the case in auxiliary 3 when a single player has the shortest distance and should be the only winner
    - This will test that only 1 player is a winner, and therefore the other players are losers
https://github.khoury.northeastern.edu/CS4500-F22/cheerful-rabbits/blob/90ebb44028404c0d75d422f2cab796afff475806/Maze/src/test/java/RefereeTest.java#L269-L281

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

