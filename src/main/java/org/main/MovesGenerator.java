package org.main;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class MovesGenerator {

	final static List<Position> allDirections = List.of(
			new Position(-1, 1), new Position(0, 1), new Position(1, 1),
			new Position(-1, 0),/*new Position(      0,         0),*/ new Position(1, 0),
			new Position(-1, -1), new Position(0, -1), new Position(1, -1));

	final static List<Position> straightDirections = List.of(new Position(0, 1), new Position(-1, 0), new Position(1, 0), new Position(0, -1));

	final static List<Position> diagonalDirections = List.of(new Position(-1, 1), new Position(1, 1), new Position(-1, -1), new Position(1, -1));

	final static List<Position> knightDirections = List.of(
			new Position(-1, 2), new Position(1, 2),
			new Position(-2, 1), new Position(2, 1),
			new Position(-2, -1), new Position(2, -1),
			new Position(-1, -2), new Position(1, -2));

	public static LinkedList<Move> getKingMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board, position, moves, allDirections, 1);

		boolean kingSideAllowed = board.isWhitesTurn() ? board.KC : board.kC;
		boolean queenSideAllowed = board.isWhitesTurn() ? board.QC : board.qC;

		if (kingSideAllowed) {
			Position kingPassingSquare = position.add(new Position(1, 0));
			Position toSquare = position.add(new Position(2, 0));
			if (board.getPiece(kingPassingSquare).isNone() && board.getPiece(toSquare).isNone()) {
				if (!board.isAttacked(position, !board.isWhitesTurn())
						&& !board.isAttacked(kingPassingSquare, !board.isWhitesTurn())
						&& !board.isAttacked(toSquare, !board.isWhitesTurn())) {
					moves.add(new Move(position, toSquare));
				}
			}
		}
		if (queenSideAllowed) {
			Position kingPassingSquare = position.add(new Position(-1, 0));
			Position toSquare = position.add(new Position(-2, 0));
			Position rookPassingSquare = position.add(new Position(-3, 0));
			if (board.getPiece(kingPassingSquare).isNone() && board.getPiece(toSquare).isNone() && board.getPiece(rookPassingSquare).isNone()) {
				if (!board.isAttacked(position, !board.isWhitesTurn())
						&& !board.isAttacked(kingPassingSquare, !board.isWhitesTurn())
						&& !board.isAttacked(toSquare, !board.isWhitesTurn())) {
					moves.add(new Move(position, toSquare));
				}
			}
		}

		return moves;
	}

	public static LinkedList<Move> getQueenMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board, position, moves, allDirections, 10);
		return moves;
	}

	public static LinkedList<Move> getRookMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board, position, moves, straightDirections, 10);
		return moves;
	}

	public static LinkedList<Move> getBishopMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board, position, moves, diagonalDirections, 10);
		return moves;
	}

	public static LinkedList<Move> getKnightMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();
		addDirectionMoves(board, position, moves, knightDirections, 1);
		return moves;
	}

	public static LinkedList<Move> getPawnMoves(Board board, Position position) {
		LinkedList<Move> moves = new LinkedList<>();

		int dir = (board.isWhitesTurn() ? 1 : -1);
		int pawnBaseRank = (board.isWhitesTurn() ? 1 : 6);
		Piece movingPiece = (board.isWhitesTurn() ? Piece.WhitePawn : Piece.BlackPawn);
		int entPassentRank = (board.isWhitesTurn() ? 4 : 3);

		Position currentPos = position.add(new Position(0, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isNone()) {
				Move temp = new Move(position, currentPos);
				if (!isCheckAfter(board, temp)) {
					moves.add(temp);
				}

				currentPos = position.add(new Position(0, 2 * dir));
				if (position.rankIndex() == pawnBaseRank) {
					if (board.getPiece(currentPos).isNone()) {
						temp = new Move(position, currentPos);
						if (!isCheckAfter(board, temp)) {
							moves.add(temp);
						}
					}
				}
			}
		}
		currentPos = position.add(new Position(-1, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isOpponentOf(movingPiece)) {
				Move temp = new Move(position, currentPos);
				if (!isCheckAfter(board, temp)) {
					moves.add(temp);
				}
			}
		}
		currentPos = position.add(new Position(1, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isOpponentOf(movingPiece)) {
				Move temp = new Move(position, currentPos);
				if (!isCheckAfter(board, temp)) {
					moves.add(temp);
				}
			}
		}


		if (abs(board.entpassentFile - 'a' - position.fileIndex()) == 1 && position.rankIndex() == entPassentRank) {
			Move temp = new Move(position, new Position(board.entpassentFile - 'a', entPassentRank + dir));
			if (!isCheckAfter(board, temp)) {
				moves.add(temp);
			}
		}

		return moves;
	}

	private static void addDirectionMoves(Board board, Position position, LinkedList<Move> moves, List<Position> directions, int maximumNumberOfTimes) {
		for (Position dir : directions) {
			Position currentPos = position.add(dir);
			for (int d = 1; d <= maximumNumberOfTimes && currentPos.isOnBoard(); currentPos = currentPos.add(dir), d++) {
				Piece currentPiece = board.getPiece(currentPos);
				if (currentPiece.isNone()) {
					Move temp = new Move(position, currentPos);
					if (!isCheckAfter(board, temp)) {
						moves.add(temp);
					}
					continue;
				}
				if (currentPiece.isColor(!board.isWhitesTurn())) {
					Move temp = new Move(position, currentPos);
					if (!isCheckAfter(board, temp)) {
						moves.add(temp);
					}
				}
				break;
			}
		}
	}

	private static void addDirectionCaptures(Board board, Position position, LinkedList<Move> captures, List<Position> directions, int maximumNumberOfTimes) {
		for (Position dir : directions) {
			Position currentPos = position.add(dir);
			for (int d = 1; d <= maximumNumberOfTimes && currentPos.isOnBoard(); currentPos = currentPos.add(dir), d++) {
				Piece currentPiece = board.getPiece(currentPos);
				if (currentPiece.isNone()) {
					continue;
				}
				if (currentPiece.isColor(!board.isWhitesTurn()) && currentPiece.isValuable()) {
					Move temp = new Move(position, currentPos);
					if (!isCheckAfter(board, temp)) {
						captures.add(temp);
					}
				}
				break;
			}
		}
	}

	private static boolean isCheckAfter(Board board, Move move) {
		return board.move(move).isCheck(board.isWhitesTurn());
	}

	public static LinkedList<Move> getKingCaptures(Board board, Position position) {
		LinkedList<Move> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, allDirections, 1);
		return captures;
	}

	public static LinkedList<Move> getQueenCaptures(Board board, Position position) {
		LinkedList<Move> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, allDirections, 10);
		return captures;
	}

	public static LinkedList<Move> getRookCaptures(Board board, Position position) {
		LinkedList<Move> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, straightDirections, 10);
		return captures;
	}

	public static LinkedList<Move> getBishopCaptures(Board board, Position position) {
		LinkedList<Move> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, diagonalDirections, 10);
		return captures;
	}

	public static LinkedList<Move> getKnightCaptures(Board board, Position position) {
		LinkedList<Move> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, knightDirections, 1);
		return captures;
	}

	public static LinkedList<Move> getPawnCaptures(Board board, Position position) {
		LinkedList<Move> captures = new LinkedList<>();

		int dir = (board.isWhitesTurn() ? 1 : -1);
		Piece movingPiece = (board.isWhitesTurn() ? Piece.WhitePawn : Piece.BlackPawn);
		int entPassentRank = (board.isWhitesTurn() ? 4 : 3);

		Position currentPos = position.add(new Position(-1, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isOpponentOf(movingPiece)) {
				Move temp = new Move(position, currentPos);
				if (!isCheckAfter(board, temp)) {
					captures.add(temp);
				}
			}
		}
		currentPos = position.add(new Position(1, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isOpponentOf(movingPiece)) {
				Move temp = new Move(position, currentPos);
				if (!isCheckAfter(board, temp)) {
					captures.add(temp);
				}
			}
		}


		if (abs(board.entpassentFile - 'a' - position.fileIndex()) == 1 && position.rankIndex() == entPassentRank) {
			Move temp = new Move(position, new Position(board.entpassentFile - 'a', entPassentRank + dir));
			if (!isCheckAfter(board, temp)) {
				captures.add(temp);
			}
		}

		return captures;
	}
}
