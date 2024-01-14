Pair: cheerful-rabbits \
Commit: [5031364adb20b7e1c34d65d8a620bc268b20b69e](https://github.khoury.northeastern.edu/CS4500-F22/cheerful-rabbits/tree/5031364adb20b7e1c34d65d8a620bc268b20b69e) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/cheerful-rabbits/blob/e80141ae3b0d876643956da560fc106f56fea75b/9/self-9.md \
Score: 70/100 \
Grader: James Packard

- [20/20] accurate and helpful self evaluation

- [10/10] scoring function
  > Great purpose statement!
- [10/10] find the player(s) that has(have) visited the highest number of goals: `getPlayersWithHighestGoalsVisited` + purpose statement
- [10/10] compute their distances to the home tile: `getPlayersWithMinDistanceToNextGoal` + purpose statement
- [0/10] pick those with the shortest distance as winners
  > This functionality was implemented in the previous method, instead of being given its own method.

- [20/40] Unit tests for the four methods mentioned above.
  > There is only a unit test for the top-level score function, but it does not test the case when the state has no players. 50%.
