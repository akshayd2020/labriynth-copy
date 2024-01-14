Pair: jamielin-jessicasu77 \
Commit: [606f5cb4e2cd6f8cc85198b6eb9ef77ae4761a90](https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/tree/606f5cb4e2cd6f8cc85198b6eb9ef77ae4761a90) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/0efe2c337c926d91896e98ea6acd0ea2ff780762/5/self-5.md \
Score: 117/160 \
Grader: Mike Delmonaco

## Programming (20 pts self-eval, 110 pts code)

-20 (all self-eval pts) I see that you filled out your self eval, but it was committed on October 29th, which is after the dealine for self-evals. Unfortunately, I cannot
give you credit for it.

Good job providing links to the correct commit with line ranges! This is very helpful.

### Player

WARNING: your setup method does not take in an optional state, which is specified in [Logical Interactions](https://course.ccs.neu.edu/cs4500f22/local_protocol.html).

Boolean-returning functions/methods have purpose statements written in the form of a question, not methods like `Player#win`, which _takes in_ a boolean and returns void.

Good job on the Player! Your code is very clear and well-documented. The only thing missing is a purpose statement for the class itself.

### Referee

-3 The purpose and name for `Referee#takingTurns` is unclear. Neither suggest that a whole game is run.

-10 No method for running a single round.

You should have a separate method for running a single round. It will likely make it easier to handle round-based conditions in the logic.

This first unit test seems to be wrong. You are not using your `expected` array in the assertion, and you are actually expecting a set of arrays,
when your method returns an array of sets.

-5 This is not a unit test for the scoring function that produces several winners. It seems like you are expecting zero winners and two cheaters,
since the value at index 1 of the result of `Referee#runGame` is the set of cheaters. Either way, this is not a test of the scoring function/method,
but of `Referee#runGame` as a whole. This is not what was asked for. I gave you half-credit because you meant to test multiple winners, which is close
to what was asked for.

In the future, if you did not implement a piece of functionality, say so. Or at least explicitly say that this is not a test of the scoring function,
but of the `Referee#runGame` method. I would've taken off points for misdirection here if your self-eval was graded. If you are honest about not implementing
a piece of functionality, you will get partial credit instead of being penalized.

## Design (30 pts)

-5 _How_ will the user select a row/column and move it? I gave you half credit for specifying that this was possible, but not how it is possible.

I'm interpreting your last sentence to mean that the user clicks on one of the displayed positions that their avatar can move to in order to perform the move.
However, you should be more explicit and clear when you are describing a GUI like this.

