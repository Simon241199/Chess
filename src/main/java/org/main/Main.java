package org.main;

import org.com.COM;
import org.ui.ChessWindow;
import org.ui.PlayerInput;

import java.util.Collections;
import java.util.LinkedList;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		PlayerInput playerInput = new PlayerInput();
		ChessWindow window = new ChessWindow(playerInput);
		Board board = new Board(Board.STARTING_POSITION);

		window.getIBoardDisplay().display(board, false);

		Movable[] contestants = new Movable[]{playerInput, new COM()};

		for (int i = 0; board.getAllMoves().size() != 0; i++) {
			window.getIBoardDisplay().display(board, false);
			Move move = contestants[i%2].getMove(board);
			board = board.move(move);
		}
		window.getIBoardDisplay().display(board, false);
		Thread.sleep(5000);
		window.close();
	}
}