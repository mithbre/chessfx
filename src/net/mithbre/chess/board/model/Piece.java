package net.mithbre.chess.board.model;

public class Piece {
	protected char identifier;
	private boolean white; // true = white, false = black
	private char pieceClass;
	private int[] movement;
	
	public Piece(boolean color, char id, int[][] moveset) {
		white = color;
		pieceClass = id;
		if (white) {
			identifier = id;
			movement = moveset[1];
		} else {
			identifier = Character.toLowerCase(id);
			movement = moveset[0];
		}
	}

	public char getID() {
		return identifier;
	}
	
	public boolean getColor() {
		return white;
	}

	public char getPieceClass() {
		return pieceClass;
	}
	
	public int[] getMoveset() {
		return movement;
	}
	
	public int moveDirection(int pos, int square) {
		return 0;
	}
}


