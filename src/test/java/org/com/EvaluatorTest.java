package org.com;

import org.junit.jupiter.api.Test;

import org.assertj.core.api.*;
import org.main.Board;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EvaluatorTest {

	@Test
	void testRunDeterministic() {
		Board board = new Board("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR w KQkq - 0 1");
		int firstScore;
		{
			Evaluator evaluate = new Evaluator(board, 1, new SharedTranspositionTable());
			evaluate.run();
			firstScore = evaluate.getScore();
		}
		for (int i = 0; i < 100; i++) {
			Evaluator evaluate = new Evaluator(board, 1, new SharedTranspositionTable());
			evaluate.run();
			assertThat(evaluate.getScore()).isEqualTo(firstScore);
		}
	}
}