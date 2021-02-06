# DESIGN Document for PROJECT_NAME
## NAME(s) 
    Cole Spector

## Role(s) 
    Cole: Primary (and only) coder


## Design Goals
    I made it simple to add new levels, and balls into the game
    I made what I thought was a simple way to add powerups and bricks in a switch statment, however
    after recent classes i have learned that this wouldve been better to be done with extended classes.
    


## High-Level Design
    The Main.java class is called to run the game, and sets up Level.java
    Level.java sets up the game window, creates the paddle, and orchistrates all of the other classes
    Ball.java creates individual balls to be used during the game.  It interacts with Level.java, StatusDisplay.java, and Brick.java
    Brick.java creates individual bricks to be used during the game.  It interacts with Level.java, StatusDisplay.java, Powerup.java and Ball.java
    Powerup.java creates a queue of Powerups to be used during the game.  It interacts with Level.java, StatusDisplay.java, Brick.java, and Ball.java
    SplashScreen.java creates the splash screen to be used before, and afte the game. It interacts with StatusDisplay.java
    StatusDisplay.java creates the user information (lives, score, level) to be used during the game, and aids all other classes.

## Assumptions or Simplifications
    I tried to make all objects that wouldn't simply need getters and setters (i.e. not the paddle) their own class.

## Changes from the Plan
    The things i changed from my plan were i made the lazer powerup fire automatically, the powerup block drop its powerup instead of automatically giving it, i scrapped the barrier cheat and added a powerup drop cheat, added an indestructable block, and scrapped the paddle class.


## How to Add New Features
    The new features I would add to my project would be to split Powerup, SplashScreen and Brick into multiple class extensions.
    I would do this for Powerup by creating one class which would populate the queue, and then another abstract class which would have 3 extensions, one for each powerup type.
    For the SplashScreen class, i would add a class with abstract method for the showSplashScreen method, which would be overridden by the extensions (one for each game condition)
    Finally, for the brick class I would have the empty brick be the default, and then have class extensions for each other brick type, overriding the isHit and setBrickProperties methods.