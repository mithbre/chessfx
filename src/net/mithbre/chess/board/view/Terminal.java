package net.mithbre.chess.board.view;

import net.mithbre.chess.board.model.Board;

public class Terminal {
	private static Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	private static boolean mate = false;
	private static String userMove = "";
	
	public static void main(String[] args) {
		while (!mate) {
			//System.out.printf("%s\n\n", board.getCurrentFen());
			display();
			//userMove = validateInputMove();
			board.move(userMove);
			//mate = board.isMate();
		}
	}
	
	public static String[] splitInput(String movePair) {
		return movePair.split(" ");
	}

	public static void display() {
		char[] flatBoard = board.getFlatBoard();
		int rank = 8;
		System.out.printf("  abcdefgh\n");
		System.out.printf("%d ", rank--);
		for (int i = 0; i < flatBoard.length; i++) {
			System.out.printf("%s", flatBoard[i]);
			if ((i+1) % 8 == 0 && i != flatBoard.length - 1) {
				System.out.printf("\n%d ", rank--);
			}
		}
	}
	
	public static String validateInputMove() {
		// take in user input
		// expected setup is 'e2 e4'
		System.out.printf("\n\nType your move in the format e2 e4: ");
		
		// use a regex to check if it's in the valid format
		// lowercase all letters.
		// ^[a-h][1-8] [a-h][1-8]$
		// that is close, but it matches more than I want.
		// a1 b2	good
		// e2 e4	good
		// e4 e45   bad
		return "e2 e4";
	}
}
