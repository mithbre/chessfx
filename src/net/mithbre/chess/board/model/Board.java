package net.mithbre.chess.board.model;

import java.util.ArrayList;

public class Board {
	private final int SQUARES = 120;
	private final boolean WHITE = true, BLACK = false;
	private final int CHECK = -1, CHECKMATE = -2, STALEMATE = -3;
	private final int PROMOTEWHITE = -10;
	private final int PROMOTEBLACK = -11;
	private final int NORMAL = 0;
	private final int[] sentinelSquares = {  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,
											11,  12,  13,  14,  15,  16,  17,  18,  19,  20,  29,
											30,  39,  40,  49,  50,  59,  60,  69,  70,  79,  80,
											89,  90,  99, 100, 101, 102, 103, 104, 105, 106, 107,
										   108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119};
	
	private ArrayList<String> previousMoves = new ArrayList<>();
	private ArrayList<String> fenStates = new ArrayList<>();
	private char[] flatBoard = new char[64];
	private Piece[] board = new Piece[SQUARES];
	private String[] specialMove = {"", ""}; // To alert external interfaces to En Passant and Castling
	private int halfMove = 1 , turnNum = 1, enPassantPos = 0;
	private int kingPosWhite, kingPosBlack;
	private boolean sideToMove = WHITE;
	private boolean enPassant = false, castle = false;
	
	private Fen fen;
	
	public Board(String initialFen) {
		/* Why go through all the trouble?
		 * original intention was to allow me to pass the FEN
		 * to the engine so it could create a new board
		 * for generating moves without effecting the main one.
		 * 
		 * Also it could be used (with some changes) to see if 
		 * 3 move repetition has been reached.
		 * 
		 * If I understand it correctly...drop halfMove and turnNum
		 * and then just either compare the remainder to all other moves,
		 * or hash and compare. As well as count duplicates.
		 * Should work anyway.
		 * 
		 * Also testing positions, but I implemented this
		 * FEN setup waaay too late. For that to happen.
		 */
		
		// Setup board based on FEN
		fenStates.add(initialFen);
		fen = new Fen();
		fen.decodeFen(initialFen);
		this.flatBoard = fen.getFlatBoard();
		buildBoard(fen.getCastlingRights());
		
		if (fen.getEnPassant().equals("")) {
			enPassantPos = 0;
		} else {
			enPassantPos = translatePos(fen.getEnPassant());
		}
		halfMove = fen.getHalfMove();
		this.sideToMove = fen.getSideToMove();
		turnNum = fen.getTurn();
		
		updateFlatBoard();
	}

	private void buildBoard(String[] castlingRights) {
		int actualSquare = 91;
		for (int square = 0; square < flatBoard.length; square++) {
			while (offBoard(actualSquare)) {
				// drop to beginning of next rank
				actualSquare -= 18; 
			}
			switch(flatBoard[square]) {
			case '.': board[actualSquare] = null;			   break;
			case 'P': board[actualSquare] = new Pawn(WHITE);   break;
			case 'p': board[actualSquare] = new Pawn(BLACK);   break;
			case 'N': board[actualSquare] = new Knight(WHITE); break;
			case 'n': board[actualSquare] = new Knight(BLACK); break;
			case 'B': board[actualSquare] = new Bishop(WHITE); break;
			case 'b': board[actualSquare] = new Bishop(BLACK); break;
			case 'R': board[actualSquare] = new Rook(WHITE);   break;
			case 'r': board[actualSquare] = new Rook(BLACK);   break;
			case 'Q': board[actualSquare] = new Queen(WHITE);  break;
			case 'q': board[actualSquare] = new Queen(BLACK);  break;
			case 'K':
				board[actualSquare] = new King(WHITE);
				kingPosWhite = actualSquare;
				break;
			case 'k':
				board[actualSquare] = new King(BLACK);
				kingPosBlack = actualSquare;
				break;
			}
			
			
			if (castlingRights.length == 0) {
				// no one has castling rights
				if (board[actualSquare] != null && board[actualSquare].getPieceClass() == 'R') {
					((Rook) board[actualSquare]).revokeCastlingRights();
				} else if (board[actualSquare] != null && board[actualSquare].getPieceClass() == 'K') {
					((King) board[actualSquare]).revokeCastlingRights();
				}
			} else if (board[actualSquare] != null && board[actualSquare].getPieceClass() == 'R') {
				// if the square we just filled is a rook
				boolean rights = false;
				for (String pos: castlingRights) {
					// see if it has castling rights and revoke as necessary.
					if (pos.equals(translatePos(actualSquare))) {
						rights = true;
						break;
					}
				}
				if (!rights) {
					((Rook) board[actualSquare]).revokeCastlingRights();
				}
			}
			actualSquare++;
		}
	}
	
	public String createFen() {
		String castlingRights = "";
		// determine white castling rights
		if (board[25] != null && board[25].getPieceClass() == 'K' &&
			((King) board[25]).getCastlingRights()) {
			if (board[28] != null && board[28].getPieceClass() == 'R' &&
				((Rook) board[28]).getCastlingRights()) {
				castlingRights += 'K';
			}
			if (board[21] != null && board[21].getPieceClass() == 'R' &&
				((Rook) board[21]).getCastlingRights()) {
				castlingRights += 'Q';
			}
		}
		
		// determine black castling rights
		if (board[95] != null && board[95].getPieceClass() == 'K' &&
			((King) board[95]).getCastlingRights()) {
			if (board[98] != null && board[98].getPieceClass() == 'R' &&
				((Rook) board[98]).getCastlingRights()) {
				castlingRights += 'k';
			}
			if (board[91] != null && board[91].getPieceClass() == 'R' &&
				((Rook) board[91]).getCastlingRights()) {
				castlingRights += 'q';
			}
		}

		fen.clear();
		String enTemp = "-";
		if (enPassantPos != 0) {
			enTemp = translatePos(enPassantPos);
		}

		fen.encodeFen(flatBoard, sideToMove, castlingRights, enTemp, halfMove, turnNum);
		return fen.getFen();
	}
	
	public char getPieceID(int loc) {
		return board[loc].getID();
	}
	
	public String[] getSpecialMove() {
		return specialMove;
	}
	
	public void consumeSpecialMove() {
		specialMove[0] = "";
		specialMove[1] = "";
	}
	
	public char length() {
		return SQUARES;
	}
	
	public int move() {
		if (true) {
			halfMove++;
		}
		return -1; // invalid
	}
	
	public char[] getFlatBoard() {
		return flatBoard;
	}
	
	public String getCurrentFen() {
		return fenStates.get(fenStates.size() - 1);
	}
	
	public ArrayList<String> getPreviousMoves() {
		return previousMoves;
	}
	
	private void updateFlatBoard() {
		int count = 0;
		for (int rank = 91; rank >= 21; rank-=10) {
			for (int file = 0;  file < 8; file++) {
				
				if (board[rank+file] == null) {
					flatBoard[count] = '.';
				} else {
					flatBoard[count] = board[rank+file].getID();
				}
				count++;
			}
		}
	}
	
	public String translatePos(int pos) {
		// get the 10's place
		int file = pos-(pos / 10 * 10);
		// get the 1's place, transpose up to integer character codes.
		int rank = (pos/10);
		// transpose up to character codes.
		file += ('a' - 1);
		rank += ('0' - 1);
		
		String value = Character.toString(file) + Character.toString(rank);
		return value;
	}
	
	public int translatePos(String pos) {
		int file = pos.charAt(0);
		int rank = pos.charAt(1);

		// drop the letter down to a multiple of 10
		file = file - 'a' + 1;
		// drop the character representation down to an integer representation
		rank = (rank - '0' + 1) * 10 ;

		return file + rank;
	}
	
	public boolean offBoard(int square) {
		for (int s: sentinelSquares) {
			if (square == s) {
				return true;
			}
		}
		return false;
	}
	
	private boolean leapfrog(int start, int end, int primitive) {
		if (board[start].getPieceClass() != 'N' && board[start].getPieceClass() != 'P') {
			// Make sure that there's nothing between the start and end square
			//System.out.printf("leapfrog: start=%d  primitive=%d  end=%d  start+prim=%d\n", start, primitive, end, start+primitive);
			for (int square = start + primitive; square != end; square += primitive) {
				if (board[square] != null) {
					return true;
				}
			}
		} else if (board[start].getPieceClass() == 'P') {
			// handle leapfrogging during double push
			if (primitive == 20) {
				if (board[start+10] != null) {
					return true;
				}
			} else if (primitive == -20) {
				if (board[start-10] != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	private int canCastle(int start, int primitive) {
		// setup variables
		int direction = 0, rookPos = 0;

		if (primitive == 2) {
			direction = 1; // kingside
			rookPos = (board[start].getColor()) ? 28 : 98; // white : black
		} else {
			direction = -1; // Queenside
			rookPos = (board[start].getColor()) ? 21 : 91; // white : black
		}
		
		// Since the check for castling rights is less costly lets do that first.
		
		// verify !null, pieceClass, castlingRights
		if (board[rookPos] != null && board[rookPos].getPieceClass() == 'R' && !((Rook) board[rookPos]).getCastlingRights()) {
			if (primitive == -2) {
				//System.out.println("castling: Queenside rook has already moved.");
				return -1;
			} else {
				//System.out.println("castling: Kingside rook has already moved.");
				return -2;
			}
		} else {
			// Everyone has castling rights.
			// See if the king or ANY SQUARE between and including the rook are under attack.

			// Check all the things!
			for (int square = start; square <= rookPos; square += direction) {
				//System.out.printf("board: start=%d  square=%d  castleEnd=%d  dir=%d\n", start, square, rookPos, direction);
				if (underAttack(square, !board[start].getColor())) {
					//System.out.println("castling: Involved square under attack.");
					return 0;
				}
			}
		}
		specialMove[0] = translatePos(rookPos);
		if (rookPos == 21) {		// queenside white
			specialMove[1] = "d1";
		} else if (rookPos == 28) {	// kingside white
			specialMove[1] = "f1";
		} else if (rookPos == 91) {	// queenside black
			specialMove[1] = "d8";
		} else if (rookPos == 98) {	// kingside black
			specialMove[1] = "f8";
		}

		castle = true;
		return 1;
	}
	
	// this can check if ANY square is under attack from the color passed in.
	public boolean underAttack(int square, boolean color) {
		int knightMoves[] = {8, 12, 19, 21, -8, -12, -19, -21};
		int repeatingMoves[] = {1, 9, 10, 11, -1, -9, -10, -11};
		int ray = 0;
		
		// idea is to step away from square in one direction until either
		// a. sintenelSquare is found
		// b. a piece is found
		// if a OR b of the opposite color, continue
		// if b of the same color
		// ask if it is possible to attack square.
		// if true, return true.
		// if all possibilities are exhausted, then return false.
		
		for (int primitive: knightMoves) {
			ray = square + primitive;
			if (board[ray] != null && board[ray].getPieceClass() == 'N' && board[ray].getColor() == color) {
				return true;
			}
		}
		
		
		// cast ray from king in one direction.
		for (int primitive: repeatingMoves) {
			ray = square + primitive;
			// if not offBoard
			while (!offBoard(ray)) {
				//System.out.printf("underAttack: square=%d  prim=%d  ray=%d\n", square, primitive, ray);
				// not null and color we're looking for
				if (board[ray] != null && (board[ray].getColor() == color)) {
					// test to see if it can take the king.
					if (board[ray].getPieceClass() == 'P') {
						if ((board[ray].getColor() == color) && (square + 9 == ray || square + 11 == ray) ||
							(board[ray].getColor() == color) && (square - 9 == ray || square - 11 == ray)) {
							//System.out.println("pawn threat");
							return true;
						} else {
							//System.out.println("not pawn threat");
							break;
						}
						//return false; //for now
					} else if (board[ray].moveDirection(ray, square) != 0) {
						// since there's nothing else between the attacked square and whatever this is, the square is being attacked.
						//System.out.printf("underAttack: square=%d by=%d\n", square, ray);
						return true;
					}
				} else if (board[ray] != null && board[ray].getColor() != color) {
					// cast next ray
					break;
				}
				ray += primitive;
			}
		}

		//System.out.println("inCheck: not in check");
		return false;
	}
	
	public boolean testMove(int start, int end) {
		Piece hold = board[end];
		board[end] = board[start];
		board[start] = null;

		int kingPos;
		if (board[end].getPieceClass() == 'K') {
			kingPos = end;
		} else {
			if (board[end].getColor()) {
				kingPos = kingPosWhite;
			} else {
				kingPos = kingPosBlack;
			}
		}

		if (inCheck(kingPos)) {
			// So close, but the friendly King is in check after moving.
			board[start] = board[end];
			board[end] = hold;
			return false;
		} else {
			// Put the piece back
			board[start] = board[end];
			board[end] = hold;
		}
		return true;
	}
	
	// Both king positions must be checked for Check condition
	public boolean inCheck(int kingPos) {
		//System.out.printf("inCheck: kingPos=%d\n", kingPos);
		boolean color = board[kingPos].getColor();
		return underAttack(kingPos, !color);
	}
	
	
	private boolean inStalemate(int kingPos, boolean color) {
		int pos;
		boolean stalemate = true;
		// step through board
		for (int rank = 91; rank >= 21; rank-=10) {
			for (int file = 0;  file < 8; file++) {
				pos = rank + file;
				
				// for every piece of color
				if (board[pos] != null && board[pos].getColor() == color) {
					//System.out.printf("inStalemate: checking square %d\n", pos);
					int[] primitiveMoves = board[pos].getMoveset();
					// try a primitive move.
					for (int move: primitiveMoves) {
						// check to see if move is legal
						// if the move isn't offboard
						if (!offBoard(pos+move)) {
							if (board[pos+move] == null && testMove(pos, pos+move)) {
								// no one there, and no check afterwards
								//System.out.printf("No one at square %d\n", pos+move);
								//System.out.printf("inStalemate: no\n");
								stalemate = false;
								// continue ray
							} else if (board[pos+move] != null &&
									   board[pos+move].getColor() == color) {
								// friendly piece is there.
								//System.out.printf("Friendly piece at square %d\n", pos+move);
								continue; // next ray
							} else if (board[pos+move] != null &&
									   board[pos+move].getColor() != color &&
									   testMove(pos, pos+move)) {
								// unfriendly piece is there. Not in check after move
								//System.out.printf("Unfriendly piece at square %d\n", pos+move);
								stalemate = false;
							} else {
								// unfriendly piece is there and in check after move.
								//System.out.printf("Unfriendly piece at square %d, check\n", pos+move);
								continue;
							}
						} else {
							//System.out.printf("Square %d offboard\n", pos+move);
						}
						
						if (!stalemate) {
							return false;
						}
					}
				}
			}
		}
		return stalemate;
	}
	
	/**
	 * Move the chess piece if possible.
	 * <p>
	 * It checks the validity of a move, moves a piece if it's legal and
	 * returns information about the state of the board.
	 *
	 * @param  String[] notation in the form of ["a1","a2"]
	 * @return int 1 Legal move
	 * 		   int 0 Illegal move
	 * 		   int -1 Check
	 * 		   int -2 Checkmate
	 * 		   int -3 Stalemate
	 */
	
	// TODO: implement fen export and export after each move (if this is done then 3 fold rep is achievable).
	public int move(String notation) {
		String[] turn = splitNotation(notation);
		//System.out.printf("\nboard: %s %s\n", turn[0], turn[1]);
		// Oh holy hell. What did I get myself into?
		
		// Translate from user friendly positioning to array friendly indices.
		int start = translatePos(turn[0]);
		int end = translatePos(turn[1]);
		int primitive;
		
		// Startup the great filter
		if (board[start] == null) {
			// Nothing to move
			//System.out.println("Nothing to move.");
			return 1;
		} else if (sideToMove != board[start].getColor()) {
			// Wrong color being moved
			//System.out.println("Wrong color being moved.");
			return 2;
		} else if (board[end] != null && sideToMove == board[end].getColor()) {
			// Why are you attempting to take your own piece?
			//System.out.println("Attempted to capture own piece.");
			return 3;
		} else {
			// Correct color
			//System.out.println("board: Correct color.");

			// Ask piece if movement direction is legal for it.
			primitive = board[start].moveDirection(start, end);
			if (primitive == 0) {
				// Piece in question can't move as requested.
				// King castling rights have already been considered.
				//System.out.println("Piece is unable to move as requested.");
				return 4;
			}

			// What type of move is it?
			if (board[start].getPieceClass() == 'K' && (primitive == -2 || primitive == 2)) {
				// Congratulations, it's a King and he wants to castle.

				// REMINDER, castling rights has already been checked by the King.
				// Gracious Lord that he is.
				//System.out.println("board: Castling.");
				
				int castle = canCastle(start, primitive);
				switch (castle) {
					case  0: return 12; // Involved square under attack.
					case -1: return  5; // Queenside rook has already moved.
					case -2: return  6; // Kingside rook has already moved.
				}
				// otherwise we're good
			} else if (board[start].getPieceClass() == 'P') {
				//System.out.println("board: Pawn move.");
				// capture ?
				//System.out.printf("board: prim=%d  start=%d  start/10=%d\n", primitive, start, start / 10);
				if (( board[start].getColor() && (primitive ==  9 || primitive ==  11)) ||   // white
					(!board[start].getColor() && (primitive == -9 || primitive == -11)) ) {  // black
					//System.out.println("board: Pawn capture");
					
					// get previous move
					if (turnNum == 1) {
						return 7;
					}
					
					String[] lastMove = splitNotation(previousMoves.get(previousMoves.size() - 1));
					int checkAgainst = translatePos(lastMove[1]);
					
					//System.out.printf("cA=%d  start=%d  start+1=%d  start-1=%d\n", checkAgainst, start, start+1, start-1);
					//System.out.printf("color=  start/10=%d\n", start/10);
					
					// I apologize for what you're about to see.
					// Are we trying to do an en passant?
					
					//////////////////////////////////////////////////////////////////////////
					// TODO: Can this be simplified?
					// during a double push, simply store the square number being skipped over
					// then here check to see if thats where the pawn is going, if so, en passant?
					//////////////////////////////////////////////////////////////////////////
					if (   board[checkAgainst].getPieceClass() == 'P' &&				// last move involved a pawn
						(  start+1 == checkAgainst || start-1 == checkAgainst) &&    	// it's next to the current piece
						(( board[start].getColor() && checkAgainst+10 == end) ||		// white's current move ends _behind_ that pawn.
						 (!board[start].getColor() && checkAgainst-10 == end)) &&		// black's current move ends _behind_ that pawn.
						(( board[start].getColor() && start / 10 == 6) ||				// white is on rank 6
						 (!board[start].getColor() && start / 10 == 5))) {				// black is on rank 5
						
						//System.out.println("en passant");
						// need to let the interface know what just transpired.
						specialMove[0] = lastMove[1];
						specialMove[1] = "";
						enPassantPos = translatePos(lastMove[1]);
						enPassant = true;
					} else if (board[end] == null) {
						return 7;
					}
				} else if ((primitive ==  20 && start / 10 != 3) || // white not on original rank
					       (primitive == -20 && start / 10 != 8)) { // black not on original rank
					// Attempt to double push on a non starting rank
					//System.out.println("Illegal double push.");
					return 8;
				} else if (primitive % 10 == 0) {
					// prevent capture attempt during normal movement or doublepush
					if (board[start + primitive] != null) {
						//System.out.println("board: Pawns can't capture like that.");
						return 11;
					}
				}
			}
		}
		
		if (leapfrog(start, end, primitive)) {
			//System.out.println("This isn't leapfrog.");
			return 9;
		}

		// Will king be in check after moving?
		// Temporarily move the piece to see if it was a pinned piece.
		if (!testMove(start, end)) {
			//System.out.println("Friendly King in check after moving.");
			return 10;
		}

		// Congratulations you've made it past the great filter
		// officially move piece
		//System.out.println("board: Move piece.");
		//Piece old = board[end]; // keep track of what used to be in the ending spot
		board[end] = board[start];
		board[start] = null;
		
		if (castle) {
			board[translatePos(specialMove[1])] = board[translatePos(specialMove[0])];
			board[translatePos(specialMove[0])] = null;
			castle = false;
		}
		
		if (board[end].getPieceClass() == 'P') {
			
		}
		if ((board[end].getPieceClass() == 'P' && board[end].getColor() && end / 10 == 9) || // white on rank 8
			(board[end].getPieceClass() == 'P' &&!board[end].getColor() && end / 10 == 2)) { // black on rank 1
				// promotion time
			return (board[end].getColor()) ? PROMOTEWHITE : PROMOTEBLACK;
		}
		
		if (enPassant) {
			// remove the offending pawn.
			//String[] lastMove = splitNotation(previousMoves.get(previousMoves.size() - 1));
			//board[translatePos(lastMove[1])] = null;
			board[enPassantPos] = null;
		}

		return updateBoardState(end, notation, enPassant, ' ');
	}

	public int continueMove(String notation, char promote) {
		// There's no way to perform an en passant and get promoted in the same turn.
		// This is safe.
		String[] turn = splitNotation(notation);
		return updateBoardState(translatePos(turn[1]), notation, false, promote);
	}
	
	private int updateBoardState(int end, String notation, boolean enPassant, char promotion) {
		boolean checked;
		boolean stalemated;
		
		if (promotion != ' ') {
			Piece hold = board[end];
			switch (promotion) {
			case 'Q':
				board[end] = new Queen(hold.getColor());
				break;
			case 'R':
				board[end] = new Rook(hold.getColor());
				((Rook) board[end]).revokeCastlingRights();
				break;
			case 'B':
				board[end] = new Bishop(hold.getColor());
				break;
			case 'N':
				board[end] = new Knight(hold.getColor());
				break;
			}
			hold = null;
		}
		
		// update sideToMove
		sideToMove = (sideToMove) ? false : true;
		
		// update the external Board representation.
		updateFlatBoard();
		
		
		if (board[end].getPieceClass() == 'K') {
			// unset Castling rights
			((King) board[end]).revokeCastlingRights();
			if (board[end].getColor()) {
				kingPosWhite = end;
			} else {
				kingPosBlack = end;
			}
		} else if (board[end].getPieceClass() == 'R') {
			// unset Castling rights
			((Rook) board[end]).revokeCastlingRights();
		} else if (board[end].getPieceClass() == 'P') {
			halfMove = 0;
		} else {
			halfMove++;
			if (halfMove == 100) {
				// TODO: uh, we're not tracking captures. So yeah, add that.
				// it's been 50 turns since there's been a capture or pawn move.
				// 50 turn rule evoked.
				return 13;
			}
		}

		// store notation
		previousMoves.add(notation);
		if (sideToMove == WHITE) {
			turnNum++;
		}
		
		// check for check/stalemate conditions
		int kingPos = (board[end].getColor()) ? kingPosBlack : kingPosWhite;
		//System.out.println("board: check for opponent check");
		checked = inCheck(kingPos);
		//System.out.println("board: check for opponent stalemate");
		stalemated = inStalemate(kingPos, board[kingPos].getColor());
		
		//System.out.println(createFen());
		fenStates.add(createFen());
		
		
		if (checked && !stalemated) {
			return CHECK;					// -1
		} else if (checked && stalemated) {
			return CHECKMATE; 				// -2
		} else if (!checked && stalemated) {
			return STALEMATE; 				// -3
		}

		return NORMAL;
	}
	
	public static String[] splitNotation(String movePair) {
		return movePair.split(" ");
	}
}
