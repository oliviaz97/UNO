# UNO

author: Olivia Zhang


### Description

Java implementation of a traditional UNO game

### Features
**1. Multi-player**
* The game supports games of more than two players

**2. Customized rules**
* Challenge
    - In the case when the last played card is a WildDrawFour card, the urrent player can choose to challenge the last player
    - Challenge succeeds when the last player played the WildDrawFour card illegally (had other legal cards to play), in this case the last player is penalized with having to draw four cards instead
    - Challenge fails when the last player played the WildDrawFour card legally, in this case the current player is penalized for the failed challenge, and has to draw six cards instead of four cards

* Swap
    - In the case when the current player plays a Number card with number 7, he/she can pick one of the other players to swap hands with

**3. AI Player**
* A basic AI player is incorporated into the complete game
* Allows multiple AI player in the same game with at least one human player

### Tests
1. Test the Model
* Run the tests located in /src/test
2. Test the GUI and controller
* Refer to the Manual Test Plan


### Documentation
* Run the following command in /src to generate Doxygen files

`
doxygen Doxyfile
`