package org.main;

import org.main.pieces.*;

import java.util.LinkedList;
import java.util.List;

public class Board {
	Piece[][] board = new Piece[8][8];

	public Board() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = Piece.None;
			}
			board[i][1] = Piece.WhitePawn;
			board[i][6] = Piece.BlackPawn;
		}
		board[0][0] = Piece.WhiteRook;
		board[7][0] = Piece.WhiteRook;
		board[0][7] = Piece.BlackRook;
		board[7][7] = Piece.BlackRook;

		board[1][0] = Piece.WhiteKnight;
		board[6][0] = Piece.WhiteKnight;
		board[1][7] = Piece.BlackKnight;
		board[6][7] = Piece.BlackKnight;

		board[2][0] = Piece.WhiteBishop;
		board[5][0] = Piece.WhiteBishop;
		board[2][7] = Piece.BlackBishop;
		board[5][7] = Piece.BlackBishop;

		board[3][0] = Piece.WhiteQueen;
		board[4][0] = Piece.WhiteKing;
		board[3][7] = Piece.BlackQueen;
		board[4][7] = Piece.BlackKing;
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

	LinkedList<Move> getAllMoves(){
		LinkedList<Move> moves = new LinkedList<Move>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				moves.addAll(getMoves(new Position(i,j)));
			}
		}
		return moves;
	}


	public Board(String fen) {
		throw new UnsupportedOperationException("Board::Board(String fen) is not yet implemented");
	}

	boolean isValid(Move move){
		return getAllMoves().contains(move);
	}

	public Board(Board oldBoard, Move move) {
		throw new UnsupportedOperationException("Board::Board(Board oldBoard, Move move) is not yet implemented");
	}

	String getFen() {
		throw new UnsupportedOperationException("Board::getFen is not yet implemented");
	}
}
