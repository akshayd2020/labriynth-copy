To: Professor Felleisen

From: Jamie Lin, Jessica Su

Date: 9/30/2022

Subject: Plan for First Three Sprints (Milestone 1)

The first three sprints will create a model, view, and controller for the first iteration of the 
Labyrinth game with a 7x7 board. The purpose of the model is to perform logical registration, 
set up the game board, and handle plays. The purpose of the controller is to 
delegate to the model upon user actions. The purpose of the view is to create a GUI for the user 
and handle user input by delegating to the controller. In future sprints, we will be implementing 
the ability to inject "house players" and add visual components for observers. The first, second,
and third sprints correspond to the completion of the model, controller, and view 
respectively.

The goal of the first sprint is to create the model's interface and implement necessary 
functionality. The purpose of the model is to register players, set up the game board, and 
handle plays. We implement the model first because the game play is outlined in the model and is 
the foundation to build the controller and the view. We estimate 4.5 hours to test and 
implement the registration functionality, 4 hours to test and implement the board set up, and 5 
hours to implement the functionality for the game play. With 3 hours for buffer time, we estimate that creating the model 
will take 16 hours. Thus, the goal of the first sprint is to implement the model.

The goal of the second sprint is to create the controller's interface and implement the 
corresponding functionality.
The controller connects to the clients via TCP, and delegates to the model for logical sign-up and setting up the game.
This builds upon the first sprint because the controller will delegate to the model for any game logic. 
The controller will also delegate to the model to handle user input during a game play. We estimate 5.25 hours to create 
the controller interface and read in client input. We estimate 1.25 hours to implement delegation to the model to set up 
the game board. We estimate it will take 2.5 hours for us to implement the functions that delegate to the model when users
perform an action. Our sprint will also include 4 hours to finish up any incomplete parts of the model. With an additional 
3 hours for buffer time, this sprint will take 16 hours to complete the model and the controller.

The goal of the third sprint is to create the view interface and implement the corresponding functionality. This builds
upon the second sprint because the view will call the controller methods in order to update the model upon user actions.
We estimate 2 hours to create the view interface and plan the design of the game's rendered components. We also estimate
3.5 hours to render the game's tiles, spare tile, home and goal tiles, and the characters' avatars. We estimate 5 hours 
to implement user actions. With 4.5 hours of buffer time, this sprint will take 16 hours to implement the view.

The first iteration of the Labyrinth game with a 7x7 board will be completed as a 
result of the first three sprints.
