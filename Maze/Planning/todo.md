# TODO

(Note: the entries in the TODO list that are marked off are completed and also appear in the Completed section with their git commits and messages.)

## 5 - Highest Priority
- [X] [Bug] Referee Tests, bug in the referee
  - [X] Fix parallel data structures for the IPlayers, instead should have the GameState’s playerstates have IPlayer optional field
  - [X] Fix RefereeTests
- [X] [Bug] Strategy needs to know the last action of a GS - it should not pick a move that undoes the last action
- [X] [Functionality of the GUI] Buttons are not connected have functionality for saveCurrentState and getting the next state
- [X] [Functionality of Observer] should be rendering after the game is over
- [X] [Functionality] - IPlayer and Player needs to take in Optional GameState

## 4 - High Priority
- [X] [Confidence] Need to test MovePlayer
- [X] [Functionality of the Observer method] The RefereeState that is returned from saveCurrentState: last field returns an array of [String, String] rather than [Integer, String]
- [X] [Constraint] Board constraints
- [X] Ensuring all the home tiles are distinct
- [X] Referee does not handle infinite loops

## 3 - GUI Related and Low Priority Constraints
- [X] [GUI] Rendering of the home and the goal of the players
- [X] [Constraint] Make gems all unique on each tile 
- [X] [Constraint] Make board gems actually random
- [X] Move makefile to proper location

## 2 - Mostly Readability Issues
- [X] Fixing purpose statement of takingTurns function in Referee
- [X] Revisit Purpose statement for Player methods (add purpose statement to the class, and make sure purpose statements match the signatures for methods)
- [X] Add purpose statement for what Optional empty means (Pass)
- [X] [Performance]: isReachable calls getReachableTiles rather than just stopping when the target tile is found
- [X] [Readability]: the return of runGame should be a new data structure
- [X] [Readability] SlidingAction should be a new data structure
- [X] [Readability] movePlayersInGameState (abstract it)
- [X] [GUI] The avatars on the board, don’t have names attached to them
- [X] [GUI] Make the avatar + home + goal not be behind the path
- [X] [GUI] Spare tile is formatted in a way so that the spare is just really long, it doesn’t look like one tile
- [X] [GUI] Borders around the Tiles
- [X] [GUI] Fix overlapping goals
- [X] [Confidence] Updating the previous test harnesses
- [X] [Flexibility] Referee should be able to hand off to multiple observers
- [X] [Flexibility] Add a rule book to be passed into actions
- [X] [Removing duplicate code] Make misbehaving player classes extend current Player class
- [ ] [GUI] Render the gems on the board

## 1 - Nice to Have
- [ ] [Ease of development] Auto Generated inputs for testing harnesses
- [ ] [Readability] Abstract slideAndInsert functions
- [ ] [Readability] abstract generateDistinctHomes/generatedDistinctAvatars
- [ ] Figure out HashSet Equality

# Completed
- [X] [Bug] Referee Tests, bug in the referee
  - [X] Fix parallel data structures for the IPlayers, instead should have the GameState’s playerstates have IPlayer optional field
    - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/3488eaa7a78146d97a1add013a5a5c590e4b43d4
    - complete fix parallel data structures from TODO 
  - [X] Fix RefereeTests
    - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/1f46514315a3d50625a95b17800536d30ac8fe23
    - Completed fix RefereeTests in TODO
- [X] [Bug] Strategy needs to know the last action of a GS - it should not pick a move that undoes the last action
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/a776dd4c782ec761447cc951b33a845ef586da81
  - Fixed Strategy needing to know last action in TODO
- [X] [Functionality of the GUI] Buttons are not connected have functionality for saveCurrentState and getting the next state
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/ec7228fe055967c2902185f8b7bfcd7a927f361e
  - Updated unfinished GUI refactor to fix buttons and next state funtionality
- [X] [Functionality of Observer] should be rendering after the game is over
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/abe196c5b8e876c395f3873ab77c919f1ef7859a
  - Fixed bug where observer should be rendering after the game is over in TODO
- [X] [Functionality] - IPlayer and Player needs to take in Optional GameState
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/0e5b8057475ed00baff0405f653219bac4940e69
  - Fix IPlayer and Player needs to take in Optional GameState in TODO 
- [X] [Confidence] Need to test MovePlayer
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/06509aae49fdb32df7a73492a740644f57a8ac22
  - Fix Need to test MovePlayer in TODO 
- [X] [Functionality of the Observer method] The RefereeState that is returned from saveCurrentState: last field returns an array of [String, String] rather than [Integer, String]
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/360dfd3a0cb24fcf46c53b76fbf187f76a48bc32
  - Fix saveCurrentState JSON representation of last action from [String, String] to [int, String] in TODO
- [X] [Constraint] Board constraints
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/8f8b8145a8a44e4f96ee7e0e3ec08a077faeed81
  - Fix board constraints (by adding purpose statement for clarity) in TODO
- [X] Ensuring all the home tiles are distinct
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/15ef35f9e552e60c76035fd945a1d4a4c5ec77f1
  - Fix ensuring all home tiles are distinct in TODO
- [X] Referee does not handle infinite loops
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/a595dbea75286f5deeadc17dc1950264d0637745
  - Fix referee handling infinite loops in TODO
- [X] [GUI] Rendering of the home and the goal of the players
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/3f35e2adb0fd73d7852c278290b4a8114893e8ec
  - Added ability to render home and goal of players for GUI in TODO
- [X] [Constraint] Make gems all unique on each tile 
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/ceb18c8be496e67c48bbd072a99843dd7b8160a9
  - Unfinished fix for making a constraint for all unique gems, and generating boards with unique gems
- [X] [Constraint] Make board gems actually random
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/2ea407f56ac926c430a3333e931b79ec295c7aeb
  - Fix make board gems actually random in TODO
- [X] Move makefile to proper location
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/093a03d42a2e4f5fe22d2032bc5062f91bafc3f7
  - Fix moving makefile to proper location in TODO
- [X] Fixing purpose statement of takingTurns function in Referee
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/79136e597143f551d98619d120a15b8b9a1676db
  - Complete Fixing purpose statement of takingTurns function in Referee in TODO
- [X] Revisit Purpose statement for Player methods (add purpose statement to the class, and make sure purpose statements match the signatures for methods)
  -  https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/1f05c252587c84c549feecd9d719ce893e520a9e
  -  Complete purpose statements in Player and IPlayer
- [X] Add purpose statement for what Optional empty means (Pass)
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/b8a37efb7d7785881cd7f2e8759a4312bf945f9e
  - Fix add purpose statement for what Optional empty means in TODO
- [X] [Performance]: isReachable calls getReachableTiles rather than just stopping when the target tile is found
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/1a1292390d44dd633d4dff57ecdbb90976511a3f
  - Fix performance issue with isReachable not stopped when target is found in TODO
- [X] [Readability]: the return of runGame should be a new data structure
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/215a6e1e66a963459e390b8c2b1571a5123c747f
  - Fix the return of runGame should be a new data structure in TODO  
- [X] [Readability] SlidingAction should be a new data structure
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/60e28b5ea1f681bea29c39fac51a0011b6cfdd8b
  - Fix SlidingAction should be a new data structure in TODO
- [X] [Readability] movePlayersInGameState (abstract it)
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/c67cd420182e4087e30813bac137c2f2ea3dbe5e
  - Fix abstracting MovePlayer in GameState in TODO, and refactored board field naming
- [X] [GUI] Make the avatar + home + goal not be behind the path
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/4972e2a981491fe135285af730462ac93ebc7713
  - Fix attaching names to avatars, homes and goals in TODO
- [X] [GUI] The avatars on the board, don’t have names attached to them
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/4972e2a981491fe135285af730462ac93ebc7713
  - Fix attaching names to avatars, homes and goals in TODO
- [X] [GUI] Borders around the Tiles
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/e2f30d3967f1abd956a6af9d9a7b5a75c1088ce6
  - Fix spare tile formatting in GUI in TODO
- [X] [GUI] Spare tile is formatted in a way so that the spare is just really long, it doesn’t look like one tile
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/e2f30d3967f1abd956a6af9d9a7b5a75c1088ce6
  - Fix spare tile formatting in GUI in TODO
- [X] [GUI] Fix overlapping goals
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/2f24ceeff17b71c36f4652e1b30bd24f4bd308c9
  - Fix overlapping goals in TODO
- [X] [Confidence] Updating the previous test harnesses
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/ad25cbb3a9ea183cc264b52506792d33581335fd
  - Fix previous test harnesses in TODO
- [X] [Flexibility] Referee should be able to hand off to multiple observers
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/e8edeadba9dd7572c15cb24a7700798bc0c473cc
  - Fix referee being able to handle multiple observers
- [X] [Flexibility] Add a rule book to be passed into actions
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/27ea4d8b3a7c6c43894fbddb948093df423bb7fb
  - wip: add rules
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/8098c3cb4eac89b5281b312851e826d1551986d9
  - wip: replacing rules across classes with single rule book class
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/dfeb95b0af0726a36fa218d74a291405e941c8cf
  - Fix add a rule book in TODO 
- [X] [Removing duplicate code] Make misbehaving player classes extend current Player class
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/commit/68160179b9b01fa53a24235ff98f493b3b4a1f1e
  - Fix misbehaving player classes extend Player class in TODO
