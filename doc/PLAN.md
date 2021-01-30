# Game Plan
### Cole Spector

#### Examples

## Interesting Breakout Variants
Two breakout variants I found interesting and 
seemingly fun to implement were Super Breakout 
and a version of Devilish.  The reason I like the Super
Breakout variant is for the powerups, which I would have either
fall down, or as simply as a block type.  My Devilish version would
be similar to the variant given, however when I played a similar
Breakout game it was not a scroller, but rather getting to the top
would allow you to access a new level.  These two variants
would be fun to implement together, and remind me of
Breakout styles I am used to playing.

## Paddle Ideas

I feel that having the paddle bounce the ball differently
depending on where it hits is an essential part of breakout,
so I would have the left third bounce it back left, the right
third bounce it back right, and the middle third bounce it back regularly.
  Along with this, I plan to have the paddle able to move (minimally)
up and down such as the top paddle in the devilish
variant video.


## Block Ideas

The three block ideas I plan to implement into my breakout game
are a block which drops a powerup when destroyed (that the player
must "catch" with their paddle) which will be randomly placed in the level
replacing normal block, mirroring their appearance this block looks like a normal block,
a block which automatically gives a
powerup when destroyed (the player can see which powerup they will get),
and a block version which takes
multiple hits to destroy.


## Power-up Ideas

Three power-up ideas I plan to use in my Breakout game
are: extending the length of the paddle, adding an extra
ball, and having it so you get a lazer you can fire by pressing
a specific key (most likely shift).


## Cheat Key Ideas

The four different cheat keys I plan to implement is 
adding a barriar around the screen so that the player cannot
lost, but, because this is a Devilish style game where the objective
is to get to the top of the screen, they cannot win either.
  I also plan to add a cheat which clears all blocks in the level, 
a cheat which increases the paddle movement speed, and which
decreases the ball movement speed.


## Level Descriptions

Below I have created ASCII art for the three levels:

key:

| o = open space

| # = normal block

| @ = unbreakable block

| X = automatic (shown) power up

| B = multi-hit block


Castle:

|________________|

|o@@oo@@oo@@oo@@o|

|oXXooXXooXXooXXo|

|################|

|################|

|######BBBB######|

|##XX##BooB##XX##|

|######BooB######|

|________________|

----------

Pyramid:

|________________|

|################|

|o#####XXXX#####o|

|oo#####XX#####oo|

|ooo##########ooo|

|oooo########oooo|

|oooooo####oooooo|

|ooooooo##ooooooo|

|________________|

-------

Demolition Derby:

|________________|
|################|

|BBBBBBBBBBBBBBBB|

|BXBooooooooooBXB|

|BXBooooooooooBXB|

|BXBoooooooooXBXB|

|BBBBBBBooBBBBBBB|

|################|

|________________|


## Class Ideas

Five different classes which may be useful for me to implement 
will be:

1. A "Stage" class, which will take in a String Array (with each index being one
line from the ASCII image above) as a parameter, with a win() method which will simply
return the next stage to go to once the level is cleared (the ball reaches the top).

2. A "Block" class which will take in whether or not it is a shown powerup block (boolean)
as a parameter, and will have a hit() method which will determine
   how the block behaves when it is hit (for a hidden powerup block this means dropping the powerup,
   for a shown powerup block this means giving the powerup, and for a multi-hit block this means "cracking")
   
3. A "Powerup" class which will take in no parameters, and an 
powerupType() method which randomly chosses the type of powerup.
   
4. A "CheatCode" class which will take in a KEY as a parameter,
and will have a breakGame() method which will implement the cheat.
   
5. A "paddle" class which will take in no parameters, and will have a
 setX() method to set the X position of the paddle.