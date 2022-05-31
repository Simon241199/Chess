package org.main;

import org.main.pieces.*;

import java.util.*;

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
			for (int f = 0; f < 8; f++) {
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

	public Piece getPiece(Position position) {
		return board[position.fileIndex()][position.rankIndex()];
	}

	LinkedList<Move> getMoves(Position position) {
		return switch (board[position.fileIndex()][position.rankIndex()]) {
			case WhiteKing, BlackKing -> King.getMoves(this, position);
			case WhiteQueen, BlackQueen -> Queen.getMoves(this, position);
			case WhiteRook, BlackRook -> Rook.getMoves(this, position);
			case WhiteBishop, BlackBishop -> Bishop.getMoves(this, position);
			case WhiteKnight, BlackKnight -> Knight.getMoves(this, position);
			case WhitePawn, BlackPawn -> Pawn.getMoves(this, position);
			default -> new LinkedList<Move>();
		};
	}

	LinkedList<Move> getAllMoves() {
		LinkedList<Move> moves = new LinkedList<Move>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				moves.addAll(getMoves(new Position(i, j)));
			}
		}
		return moves;
	}

	boolean isValid(Move move) {
		return getAllMoves().contains(move);
	}

	public Board(Board oldBoard, Move move) {
		throw new UnsupportedOperationException("Board::Board(Board oldBoard, Move move) is not yet implemented");
	}

	String getFen() {
		throw new UnsupportedOperationException("Board::getFen is not yet implemented");
	}
}
