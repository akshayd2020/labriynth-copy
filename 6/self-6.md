**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 6

Indicate below each bullet which file/unit takes care of each task:

The simple GUI must implement three gestures: 

1. rotate the tile
  - We did not realize this piece of functionality because we interpreted that there would be only
    2 buttons that show the next state and save the current state.
2. shift a row (left, right) or column (up, down)
  - We did not realize this piece of functionality because we interpreted that there would be only
    2 buttons that show the next state and save the current state.
3. move the avatar somewhere else
  - We did not realize this piece of functionality because we interpreted that there would be only
    2 buttons that show the next state and save the current state.

For each gesture, identify the view, the control, and the model in
your code. We understand that in some modern GUI frameworks (e.g.,
React) there is no separation of model, view, and control, meaning
some URLs will be identical.

The key is that a future reader of your code should be able to
understand how these three elements are realized -- from the purpose
statements of the relevant methods/functions.

Also explain here how your code ensures the sequential order of these
GUI gestures and point to the relevant pieces of code.
- The referee applies the move (in a single function) in the order of
  rotating the spare, shifting the row/column, inserting the spare, 
  and moving the player to their new position, ensuring sequential order
  of the GUI gestures.

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

