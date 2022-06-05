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

	public boolean isOpponentOf(Piece other) {
		return this.isWhite() && other.isBlack() || this.isBlack() && other.isWhite();
	}

	public boolean isColor(boolean white) {
		return this.isWhite() && white || this.isBlack() && !white;
	}

	public boolean walksDiagonal() { return this.isQueen() || this.isBishop(); }
	public boolean walksStraight() { return this.isQueen() || this.isRook(); }

	public boolean isNone() {
		return this.equals(Piece.None);
	}
	public boolean isPawn() {
		return this.equals(Piece.WhitePawn) || this.equals(Piece.BlackPawn);
	}
	public boolean isKnight() {
		return this.equals(Piece.WhiteKnight) || this.equals(Piece.BlackKnight);
	}
	public boolean isBishop() {
		return this.equals(Piece.WhiteBishop) || this.equals(Piece.BlackBishop);
	}
	public boolean isRook() {
		return this.equals(Piece.WhiteRook) || this.equals(Piece.BlackRook);
	}
	public boolean isQueen() {
		return this.equals(Piece.WhiteQueen) || this.equals(Piece.BlackQueen);
	}
	public boolean isKing() {
		return this.equals(Piece.WhiteKing) || this.equals(Piece.BlackKing);
	}

	public Piece getWithOppositeColor() {
		return switch (this){
			case None -> None;
			case WhiteKing -> BlackKing;
			case WhiteQueen -> BlackQueen;
			case WhiteRook -> BlackRook;
			case WhiteBishop -> BlackBishop;
			case WhiteKnight -> BlackKnight;
			case WhitePawn -> BlackPawn;
			case BlackKing -> WhiteKing;
			case BlackQueen -> WhiteQueen;
			case BlackRook -> WhiteRook;
			case BlackBishop -> WhiteBishop;
			case BlackKnight -> WhiteKnight;
			case BlackPawn -> WhitePawn;
		};
	}
	public Piece getPieceWithColor(boolean white) {
		if(this.isWhite() == white){
			return this;
		}
		return getWithOppositeColor();
	}

	public boolean isValuable(){
		return !(this.isPawn() || this.isNone());
	}

	public char toFenChar() {
		return switch (this) {
			case WhiteKing -> 'K';
			case WhiteQueen -> 'Q';
			case WhiteRook -> 'R';
			case WhiteBishop -> 'B';
			case WhiteKnight -> 'N';
			case WhitePawn -> 'P';
			case BlackKing -> 'k';
			case BlackQueen -> 'q';
			case BlackRook -> 'r';
			case BlackBishop -> 'b';
			case BlackKnight -> 'n';
			case BlackPawn -> 'p';
			default -> ' ';
		};
	}

	public static Piece fenCharToPiece(char c) {
		return switch (c) {
			case 'K' -> WhiteKing;
			case 'Q' -> WhiteQueen;
			case 'R' -> WhiteRook;
			case 'B' -> WhiteBishop;
			case 'N' -> WhiteKnight;
			case 'P' -> WhitePawn;
			case 'k' -> BlackKing;
			case 'q' -> BlackQueen;
			case 'r' -> BlackRook;
			case 'b' -> BlackBishop;
			case 'n' -> BlackKnight;
			case 'p' -> BlackPawn;
			default -> None;
		};
	}

	public int getValue() {
		return switch (this) {
			case WhiteQueen -> 9;
			case WhiteRook -> 5;
			case WhiteBishop -> 3;
			case WhiteKnight -> 3;
			case WhitePawn -> 1;
			case BlackQueen -> -9;
			case BlackRook -> -5;
			case BlackBishop -> -3;
			case BlackKnight -> -3;
			case BlackPawn -> -1;
			default -> 0;
		};
	}
}
