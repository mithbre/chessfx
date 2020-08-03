package net.mithbre.chess.board.model;

public class Bishop extends Piece {
	
	public Bishop(boolean color) {
		super(color, 'B', Bishop.moveset());
	}

	private static int[][] moveset() {
		// {{black}, {white}}
		int movement[][] = {{11, -11, 9, -9}, {11, -11, 9, -9}};
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
		
		//System.out.printf("bishop: search=%d  square=%d  pos=%d\n", search, square, pos);
		
		for (int primitive: getMoveset()) {
			
			//System.out.printf("bishop: primitive=%d  search=%d  search%%primitive=%d\n", primitive, search, search % primitive);
			
			if (search < 0 && primitive < 0 && search % primitive == 0) { // moving down
				return primitive;
			} else if (search > 0 && primitive > 0 && search % primitive == 0) { // moving up
				return primitive;
			}
		}
		return 0;
	}
}
