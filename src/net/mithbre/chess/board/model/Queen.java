package net.mithbre.chess.board.model;

public class Queen extends Piece {

	public Queen(boolean white) {
		super(white, 'Q', Queen.moveset());
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
	
	@Override
	public int moveDirection(int pos, int square) {
		// see if square is in a direct line away
		int search = square - pos;
		
		//System.out.printf("Queen: search=%d  square=%d  pos=%d\n", search, square, pos);
		for (int primitive: getMoveset()) {
			
			//System.out.printf("Queen: primitive=%d  search=%d  search%%primitive=%d\n", primitive, search, search % primitive);
			
			if (search < 0 && primitive < 0 && search % primitive == 0) {
				return primitive;
			} else if (search > 0 && primitive > 0 && search % primitive == 0) {
				return primitive;
			}
		}
		return 0;
	}
}
