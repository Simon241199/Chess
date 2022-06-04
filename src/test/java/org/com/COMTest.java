package org.com;

import org.junit.jupiter.api.Test;
import org.main.Board;
import org.main.Move;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class COMTest {

	@Test
	void testGetMoveDeterministic() {
		Board board = new Board("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR w KQkq - 0 1");
		COM com = new COM();
		com.maxDepth = 1;
		Move firstMove;
		com.getMove(board);
		firstMove = com.getMove(board);
		for (int i = 0; i < 100; i++) {
			assertThat(com.getMove(board)).isEqualTo(firstMove);
		}
	}
}