package net.mithbre.chess.board.model;

public class King extends Piece {
	private boolean castlingRights = true;
	
	public King(boolean white) {
		super(white, 'K', King.moveset());
	}
	
	private static int[][] moveset() {
		// {{black}, {white}}
		int movement[][] = {{11, 10, 9, 1, -11, -10, -9, -1}, {11, 10, 9, 1, -11, -10, -9, -1}};
		return movement;
	}

	@Override
	public char getID() {
		return super.identifier;
	}
	
	public void revokeCastlingRights() {
		castlingRights = false;
	}
	
	public boolean getCastlingRights() {
		return castlingRights;
	}
	
	@Override
	public int moveDirection(int pos, int square) {
		// see if square is exactly 1 location away
		int search = square - pos;
		if (search == 2 || search == -2) {
			// Wants to castle
			return (getCastlingRights()) ? search : 0;
		} else {
			for (int primitive: getMoveset()) {
				if (search == primitive) {
					return primitive;
				}
			}
		}
		return 0;
	}
}
