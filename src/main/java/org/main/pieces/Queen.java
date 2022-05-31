package org.main.pieces;

import org.main.Board;
import org.main.Move;
import org.main.Position;

import java.util.List;

public class Queen {
	final static List<Position> directions = List.of(
			new Position(-1, 1), new Position(0, 1), new Position(1, 1),
			new Position(-1, 0),/*new Position(      0,         0),*/ new Position(1, 0),
			new Position(-1, -1), new Position(0, -1), new Position(1, -1));

	public static List<Move> getMoves(Board board, Position position) {
		return null;
	}
}
