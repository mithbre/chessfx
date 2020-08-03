package net.mithbre.chess.board.model;

public class Knight extends Piece {

	public Knight(boolean white) {
		super(white, 'N', Knight.moveset());
	}
	
	private static int[][] moveset() {
		// {{black}, {white}}
		int movement[][] = {{21, -21, 19, -19, 12, -12, 8, -8}, {21, -21, 19, -19, 12, -12, 8, -8}};
		return movement;
	}

	@Override
	public char getID() {
		return super.identifier;
	}
	
	@Override
	public int moveDirection(int pos, int square) {
		// see if square is exactly 1 location away
		int search = pos - square;
		for (int primitive: getMoveset()) {
			if (search == primitive) {
				return primitive;
			}
		}
		return 0;
	}
}
