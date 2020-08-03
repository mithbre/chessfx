package net.mithbre.chess.board.model;

public class Pawn extends Piece {

	public Pawn(boolean color) {
		super(color, 'P', Pawn.moveset());
		// thanks to: https://stackoverflow.com/a/45132203
		// Stemmed from a simple want
		// "I want to pass an array to the parent class."
	}

	private static int[][] moveset() {
		// {{black}, {white}}
		int movement[][] = {{-20, -11, -10, -9}, {20, 11, 10, 9}};
		return movement;
	}
	
	@Override
	public char getID() {
		return super.identifier;
	}
	
	@Override
	public int moveDirection(int pos, int square) {
		// see if square is exactly 1 location away
		int search;
		
		// Only search one half based on color
		search = square - pos;
		
		//System.out.printf("pawn: search=%d\n", search);

		for (int primitive: getMoveset()) {
			if (search == primitive) {
				return primitive;
			}
		}
		return 0;
	}
}
