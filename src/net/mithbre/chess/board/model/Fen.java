package net.mithbre.chess.board.model;

import java.util.ArrayList;

class Fen {
	private final boolean WHITE = true, BLACK = false;
	private String[] splitFen, splitBoard;
	private String rawBoard, rawSideToMove, rawCastlingRights, rawEnPassant;
	private String fen;
	private char[] flatBoard = new char[64];
	private int halfMove, turn;
	private boolean sideToMove;

	// 5r2/2p2rb1/1pNp4/p2Pp1pk/2P1K3/PP3PP1/5R2/5R2 w - - 1 51
	// rank/rank/rank/rank/rank/rank/rank/rank a b c d e

	// a = {b, w} // black or white to move
	// b = [KQkq] // castling rights (uppercase for white, q for queenside, k for kingside)
	// c = square for en passant (the square skipped over for double push)
	// d = halfmove
	// e = turn number (note that this includes BOTH white and black moves on the same turn)// 

	public boolean getSideToMove() {
		if (rawSideToMove.equals("w")) {
			return WHITE;
		}
		return BLACK;
	}
	
	public char[] getFlatBoard() {
		return flatBoard;
	}
	
	public String[] getCastlingRights() {
		ArrayList<String> castlingRights = new ArrayList<String>();
		
		for (int rook = 0; rook < rawCastlingRights.length(); rook++) {
			switch (rawCastlingRights.charAt(rook)) {
			case 'Q': castlingRights.add("a1"); break;
			case 'K': castlingRights.add("h1"); break;
			case 'q': castlingRights.add("a8"); break;
			case 'k': castlingRights.add("h8"); break;
			}
		}
		
		String[] castlingRightsNotation = new String[castlingRights.size()];
		castlingRightsNotation = castlingRights.toArray(castlingRightsNotation);
		return castlingRightsNotation;
	}

	public String getEnPassant() {
		if (rawEnPassant.equals("-")) {
			return "";
		}
		return rawEnPassant;
	}

	public int getHalfMove() {
		return halfMove;
	}

	public int getTurn() {
		return turn;
	}
	
	public String getFen() {
		fen = "";
		fen += rawBoard + " ";
		
		if (sideToMove) {
			fen += "w ";
		} else {
			fen += "b ";
		}

		fen += rawCastlingRights + " ";
		fen += rawEnPassant + " ";
		fen += halfMove + " ";
		fen += turn;
		return fen;
	}
	
	// reset the state of the object.
	public void clear() {
		splitFen = splitBoard = null;
		rawBoard = rawSideToMove = rawCastlingRights = rawEnPassant = "";
		halfMove = turn = 0;
		flatBoard = new char[64];
	}
	
	// Collapses flatBoard to a fen representation
	public void collapseBoard() {
		int count = 0, file = 0;
		rawBoard = "";
		
		for (char square: flatBoard) {
			if (file == 8) {
				if (count > 0) {
					rawBoard = rawBoard + String.valueOf(count);
					count = 0;
				}
				rawBoard = rawBoard + "/";
				file = 0;
			}
			if (Character.isLetter(square)) {
				if (count > 0) {
					// append count
					rawBoard = rawBoard + String.valueOf(count);
					count = 0;
				}
				rawBoard = rawBoard + String.valueOf(square);
				file++;
			} else {
				count++;
				file++;
			}
		}
		if (count > 0) {
			rawBoard = rawBoard + String.valueOf(count);
			count = 0;
		}
	}

	// Expands the board into the same flatBoard setup Board uses.
	public void expandRawBoard() {
		int count = 0;
		
		for (String row: splitBoard) {
			for (int letter = 0; letter < row.length(); letter++) {
				if (Character.isLetter(row.charAt(letter))) {
					flatBoard[count] = row.charAt(letter);
					count++;
				} else {
					int blank = Character.getNumericValue(row.charAt(letter));
					for (int i = 0; i < blank; i++) {
						flatBoard[count] = '.';
						count++;
					}
				}
			}
		}
	}

	public void encodeFen(char[] flatBoard, boolean sideToMove,
			String castlingRights, String enPassantPos, int halfMove, int turn) {
		this.flatBoard = flatBoard;
		this.sideToMove = sideToMove;
		
		if (castlingRights.equals("")) {
			this.rawCastlingRights = "-";
		} else {
			this.rawCastlingRights = castlingRights;
		}
		this.rawEnPassant = enPassantPos;
		this.halfMove = halfMove;
		this.turn = turn;
		
		collapseBoard();
	}
	
	public int decodeFen(String rawFen) {
		splitFen = rawFen.split(" ");
		if (splitFen.length != 6) {
			System.out.println("fail");
			return -1;
		}

		rawBoard = splitFen[0];
		rawSideToMove = splitFen[1];
		rawCastlingRights = splitFen[2];
		rawEnPassant = splitFen[3];
		halfMove = Integer.parseInt(splitFen[4]);
		turn = Integer.parseInt(splitFen[5]);
		splitBoard = splitFen[0].split("/");
		
		expandRawBoard();
		return 1;
	}
}
