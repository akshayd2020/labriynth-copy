**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 7

Indicate below each bullet which file/unit takes care of each task:

The require revision calls for

    - the relaxation of the constraints on the board size
    - a suitability check for the board size vs player number 

1. Which unit tests validate the implementation of the relaxation?
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/c67cd420182e4087e30813bac137c2f2ea3dbe5e/Maze/src/test/java/BoardTest.java#L689-L700
  - This test utilizes a board that has 1 row x 15 column dimensions.
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/c67cd420182e4087e30813bac137c2f2ea3dbe5e/Maze/src/test/java/BoardTest.java#L882-L889
  - This test utilizes a board that has 3 row x 4 column dimensions.
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/c67cd420182e4087e30813bac137c2f2ea3dbe5e/Maze/src/test/java/BoardTest.java#L727-L737
  - This test utilizes a board that has 4 row x 4 column dimensions.
    - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/c67cd420182e4087e30813bac137c2f2ea3dbe5e/Maze/src/test/java/BoardTest.java#L61-L109
    - The above unit tests utilize the constructors in the setup() method which indicate the relaxation of dimension constraints for the Board constructor.

2. Which unit tests validate the suitability of the board/player combination? 
  - We did not implement unit tests for this board and player combination suitability check although we did update our functionality in generating GameStates to reflect this constraint change.
   
The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

