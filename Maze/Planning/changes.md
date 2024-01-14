To: Professor Felleisen

From: Jamie Lin, Eric Yip

Date: 11/10/2022

Subject: Design Task (Milestone 7)

# Blank tiles from the board
We give this change a ranking of 2 (easy) because there are 3 locations within our existing code base that need a 
single method change: the Connector class, the Unicode enum, and the AbstractObserver. We need to update the maps that 
store information of specific unicode characters to their open directions in the Connector class, add another enum field
to the Unicode enum to represent a blank tile, and update the path shapes that are generated in the AbstractObserver 
class. No changes would result in previous unit or integration tests breaking.

# Use movable tiles as goals
We give this change a ranking of 2 (easy) because there are once again only 3 locations in our code base that need small
changes in order to abstract previous functionality. In the GameState class, we will need to make 2 changes: adding a method to shift the goals by abstracting functionality from the method for moving the player's positions when sliding and rewriting the method for generating goal positions so generated goal tiles are not constrained to only fixed tiles. In our SlideAndInsert class, we would simply call the newly created goal-shifting method in GameState to shift the goals.

# Ask players to sequentially move several goals, one at a time
We give this change a ranking of 3 (moderate) because there are several places in the code base that need adjustments that will change fundamental implementation logic. The PlayerState class would need to store a queue of goal positions instead of a single goal, where the player's current goal is the first element of the queue and they have reached all their goals once the queue is empty. Referee will require a change to setting up players after they reach their goal, as it will now need to check if the player has reached an intermediate goal, and call setup on that player with their next target goal.