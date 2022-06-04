package org.com;

import org.main.Board;

public interface ISharedTranspositionTable {
	void add(Board board, int score, int remainingEvalDepth);

	int getOrNMate(Board board, int minEvalDepth);
	void clear();
}
