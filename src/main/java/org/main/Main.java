package org.main;

import org.ui.ChessWindow;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		ChessWindow window = new ChessWindow();
		Board board = new Board();
		window.getIBoardDisplay().display(board, false);
		System.out.println("\u2654");
	}
}