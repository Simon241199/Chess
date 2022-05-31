package org.main;

public record Position(int fileIndex, int rankIndex) {
	Position multiply(int n) {
		return new Position(fileIndex * n, rankIndex * n);
	}

	Position add(Position other) {
		return new Position(fileIndex + other.fileIndex, rankIndex + other.rankIndex);
	}

	boolean isOnBoard() {
		return fileIndex >= 0 && fileIndex <= 7 && rankIndex >= 0 && rankIndex <= 7;
	}
}
