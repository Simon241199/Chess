package org.main.pieces;

import org.main.Board;
import org.main.Move;
import org.main.Position;

import java.util.LinkedList;
import java.util.List;

public class Rook {
	final static List<Position> directions = List.of(new Position(0, 1), new Position(-1, 0), new Position(1, 0), new Position(0, -1));

	public static LinkedList<Move> getMoves(Board board, Position position) {
		return null;
	}
}
