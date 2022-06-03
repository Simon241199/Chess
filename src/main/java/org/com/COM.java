package org.com;

import org.main.Board;
import org.main.Move;
import org.main.Movable;

import java.util.Collections;
import java.util.LinkedList;

public class COM implements Movable {
	@Override
	public Move getMove(Board board) {
		LinkedList<Move> allMoves = board.getAllMoves();
		Collections.shuffle(allMoves);

		return allMoves.getFirst();
	}
}
