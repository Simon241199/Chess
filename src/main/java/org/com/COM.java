package org.com;

import org.main.Board;
import org.main.Move;
import org.main.Movable;
import org.main.Position;

import java.util.Collections;
import java.util.LinkedList;

import static java.lang.Math.max;

public class COM implements Movable {
	final static int mate = Integer.MAX_VALUE;

	int maxDepth = 3;

	@Override
	public Move getMove(Board board) {
		LinkedList<Move> allMoves = board.getAllMoves();

		Move bestMove = new Move(new Position(0, 0), new Position(0, 0));
		int bestScore = -mate;

		for (Move move : allMoves) {
			int score = -evaluate(board.move(move), 0,-bestScore,-mate);
			if (score > bestScore) {
				bestScore = score;
				bestMove = move;
			}
		}

		return bestMove;
	}

	public int evaluate(Board board, int depth, int upper, int lower) {
		if (depth >= maxDepth) {
			return elementaryEvaluation(board);
		}

		LinkedList<Move> allMoves = board.getAllMoves();

		for (Move move : allMoves) {
			lower = max(-evaluate(board.move(move), depth + 1,-lower,-upper), lower);
			if(lower>=upper){
				return lower;
			}
		}

		return lower;
	}

	int elementaryEvaluation(Board board) {
		int moveCount = board.getAllMoves().size();
		if (moveCount == 0) {
			if (board.isCheck(board.isWhitesTurn())) {
				return -mate;
			}
			return 0;
		}

		Position emptySquare = new Position(0, 0);
		while (!board.getPiece(emptySquare).isNone()) {
			emptySquare = emptySquare.nextInBoardIteration();
			if (!emptySquare.isOnBoard()) {
				emptySquare = new Position(0, 0);
				break;
			}
		}

		int replyMoveCount = board.move(new Move(emptySquare, emptySquare)).getAllMoves().size();


		float moveRatio = (moveCount - replyMoveCount) / (float) (replyMoveCount + moveCount);
		return (int) (moveRatio * 100);
	}
}
