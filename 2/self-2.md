## Self-Evaluation Form for Milestone 2

Indicate below each bullet which file/unit takes care of each task:

1. point to the functionality for determining reachable tiles 

   - a data representation for "reachable tiles"
     - Set\<Point>
     - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/main/java/Board.java#L135
   - its signature and purpose statement
     - interface: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/main/java/IBoard.java#L28-L35
     - class implementation: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/main/java/Board.java#L132-L135
   - its "cycle detection" coding (accumulator)
     - Set<Point> representing visited tiles
       - instantiating accumulator: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/main/Maze/src/main/java/Board.java#L142
       - Adding to accumulator: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/main/java/Board.java#L182
   - its unit test(s)
      - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/test/java/BoardTest.java#L276-L285

2. point to the functionality for shifting a row or column 

   - its signature and purpose statement
      - interface: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/main/java/IBoard.java#L10-L17
      - class implementation: https://github.khoury.northeastern.
        edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/main/java/Board.java#L26-L30
   - unit tests for rows and columns
      - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/test/java/BoardTest.java#L189-L259
3. point to the functionality for inserting a tile into the open space
   - its signature and purpose statement
      - interface: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/main/java/IBoard.java#L19-L26
      - class: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/main/java/Board.java#L123
   - unit tests for rows and columns
      - https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/4a618328ce8e9a6c97acced32932222a958797f9/Maze/src/test/java/BoardTest.java#L260-L274
      

If you combined pieces of functionality or separated them, explain.

If you think the name of a method/function is _totally obvious_,
there is no need for a purpose statement. 

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

