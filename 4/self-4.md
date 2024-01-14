**If you use GitHub permalinks, make sure your links points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 4

The milestone asks for a function that performs six identifiable
separate tasks. We are looking for four of them and will overlook that
some of you may have written deep loop nests (which are in all
likelihood difficult to understand for anyone who is to maintain this
code.)

Indicate below each bullet which file/unit takes care of each task:

1. the "top-level" function/method, which composes tasks 2 and 3:
   * https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/b9d6ed58783b5ea780369604ed17b19f82190102/Maze/src/main/java/AbstractStrategy.java#L23

2. a method that `generates` the sequence of spots the player may wish to move to:
   * Riemann strategy: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/b9d6ed58783b5ea780369604ed17b19f82190102/Maze/src/main/java/RiemannStrategy.java#L31-L40
   * Euclid strategy: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/b9d6ed58783b5ea780369604ed17b19f82190102/Maze/src/main/java/EuclidStrategy.java#L34-L59

3. a method that `searches` rows,  columns, etcetc. 
   * https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/b9d6ed58783b5ea780369604ed17b19f82190102/Maze/src/main/java/AbstractStrategy.java#L191-L206

4. a method that ensure that the location of the avatar _after_ the
   insertion and rotation is a good one and makes the target reachable
   * https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/b9d6ed58783b5ea780369604ed17b19f82190102/Maze/src/main/java/AbstractStrategy.java#L61-L82

ALSO point to

- the data def. for what the strategy returns
  - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/b9d6ed58783b5ea780369604ed17b19f82190102/Maze/src/main/java/PlayInfo.java#L1-L16

- unit tests for the strategy
  - Both Riemann and Euclid Tests: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/b9d6ed58783b5ea780369604ed17b19f82190102/Maze/src/test/java/StrategyTest.java#L89-L174

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality or realized
them differently, say so and explain yourself.


