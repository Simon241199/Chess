package org.com;

import org.main.Board;

public interface ISharedTranspositionTable {
	void add(Board board, int score, int remainingEvalDepth);

	int getOrDefault(Board board, int minEvalDepth, int defaultVal);

	int getOrNMate(Board board, int minEvalDepth);
	void clear();
}
