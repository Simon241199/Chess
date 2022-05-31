package org.main;

public enum Piece {
	None,

	WhiteKing,
	WhiteQueen,
	WhiteRook,
	WhiteBishop,
	WhiteKnight,
	WhitePawn,

	BlackKing,
	BlackQueen,
	BlackRook,
	BlackBishop,
	BlackKnight,
	BlackPawn;

	public char getCharacter() {
		return switch (this) {
			case WhiteKing -> '\u2654';
			case WhiteQueen -> '\u2655';
			case WhiteRook -> '\u2656';
			case WhiteBishop -> '\u2657';
			case WhiteKnight -> '\u2658';
			case WhitePawn -> '\u2659';
			case BlackKing -> '\u265A';
			case BlackQueen -> '\u265B';
			case BlackRook -> '\u265C';
			case BlackBishop -> '\u265D';
			case BlackKnight -> '\u265E';
			case BlackPawn -> '\u265F';
			default -> ' ';
		};
	}

	public boolean isWhite() {
		return switch (this) {
			case WhiteKing, WhiteQueen, WhiteRook, WhiteBishop, WhiteKnight, WhitePawn -> true;
			default -> false;
		};
	}

	public boolean isBlack() {
		return switch (this) {
			case BlackKing, BlackQueen, BlackRook, BlackBishop, BlackKnight, BlackPawn -> true;
			default -> false;
		};
	}
}
