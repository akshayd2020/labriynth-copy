Pair: jamielin-jessicasu77 \
Commit: [b9d6ed58783b5ea780369604ed17b19f82190102](https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/tree/b9d6ed58783b5ea780369604ed17b19f82190102) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/jamielin-jessicasu77/blob/56243077dcd1417df80a8856491991d85d73158f/4/self-4.md \
Score: 100/110 \
Grader: Eshwari Bhide

10/10: Self-eval

`strategy.PP`: 90/100
- [10/20] a data definition for the return value of a call to strategy
  - -10 There needs to be explicit documentation of what an empty Optional means (that it represents a Pass). 
  - Your data definition interpretation could be stronger; interpretations for fields are kind of found in the purpose statements for their getters, but it would be better to have these also as part of data definition interpretation.
- [15/15] - good name, signature/types, and purpose statement for the top-level function that *composes* generating a sequence of spots to move to and searching.
- [15/15] - good name, signature/types, and purpose statement for generating the sequence of spots the player may wish to move to
- [15/15] - good name, signature/types, and purpose statement for searching rows then columns.
- [15/15] - good name, signature/types, and purpose statement for some function/method that validates the location of the avatar after slide/insert is not the target and the current target is reachable.
 
- [10/10] - for unit test that produces an action to move the player
- [10/10] - for unit test that forces player to pass on turn

`player-protocol.md` (Ungraded)
- Does the `startUp` method also handle assigning players their private goal tiles? 
- Minor, but the name `getWinner` makes it sound like the player is asking the referee about who the winner, while it's really the referee that is _informing_ the player. 
- No mention of a player passing on its turn
- What happens with cheating players?
