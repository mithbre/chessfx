This project is a stepping stone for another project of mine in the works. As such, at some point
after the engine is written this project will end and be rewritten in C/C++ for that other project.
This primarily exists so I have something to compare against and have a nice visual reference.

TODO:

///////////////////////////////////////////////////////////////////////////////////////////////////

Move generation will probably help with some of the bugs. If that is removed from board and shared
with the engine (whenever that makes an appearance) then it should help simplify the board a little
and allow for separate testing.

///////////////////////////////////////////////////////////////////////////////////////////////////

Importing a game will break if there's a promotion involved.
Exporting a game will overlook promotions
I'm not storing either of these.

UCI engines apparently use a method similar to mine for communicating with the board
I use "e2 e4"
UCI engines would use "e2e4".
UCI engines for promotion would use h7h8q for a queen promotion.

More info here:
http://wbec-ridderkerk.nl/html/UCIProtocol.html

///////////////////////////////////////////////////////////////////////////////////////////////////

Move detection is still buggy.
I intend to throw a few hundred thousand Lichess games at this to watch it break marvelously.
https://database.lichess.org/standard/lichess_db_standard_rated_2014-07.pgn.bz2
(1,048,440 games; 176 MB; ~1 GB inflated)
Haven't decided on how to automate the failure detection.

///////////////////////////////////////////////////////////////////////////////////////////////////
