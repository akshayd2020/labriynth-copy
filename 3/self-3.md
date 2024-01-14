## Self-Evaluation Form for Milestone 3

Indicate below each bullet which file/unit takes care of each task:

1. rotate the spare tile by some number of degrees
- https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/70af8c0ca0c25f6141765450e50dd737f01eb38b/Maze/src/main/java/RotateTile.java#L19-L24
- The GameState performAction(IAction action) function delegates to the RotateTile class.
2. shift a row/column and insert the spare tile
   - shiftAndInsert()
     - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/70af8c0ca0c25f6141765450e50dd737f01eb38b/Maze/src/main/java/SlideAndInsert.java#L25-L33
   - plus its unit tests
     - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/70af8c0ca0c25f6141765450e50dd737f01eb38b/Maze/src/test/java/GameStateTest.java#L94-L202
3. move the avatar of the currently active player to a designated spot
   - We did not realize this was part of this spec's functionality requirements.
4. check whether the active player's move has returned its avatar home
   - We did not realize this was part of the spec.
5. kick out the currently active player
   - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/70af8c0ca0c25f6141765450e50dd737f01eb38b/Maze/src/main/java/GameState.java#L153-L158
   - This function sets the active player to the given player. We chose to add the ability to 
     set the active player right away rather than replacing the current active player with a 
     temporary data structure.
     
The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

