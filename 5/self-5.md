**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 5

Indicate below each bullet which file/unit takes care of each task:

The player should support five pieces of functionality: 

- `name`
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/main/java/Player.java#L53-L55
- `propose board` (okay to be `void`)
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/main/java/Player.java#L60-L68
- `setting up`
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/main/java/Player.java#L70-L77
- `take a turn`
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/main/java/Player.java#L79-L91
- `did I win`
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/main/java/Player.java#L93-L99

Provide links. 

The referee functionality should compose at least four functions:

- setting up the player with initial information
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/main/java/Referee.java#L54-L65
- running rounds until termination
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/main/java/Referee.java#L86-L118
- running a single round (part of the preceding bullet)
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/main/java/Referee.java#L97-L116
  - We keep track of a single round in the GameState but the `takingTurns()` function runs rounds
- scoring the game
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/main/java/Referee.java#L171-L185

Point to two unit tests for the testing referee:

1. a unit test for the referee function that returns a unique winner
   - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/test/java/RefereeTest.java#L107-L113
2. a unit test for the scoring function that returns several winners
   - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/7b8bfcda1851a0434fd83f5e184e0deb9ce2ca96/Maze/src/test/java/RefereeTest.java#L163-L188

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files -- in the last git-commit from Thursday evening. 

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

