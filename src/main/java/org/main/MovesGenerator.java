package org.main;

import java.util.LinkedList;
import java.util.List;

public class MovesGenerator {

	private final static List<Position> allDirections = List.of(
			new Position(-1, 1), new Position(0, 1), new Position(1, 1),
			new Position(-1, 0),/*new Position(      0,         0),*/ new Position(1, 0),
			new Position(-1, -1), new Position(0, -1), new Position(1, -1));

	private final static List<Position> straightDirections = List.of(new Position(0, 1), new Position(-1, 0), new Position(1, 0), new Position(0, -1));

	private final static List<Position> diagonalDirections = List.of(new Position(-1, 1), new Position(1, 1), new Position(-1, -1), new Position(1, -1));

	private final static List<Position> knightDirections = List.of(
			new Position(-1, 2), new Position(1, 2),
			new Position(-2, 1), new Position(2, 1),
			new Position(-2, -1), new Position(2, -1),
			new Position(-1, -2), new Position(1, -2));

	public static LinkedList<Move> getKingMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board,position,moves,allDirections,1);
		// TODO Castling
		return moves;
	}

	public static LinkedList<Move> getQueenMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board,position,moves,allDirections,10);
		return moves;
	}

	public static LinkedList<Move> getRookMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board,position,moves,straightDirections,10);
		return moves;
	}

	public static LinkedList<Move> getBishopMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board,position,moves,diagonalDirections,10);
		return moves;
	}

	public static LinkedList<Move> getKnightMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board,position,moves,knightDirections,1);
		return moves;
	}
	public static LinkedList<Move> getPawnMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		// TODO
		return moves;
	}

	private static void addDirectionMoves(Board board, Position position, LinkedList<Move> moves, List<Position> directions, int maximumNumberOfTimes) {
		Piece movingPiece = board.getPiece(position);
		if(movingPiece.isNone())
			throw new UnsupportedOperationException("addDirectionMoves an Position muss das Piece stehen, da sonst dessen Farbe unbekannt ist.");

		for (Position dir : directions) {
			Position currentPos = position.add(dir);
			for (int d = 1; d <= maximumNumberOfTimes && currentPos.isOnBoard(); currentPos = currentPos.add(dir), d++) {
				if(board.getPiece(currentPos).isNone()){
					moves.add(new Move(position,currentPos));
					continue;
				}
				if(movingPiece.isOpponentOf(board.getPiece(currentPos))){
					moves.add(new Move(position,currentPos));
				}
				break;
			}
		}
	}
}
