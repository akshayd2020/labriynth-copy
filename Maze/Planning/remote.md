To: Professor Felleisen

From: Jamie Lin, Jessica Su

Date: 11/3/2022

Subject: Design Task for Games! (Milestone 6)

# Gathering Players
The protocol for gathering players will be that clients can connect to the server by utilizing 
a proxy object that has the ability to delegate to the actual component that deals with sorting
the signed up players and initializing the wait list for a game. The proxy object will not only
forward the request to the actual component, it could display to the user that their request for
registration is being processed. 

# Launching a Game
Once the component dealing with all the player signups gets the list of players to actually play
in the game, the referee will run a game and allow the users to take turns and make decisions
through the provided Player API that allows for the players to return moves that the referee
will make if the moves are valid or do not lead to any failure modes. If any of the players' moves
result in a behavior that is not allowed, the referee will stop interacting with them. 

# Reporting the Result
After the game has ended, the referee will calculate all of the winners and take note of all 
those who are losers (not misbehaved player nor winners) and interacts with the players through
the communication component that the players interact with. 
