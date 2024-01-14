**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 8

Indicate below each bullet which file/unit takes care of each task.

For `Maze/Remote/player`,

- explain how it implements the exact same interface as `Maze/Player/player`
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/25e757e022355af88629975fef3cd32704f779a5/Maze/src/main/java/ProxyPlayer.java#L12
  - Our ProxyPlayer implements the exact same interface as our Player class, as they both implement the IPlayer interface, which contains methods for setting up
  a player, taking a turn, and notifying them of the win status.
- explain how it receives the TCP connection that enables it to communicate with a client
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/25e757e022355af88629975fef3cd32704f779a5/Maze/src/main/java/Server.java#L148-L159
  - The ProxyPlayer receives a TCP connection from the Server, who will first establish that connection with the client. Once the client sends over a valid name,
  the Server will create the ProxyPlayer with the given name and socket connection, which allows the ProxyPlayer to send and receive messages from the client
  through this socket.
- point to unit tests that check whether it writes JSON to a mock output device
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/25e757e022355af88629975fef3cd32704f779a5/Maze/src/test/java/ProxyPlayerTest.java

For `Maze/Remote/referee`,

- explain how it implements the same interface as `Maze/Referee/referee`
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/25e757e022355af88629975fef3cd32704f779a5/Maze/src/main/java/ProxyReferee.java#L72-L107
  - Both the ProxyReferee and Referee delegate the setup, take turn, and win method calls to a player, and will receive the result of that delegation.
- explain how it receives the TCP connection that enables it to communicate with a server
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/25e757e022355af88629975fef3cd32704f779a5/Maze/src/main/java/Client.java#L28-L39
  - The ProxyReferee will receive a TCP connection from the Client, who will first establish a connection with the server. After this socket connection is
  created, the Client will then create the ProxyReferee with the socket, which is used by the ProxyReferee to send and receive messages from the server.
- point to unit tests that check whether it reads JSON from a mock input device
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/25e757e022355af88629975fef3cd32704f779a5/Maze/src/test/java/ProxyRefereeTest.java

For `Maze/Client/client`, explain what happens when the client is started _before_ the server is up and running:

- does it wait until the server is up (best solution)
  - https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/25e757e022355af88629975fef3cd32704f779a5/Maze/src/main/java/Client.java#L28-L39
  - Yes, the client will wait until the server is up to connect. Specifically, on starting up, the client will create the connection on a socket and send the
  player name through that socket. If the server is not up at this point, this socket will not do anything until the server fully establishes and accepts the
  connection, in which case the server will read the name that was sent earlier from the socket.
- does it shut down gracefully (acceptable now, but switch to the first option for 9)
  - Our client will not shut down gracefully since we implemented the first option.

For `Maze/Server/server`, explain how the code implements the two waiting periods:

- is it baked in? (unacceptable after Milestone 7)
  - No, our waiting periods are not baked in since we create a single method that is a single waiting period and we call it twice in our sign-up phase function.
- parameterized by a constant (correct).
  -  Sign-up method that calls the waiting period twice: https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/25e757e022355af88629975fef3cd32704f779a5/Maze/src/main/java/Server.java#L70-L80 
  -  Method that runs a single waiting period: https://github.khoury.northeastern.edu/CS4500-F22/stoic-whales/blob/1b79393b6b4030c222db32dacc4d600d179b6a65/Maze/src/main/java/Server.java#L90-L96
  - Yes, our two waiting periods are parameterized by a constant (same duration of time). 

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

