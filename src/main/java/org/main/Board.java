package org.main;

import java.util.*;

import static java.lang.Math.abs;

public class Board {
	public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	Piece[][] board = new Piece[8][8];
	boolean isWhitesTurn;
	boolean KC = true;
	boolean QC = true;
	boolean kC = true;
	boolean qC = true;
	char entpassentFile = '-';
	int lastCommitment = 0;
	int moveNumber = 0;

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
			for (int f = 0; f < rows[r].length(); f++) {
				char c = rows[r].charAt(f);
				Piece piece = Piece.fenCharToPiece(c);
				if (piece == Piece.None) {
					f += Integer.parseInt(c + "") - 1;
				} else {
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

		if (from.equals(new Position(7, 0)) || from.equals(new Position(3, 0))) {
			this.KC = false;
		}
		if (from.equals(new Position(0, 0)) || from.equals(new Position(3, 0))) {
			this.QC = false;
		}
		if (from.equals(new Position(7, 7)) || from.equals(new Position(3, 7))) {
			this.kC = false;
		}
		if (from.equals(new Position(0, 7)) || from.equals(new Position(3, 7))) {
			this.qC = false;
		}

		if (movedPiece.isPawn() && abs(from.rankIndex() - to.rankIndex()) == 2) {
			entpassentFile = (char) ('a' + from.fileIndex());
		}

		if (!targetedPiece.isNone() || movedPiece.isPawn()) {
			lastCommitment = 0;
		}

		// TODO Entpassent and Castling and Pawn-Promotion

		this.setPiece(to, this.getPiece(from));
		this.setPiece(from, Piece.None);
	}

	public Piece getPiece(Position position) {
		return board[position.fileIndex()][position.rankIndex()];
	}

	public void setPiece(Position position, Piece piece) {
		board[position.fileIndex()][position.rankIndex()] = piece;
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

	LinkedList<Move> getAllMoves() {
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

	Board move(Move move) {
		return new Board(this, move);
	}

	boolean isValid(Move move) {
		return getAllMoves().contains(move);
	}

	boolean isCheck(boolean white) {
		Position kingPos = new Position(0,0);
		Piece searchingFor = white ? Piece.WhiteKing:Piece.BlackKing;
		while(!getPiece(kingPos).equals(searchingFor)){
			kingPos = kingPos.nextInBoardIteration();
			if(!kingPos.isOnBoard()){
				return false;
			}
		}

		for(Position dir : MovesGenerator.straightDirections){
			Position currentPos = kingPos.add(dir);

		}

		return false;
	}

	String getFen() {
		throw new UnsupportedOperationException("Board::getFen is not yet implemented");
	}
}
