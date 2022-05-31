package org.main.pieces;

import org.main.Board;
import org.main.Move;
import org.main.Position;

import java.util.List;

public class Knight {
	final static List<Position> directions = List.of(
			new Position(-1, 2), new Position(1, 2),
			new Position(-2, 1), new Position(2, 1),
			new Position(-2, -1), new Position(2, -1),
			new Position(-1, -2), new Position(1, -2));

	public static List<Move> getMoves(Board board, Position position) {
		return null;
	}
}
