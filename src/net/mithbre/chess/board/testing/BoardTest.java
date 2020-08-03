package net.mithbre.chess.board.testing;
import net.mithbre.chess.board.model.*;

public class BoardTest {
	//private static final boolean WHITE = true;
	//private static final boolean BLACK = false;
	static int legal = 0;
	
	public static void main(String[] args) {
		testTranslatePos();
		//display();
		//testRook();
		longTest();
		Board boardtest = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		display(boardtest);
		
	}
	
	/*
	private static void testRook() {
		Rook whiteRook = new Rook(WHITE);
		Rook blackRook = new Rook(BLACK);
	}
	*/
	
	public static void display(Board board) {
		char[] flatBoard = board.getFlatBoard();
		int rank = 8;
		System.out.printf("\n\n  abcdefgh\n");
		System.out.printf("%d ", rank--);
		for (int i = 0; i < flatBoard.length; i++) {
			System.out.printf("%s", flatBoard[i]);
			if ((i+1) % 8 == 0 && i != flatBoard.length - 1) {
				System.out.printf("\n%d ", rank--);
			}
		}
	}
	
	
	private static void longTest() {
		String[] moves = {"e2 e4", // white: pawn move, legal
				  "e4 e5", // white: wrong side to move
				  "e7 e6", // black: pawn move, legal
				  "e4 e5", // white: pawn move, legal
				  "f8 d6", // black: bishop move, legal
				  "f1 f3", // white: move bishop illegally
				  "e5 d6", // white: pawn capture
				  "e6 d5", // black: illegal pawn "capture"
				  "e6 e4", // black: illegal pawn double push
				  "e8 e7", // black: illegal king move, attempts to check the king with a pawn // this breaks
				  "b8 c6", // black: queenside knight move
				  "g2 g4", // white: pawn double push
				  "c6 b4", // black: knight move
				  "g4 g5", // white: pawn move
				  "b4 c2", // black: knight move, checks white king
				  "g5 f6", // white: attempt to move a piece leaving the king in check
				  
				  "g5 g6", // white: attempt to move pawn when king is in check TODO: this does not work as intended!
				  "e1 e2", // white: move king out of check (no more castling from white)
				  "g8 h6", // black: move kingside knight
				  "g5 f6", // white: attempt en passant on an empty square.
				  "d1 c2", // white: capture knight.
				  "f7 f5", // black: pawn move, setup for en passant from white
				  "g5 f6", // white: en passant
				  "d8 f6", // black: capture pawn
				  "e2 e1", // white: move king back to start position
				  "b7 b6", // black: move pawn
				  "f1 c4", // white: move kingside bishop
				  "c8 a6", // black: move queenside bishop
				  "g1 f3", // white: move kingside knight
				  "b6 b5", // black: move pawn
				  "e1 g1", // white: attempt kingside castle
				  "d6 d7", // white: attempt capture by pawn
				  "d6 c7", // white: capture by pawn, queenside castle not available for black
				  "e8 c8", // black: attempt queenside castle.
				  "b5 b4", // black: move pawn
				  "c7 c8", // white: pawn promotion
				  "e8 c8", // black: attempt queenside castle.
				  "a8 c8", // black: rook captures queen. Queenside castle unavailable.
				  
				  "c8 a8", // black: move rook back
				  
				  "e8 c8" // black: attempt queenside castle.
				  };
		Board boardtest = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		for (String move: moves) {
			System.out.println(move);
			legal = boardtest.move(move);
			System.out.printf("move type: %s\n", legal);
			display(boardtest);
			System.out.printf("\n--------------------------------\n");
		}
	}
	
	private static void testTranslatePos() {
		Board boardtest = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		String[] testSetString = {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
								  "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
								  "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
								  "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
								  "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
								  "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
								  "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
								  "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"};
		int[] testSetInt = {21, 22, 23, 24, 25, 26, 27, 28,
							31, 32, 33, 34, 35, 36, 37, 38,
							41, 42, 43, 44, 45, 46, 47, 48,
							51, 52, 53, 54, 55, 56, 57, 58,
							61, 62, 63, 64, 65, 66, 67, 68,
							71, 72, 73, 74, 75, 76, 77, 78,
							81, 82, 83, 84, 85, 86, 87, 88,
							91, 92, 93, 94, 95, 96, 97, 98};
		
		System.out.println("Test Board.translatePos(String)");
		for (int i = 0; i < testSetString.length; i++) {
			if (boardtest.translatePos(testSetString[i]) == testSetInt[i]) {
				System.out.printf("PASS: %s %d\n", testSetString[i], testSetInt[i]);
			} else {
				System.out.printf("FAIL: %s %d %d\n", testSetString[i], testSetInt[i], boardtest.translatePos(testSetString[i]));
			}
		}
		
		System.out.println("\nTest Board.translatePos(int)");
		for (int i = 0; i < testSetInt.length; i++) {
			if (testSetString[i].equals(boardtest.translatePos(testSetInt[i]))) {
				System.out.printf("PASS: %d %s\n", testSetInt[i], testSetString[i]);
			} else {
				System.out.printf("FAIL: %d %s %s\n", testSetInt[i], testSetString[i], boardtest.translatePos(testSetInt[i]));
			}
		}
	}
}
