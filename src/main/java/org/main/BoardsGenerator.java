package org.main;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class BoardsGenerator {

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

	public static LinkedList<Board> getKingBoards(Board board, Position position) {
		LinkedList<Board> boards = new LinkedList<>();
		addDirectionBoards(board, position, boards, allDirections, 1);

		boolean kingSideAllowed = board.isWhitesTurn() ? board.KC : board.kC;
		boolean queenSideAllowed = board.isWhitesTurn() ? board.QC : board.qC;

		if (kingSideAllowed) {
			Position kingPassingSquare = position.add(new Position(1, 0));
			Position toSquare = position.add(new Position(2, 0));
			if (board.getPiece(kingPassingSquare).isNone() && board.getPiece(toSquare).isNone()) {
				if (!board.isAttacked(position, !board.isWhitesTurn())
						&& !board.isAttacked(kingPassingSquare, !board.isWhitesTurn())
						&& !board.isAttacked(toSquare, !board.isWhitesTurn())) {
					boards.add(board.move(new Move(position, toSquare)));
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
					boards.add(board.move(new Move(position, toSquare)));
				}
			}
		}

		return boards;
	}

	public static LinkedList<Board> getQueenBoards(Board board, Position position) {
		LinkedList<Board> boards = new LinkedList<>();
		addDirectionBoards(board, position, boards, allDirections, 10);
		return boards;
	}

	public static LinkedList<Board> getRookBoards(Board board, Position position) {
		LinkedList<Board> boards = new LinkedList<>();
		addDirectionBoards(board, position, boards, straightDirections, 10);
		return boards;
	}

	public static LinkedList<Board> getBishopBoards(Board board, Position position) {
		LinkedList<Board> boards = new LinkedList<>();
		addDirectionBoards(board, position, boards, diagonalDirections, 10);
		return boards;
	}

	public static LinkedList<Board> getKnightBoards(Board board, Position position) {
		LinkedList<Board> boards = new LinkedList<>();
		addDirectionBoards(board, position, boards, knightDirections, 1);
		return boards;
	}

	public static LinkedList<Board> getPawnBoards(Board board, Position position) {
		LinkedList<Board> boards = new LinkedList<>();

		int dir = (board.isWhitesTurn() ? 1 : -1);
		int pawnBaseRank = (board.isWhitesTurn() ? 1 : 6);
		Piece movingPiece = (board.isWhitesTurn() ? Piece.WhitePawn : Piece.BlackPawn);
		int entPassentRank = (board.isWhitesTurn() ? 4 : 3);

		Position currentPos = position.add(new Position(0, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isNone()) {
				Board temp = board.move(new Move(position, currentPos));
				if (!temp.isCheck(board.isWhitesTurn())) {
					boards.add(temp);
				}

				currentPos = position.add(new Position(0, 2 * dir));
				if (position.rankIndex() == pawnBaseRank) {
					if (board.getPiece(currentPos).isNone()) {
						temp = board.move(new Move(position, currentPos));
						if (!temp.isCheck(board.isWhitesTurn())) {
							boards.add(temp);
						}
					}
				}
			}
		}
		currentPos = position.add(new Position(-1, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isOpponentOf(movingPiece)) {
				Board temp = board.move(new Move(position, currentPos));
				if (!temp.isCheck(board.isWhitesTurn())) {
					boards.add(temp);
				}
			}
		}
		currentPos = position.add(new Position(1, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isOpponentOf(movingPiece)) {
				Board temp = board.move(new Move(position, currentPos));
				if (!temp.isCheck(board.isWhitesTurn())) {
					boards.add(temp);
				}
			}
		}


		if (abs(board.entpassentFile - 'a' - position.fileIndex()) == 1 && position.rankIndex() == entPassentRank) {
			Board temp = board.move(new Move(position, new Position(board.entpassentFile - 'a', entPassentRank + dir)));
			if (!temp.isCheck(board.isWhitesTurn())) {
				boards.add(temp);
			}
		}

		return boards;
	}

	private static void addDirectionBoards(Board board, Position position, LinkedList<Board> boards, List<Position> directions, int maximumNumberOfTimes) {
		for (Position dir : directions) {
			Position currentPos = position.add(dir);
			for (int d = 1; d <= maximumNumberOfTimes && currentPos.isOnBoard(); currentPos = currentPos.add(dir), d++) {
				Piece currentPiece = board.getPiece(currentPos);
				if (currentPiece.isNone()) {
					Board temp = board.move(new Move(position, currentPos));
					if (!temp.isCheck(board.isWhitesTurn())) {
						boards.add(temp);
					}
					continue;
				}
				if (currentPiece.isColor(!board.isWhitesTurn())) {
					Board temp = board.move(new Move(position, currentPos));
					if (!temp.isCheck(board.isWhitesTurn())) {
						boards.add(temp);
					}
				}
				break;
			}
		}
	}

	private static void addDirectionCaptures(Board board, Position position, LinkedList<Board> captures, List<Position> directions, int maximumNumberOfTimes) {
		for (Position dir : directions) {
			Position currentPos = position.add(dir);
			for (int d = 1; d <= maximumNumberOfTimes && currentPos.isOnBoard(); currentPos = currentPos.add(dir), d++) {
				Piece currentPiece = board.getPiece(currentPos);
				if (currentPiece.isNone()) {
					continue;
				}
				if (currentPiece.isColor(!board.isWhitesTurn()) && currentPiece.isValuable()) {
					Board temp = board.move(new Move(position, currentPos));
					if (!temp.isCheck(board.isWhitesTurn())) {
						captures.add(temp);
					}
				}
				break;
			}
		}
	}

	public static LinkedList<Board> getKingCaptures(Board board, Position position) {
		LinkedList<Board> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, allDirections, 1);
		return captures;
	}

	public static LinkedList<Board> getQueenCaptures(Board board, Position position) {
		LinkedList<Board> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, allDirections, 10);
		return captures;
	}

	public static LinkedList<Board> getRookCaptures(Board board, Position position) {
		LinkedList<Board> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, straightDirections, 10);
		return captures;
	}

	public static LinkedList<Board> getBishopCaptures(Board board, Position position) {
		LinkedList<Board> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, diagonalDirections, 10);
		return captures;
	}

	public static LinkedList<Board> getKnightCaptures(Board board, Position position) {
		LinkedList<Board> captures = new LinkedList<>();
		addDirectionCaptures(board, position, captures, knightDirections, 1);
		return captures;
	}

	public static LinkedList<Board> getPawnCaptures(Board board, Position position) {
		LinkedList<Board> captures = new LinkedList<>();

		int dir = (board.isWhitesTurn() ? 1 : -1);
		Piece movingPiece = (board.isWhitesTurn() ? Piece.WhitePawn : Piece.BlackPawn);
		int entPassentRank = (board.isWhitesTurn() ? 4 : 3);

		Position currentPos = position.add(new Position(-1, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isOpponentOf(movingPiece)) {
				Board temp = board.move(new Move(position, currentPos));
				if (!temp.isCheck(board.isWhitesTurn())) {
					captures.add(temp);
				}
			}
		}
		currentPos = position.add(new Position(1, dir));
		if (currentPos.isOnBoard()) {
			if (board.getPiece(currentPos).isOpponentOf(movingPiece)) {
				Board temp = board.move(new Move(position, currentPos));
				if (!temp.isCheck(board.isWhitesTurn())) {
					captures.add(temp);
				}
			}
		}


		if (abs(board.entpassentFile - 'a' - position.fileIndex()) == 1 && position.rankIndex() == entPassentRank) {
			Board temp = board.move(new Move(position, new Position(board.entpassentFile - 'a', entPassentRank + dir)));
			if (!temp.isCheck(board.isWhitesTurn())) {
				captures.add(temp);
			}
		}

		return captures;
	}
}
