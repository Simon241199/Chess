package org.com;

import org.main.Board;
import org.ui.DebugPrinter;

import java.util.HashMap;

import static org.com.Evaluator.mate;

record ScoreDepth(Integer score, int remainingDepth) {
}

public class SharedTranspositionTable extends HashMap<Long, ScoreDepth> implements ISharedTranspositionTable {

	@Override
	public synchronized void add(Board board, int score, int remainingEvalDepth) {
		ScoreDepth sd = super.getOrDefault(board.hash(), new ScoreDepth(0, -1));
		if (sd.remainingDepth() <= remainingEvalDepth) {
			super.put(board.hash(), new ScoreDepth(score, remainingEvalDepth));
		}
	}

	@Override
	public int getOrDefault(Board board, int minEvalDepth, int defaultVal) {
		ScoreDepth scoreDepth = super.getOrDefault(board.hash(), new ScoreDepth(null, Integer.MIN_VALUE));
		if (scoreDepth.remainingDepth() >= minEvalDepth) {
			/*System.out.println("get " + scoreDepth.score() + ", " + scoreDepth.remainingDepth() + ", " + board.hash());
			DebugPrinter.print(board);*/
			return scoreDepth.score();
		}
		return defaultVal;
	}

	@Override
	public synchronized int getOrNMate(Board board, int minEvalDepth) {
		return getOrDefault(board, minEvalDepth, -mate);
	}
}
