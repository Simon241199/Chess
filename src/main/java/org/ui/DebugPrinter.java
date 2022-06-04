package org.ui;

import org.main.Board;
import org.main.Position;

public class DebugPrinter {
	public static void print(Board board){
		System.out.println("-------------------");
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(board.getPiece(new Position(j, 7 - i)).getCharacter()+" ");
			}
			System.out.println();
		}
		System.out.println("-------------------");
	}
}
