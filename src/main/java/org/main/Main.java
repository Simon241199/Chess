package org.main;

import org.ui.ChessWindow;

import java.util.Collections;
import java.util.LinkedList;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		ChessWindow window = new ChessWindow();
		Board board = new Board(Board.STARTING_POSITION);

		window.getIBoardDisplay().display(board, false);

		Thread.sleep(5000);
		for (int i = 0; i < 50; i++) {

			window.getIBoardDisplay().display(board, false);

			Thread.sleep(1000);

			LinkedList<Move> allMoves = board.getAllMoves();
			Collections.shuffle(allMoves);

			board = board.move(allMoves.getFirst());
		}
	}
}