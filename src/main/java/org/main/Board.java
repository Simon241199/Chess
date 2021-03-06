package org.main;

import java.util.*;

import static java.lang.Math.abs;

public class Board {
	public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	Piece[][] board = new Piece[8][8];

	public boolean isWhitesTurn() {
		return isWhitesTurn;
	}

	private boolean isWhitesTurn;
	boolean KC = true;
	boolean QC = true;
	boolean kC = true;
	boolean qC = true;
	char entpassentFile = '-';
	int lastCommitment = 0;
	int moveNumber = 0;
	Piece promotion = Piece.WhiteQueen;

	public Board(String fen) {
		for (int r = 0; r < 8; r++) {
			for (int f = 0; f < 8; f++) {
				board[f][r] = Piece.None;
			}
		}

		String[] parts = fen.split(" ");
		if (parts.length != 6) {
			throw new IllegalArgumentException("The FEN-String \"" + fen + "\" is not supported. parts.length != 6");
		}
		String[] rows = parts[0].split("/");
		if (rows.length != 8) {
			throw new IllegalArgumentException("The FEN-String \"" + fen + "\" is not supported. rows.length != 8");
		}
		Collections.reverse(Arrays.asList(rows));

		for (int r = 0; r < 8; r++) {
			rows[r] = rows[r].replaceAll("1", "_");
			rows[r] = rows[r].replaceAll("2", "__");
			rows[r] = rows[r].replaceAll("3", "___");
			rows[r] = rows[r].replaceAll("4", "____");
			rows[r] = rows[r].replaceAll("5", "_____");
			rows[r] = rows[r].replaceAll("6", "______");
			rows[r] = rows[r].replaceAll("7", "_______");
			rows[r] = rows[r].replaceAll("8", "________");
			for (int f = 0; f < rows[r].length(); f++) {
				char c = rows[r].charAt(f);
				Piece piece = Piece.fenCharToPiece(c);
				if (!piece.isNone()) {
					board[f][r] = piece;
				}
			}
		}
		isWhitesTurn = parts[1].equals("w");

		KC = parts[2].contains("K");
		QC = parts[2].contains("Q");
		kC = parts[2].contains("k");
		qC = parts[2].contains("q");

		entpassentFile = parts[3].charAt(0);

		lastCommitment = Integer.parseInt(parts[4]);

		moveNumber = Integer.parseInt(parts[5]);
	}

	private Board(Board oldBoard, Move move) {
		Position from = move.from();
		Position to = move.to();
		Piece movedPiece = oldBoard.getPiece(from);
		Piece targetedPiece = oldBoard.getPiece(to);
		for (int r = 0; r < 8; r++) {
			for (int f = 0; f < 8; f++) {
				this.board[f][r] = oldBoard.board[f][r];
			}
		}
		this.isWhitesTurn = !oldBoard.isWhitesTurn;
		this.KC = oldBoard.KC;
		this.QC = oldBoard.QC;
		this.kC = oldBoard.kC;
		this.qC = oldBoard.qC;
		this.entpassentFile = '-';
		this.lastCommitment = oldBoard.lastCommitment + 1;
		this.moveNumber = oldBoard.moveNumber + (isWhitesTurn ? 1 : 0);

		if (from.equals(new Position(7, 0)) || to.equals(new Position(7, 0)) || from.equals(new Position(4, 0))) {
			this.KC = false;
		}
		if (from.equals(new Position(0, 0)) || to.equals(new Position(0, 0)) || from.equals(new Position(4, 0))) {
			this.QC = false;
		}
		if (from.equals(new Position(7, 7)) || to.equals(new Position(7, 7)) || from.equals(new Position(4, 7))) {
			this.kC = false;
		}
		if (from.equals(new Position(0, 7)) || to.equals(new Position(0, 7)) || from.equals(new Position(4, 7))) {
			this.qC = false;
		}

		if (movedPiece.isPawn() && abs(from.rankIndex() - to.rankIndex()) == 2) {
			entpassentFile = (char) ('a' + from.fileIndex());
		}

		if (!targetedPiece.isNone() || movedPiece.isPawn()) {
			lastCommitment = 0;
		}

		if (targetedPiece.isNone() && movedPiece.isPawn() && to.fileIndex() != from.fileIndex()) {
			this.setPiece(new Position(to.fileIndex(), from.rankIndex()), Piece.None);
		}

		int castlingRank = movedPiece.isWhite() ? 0 : 7;
		if (movedPiece.isKing() && abs(from.fileIndex() - to.fileIndex()) == 2) {
			int rookToFile = (from.fileIndex() + to.fileIndex()) / 2;
			int rookFromFile = (int) Math.floor(to.fileIndex() / (float) from.fileIndex()) * 7;
			this.setPiece(new Position(rookToFile, castlingRank), this.getPiece(new Position(rookFromFile, castlingRank)));
			this.setPiece(new Position(rookFromFile, castlingRank), Piece.None);
		}

		this.setPiece(to, this.getPiece(from));
		this.setPiece(from, Piece.None);

		int pawnPromotionRank = movedPiece.isWhite() ? 7 : 0;
		if (movedPiece.isPawn() && to.rankIndex() == pawnPromotionRank) {
			Piece promotion = this.promotion.getPieceWithColor(movedPiece.isWhite());
			this.setPiece(to, promotion);
		}
	}

	public Piece getPiece(Position position) {
		return board[position.fileIndex()][position.rankIndex()];
	}

	public Piece getPieceOrNone(Position position) {
		if (position.isOnBoard())
			return board[position.fileIndex()][position.rankIndex()];
		return Piece.None;
	}

	public void setPiece(Position position, Piece piece) {
		board[position.fileIndex()][position.rankIndex()] = piece;
	}

	LinkedList<Board> getBoards(Position position) {
		return switch (board[position.fileIndex()][position.rankIndex()]) {
			case WhiteKing, BlackKing -> BoardsGenerator.getKingBoards(this, position);
			case WhiteQueen, BlackQueen -> BoardsGenerator.getQueenBoards(this, position);
			case WhiteRook, BlackRook -> BoardsGenerator.getRookBoards(this, position);
			case WhiteBishop, BlackBishop -> BoardsGenerator.getBishopBoards(this, position);
			case WhiteKnight, BlackKnight -> BoardsGenerator.getKnightBoards(this, position);
			case WhitePawn, BlackPawn -> BoardsGenerator.getPawnBoards(this, position);
			default -> new LinkedList<Board>();
		};
	}
	LinkedList<Move> getMoves(Position position) {
		return switch (board[position.fileIndex()][position.rankIndex()]) {
			case WhiteKing, BlackKing -> MovesGenerator.getKingMoves(this, position);
			case WhiteQueen, BlackQueen -> MovesGenerator.getQueenMoves(this, position);
			case WhiteRook, BlackRook -> MovesGenerator.getRookMoves(this, position);
			case WhiteBishop, BlackBishop -> MovesGenerator.getBishopMoves(this, position);
			case WhiteKnight, BlackKnight -> MovesGenerator.getKnightMoves(this, position);
			case WhitePawn, BlackPawn -> MovesGenerator.getPawnMoves(this, position);
			default -> new LinkedList<Move>();
		};
	}
	LinkedList<Move> getCaptures(Position position) {
		return switch (board[position.fileIndex()][position.rankIndex()]) {
			case WhiteKing, BlackKing -> MovesGenerator.getKingCaptures(this, position);
			case WhiteQueen, BlackQueen -> MovesGenerator.getQueenCaptures(this, position);
			case WhiteRook, BlackRook -> MovesGenerator.getRookCaptures(this, position);
			case WhiteBishop, BlackBishop -> MovesGenerator.getBishopCaptures(this, position);
			case WhiteKnight, BlackKnight -> MovesGenerator.getKnightCaptures(this, position);
			case WhitePawn, BlackPawn -> MovesGenerator.getPawnCaptures(this, position);
			default -> new LinkedList<Move>();
		};
	}
	public LinkedList<Board> getAllBoards() {
		LinkedList<Board> boards = new LinkedList<Board>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Position position = new Position(i, j);
				if (getPiece(position).isWhite() && isWhitesTurn || getPiece(position).isBlack() && !isWhitesTurn)
					boards.addAll(getBoards(new Position(i, j)));
			}
		}
		return boards;
	}

	public LinkedList<Move> getAllMoves() {
		LinkedList<Move> moves = new LinkedList<Move>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Position position = new Position(i, j);
				if (getPiece(position).isWhite() && isWhitesTurn || getPiece(position).isBlack() && !isWhitesTurn)
					moves.addAll(getMoves(new Position(i, j)));
			}
		}
		return moves;
	}
	public LinkedList<Move> getAllCaptures() {
		LinkedList<Move> captures = new LinkedList<Move>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Position position = new Position(i, j);
				if (getPiece(position).isWhite() && isWhitesTurn || getPiece(position).isBlack() && !isWhitesTurn)
					captures.addAll(getCaptures(new Position(i, j)));
			}
		}
		return captures;
	}

	public Board move(Move move) {
		return new Board(this, move);
	}

	public boolean isValid(Move move) {
		return getAllMoves().contains(move);
	}

	public boolean isCheck(boolean white) {
		Position kingPos = new Position(0, 0);
		Piece king = white ? Piece.WhiteKing : Piece.BlackKing;
		while (!getPiece(kingPos).equals(king)) {
			kingPos = kingPos.nextInBoardIteration();
			if (!kingPos.isOnBoard()) {
				return false;
			}
		}
		return isAttacked(kingPos, !white);
	}

	boolean isAttacked(Position targetPos, boolean aggressorColor) {
		int aggressivePawnDir = aggressorColor ? 1 : -1;
		Position currentPos = targetPos.add(new Position(-1, -aggressivePawnDir));
		if (getPieceOrNone(currentPos).isColor(aggressorColor) && getPieceOrNone(currentPos).isPawn()) {
			return true;
		}
		currentPos = targetPos.add(new Position(1, -aggressivePawnDir));
		if (getPieceOrNone(currentPos).isColor(aggressorColor) && getPieceOrNone(currentPos).isPawn()) {
			return true;
		}


		for (Position dir : MovesGenerator.straightDirections) {
			currentPos = targetPos.add(dir);
			if (getPieceOrNone(currentPos).isKing() && getPieceOrNone(currentPos).isColor(aggressorColor)) {
				return true;
			}

			for (int d = 1; currentPos.isOnBoard(); currentPos = currentPos.add(dir), d++) {
				Piece piece = getPiece(currentPos);
				if (piece.walksStraight() && piece.isColor(aggressorColor)) {
					return true;
				}
				if (!piece.isNone()) {
					break;
				}
			}
		}
		for (Position dir : MovesGenerator.diagonalDirections) {
			currentPos = targetPos.add(dir);
			if (getPieceOrNone(currentPos).isKing() && getPieceOrNone(currentPos).isColor(aggressorColor)) {
				return true;
			}

			for (int d = 1; currentPos.isOnBoard(); currentPos = currentPos.add(dir), d++) {
				Piece piece = getPiece(currentPos);
				if (piece.walksDiagonal() && piece.isColor(aggressorColor)) {
					return true;
				}
				if (!piece.isNone()) {
					break;
				}
			}
		}
		for (Position dir : MovesGenerator.knightDirections) {
			Piece piece = getPieceOrNone(targetPos.add(dir));
			if (piece.isKnight() && piece.isColor(aggressorColor)) {
				return true;
			}
		}

		return false;
	}

	static long hashInts(int a, int b) {
		if(a*b==0){
			a = -198074;
			b = -403895;
		}
		long expr1 = 3482530L * a + 9294010L * b + a * b * 9782345L + (long)((a-b)/(double)(a+b)*1234.5);
		long expr2 = 9573812L * a + 7892662L * b + a * b * 6589340L + (long)((a-b)/(double)(a+b)*9876.5);
		long expr3 = 1938676L * a + 2348575L * b + a * b * 5894323L + (long)((a-b)/(double)(a+b)*1010.1);
		long expr4 = 3456782L * a + 7658943L * b + a * b * 1289045L + (long)((a-b)/(double)(a+b)*1729.3);
		return expr1 >> 48 ^ expr2 >> 32 ^ expr3 >> 16 ^ expr4;
	}

	public long hash() {
		long hash = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				hash ^= hashInts(board[i][j].hashCode(), i + 8 * j);
			}
		}
		hash ^= hashInts(Boolean.hashCode(this.isWhitesTurn), 65);
		hash ^= hashInts(Boolean.hashCode(this.KC), 66);
		hash ^= hashInts(Boolean.hashCode(this.kC), 67);
		hash ^= hashInts(Boolean.hashCode(this.QC), 68);
		hash ^= hashInts(Boolean.hashCode(this.qC), 69);
		hash ^= hashInts(Character.hashCode(this.entpassentFile), 70);
		return hash;
	}

	String getFen() {
		throw new UnsupportedOperationException("Board::getFen is not yet implemented");
	}
}
