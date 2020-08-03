package net.mithbre.chess.board.model;

public class Rook extends Piece {
	private boolean castlingRights = true;
	
	public Rook(boolean white) {
		super(white, 'R', Rook.moveset());
	}
	
	private static int[][] moveset() {
		// {{black}, {white}}
		int movement[][] = {{10, -10, 1, -1}, {10, -10, 1, -1}};
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
		// see if square is in a direct line away
		int search = square - pos;
		
		//System.out.printf("rook: search=%d  square=%d  pos=%d\n", search, square, pos);
		
		for (int primitive: getMoveset()) {
			
			//System.out.printf("rook: primitive=%d  search=%d  search%%primitive=%d\n", primitive, search, search % primitive);
			
			if (search < 0 && primitive < 0 && search % primitive == 0) { // moving down
				return primitive;
			} else if (search > 0 && primitive > 0 && search % primitive == 0) { // moving up
				return primitive;
			}
		}
		return 0;
	}
}
