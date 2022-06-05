package org.com;

import org.main.Board;
import org.main.Move;
import org.main.Position;

import java.util.LinkedList;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Evaluator extends Thread {
	final static int mate = Integer.MAX_VALUE;
	final static int maxScoreWithoutForcedMate = 1_000_000;
	private int score;
	private int maxDepth;
	private Board board;
	private ISharedTranspositionTable transpositionTable;

	public Evaluator(Board board, int maxDepth, ISharedTranspositionTable transpositionTable) {
		this.board = board;
		this.maxDepth = maxDepth;
		this.transpositionTable = transpositionTable;
	}


	@Override
	public void run() {
		score = evaluate(board, 0, -mate, mate);
	}

	private int evaluate(Board board, int depth, int lower, int upper) {
		int remainingDepth = maxDepth - depth;
		int transScore = transpositionTable.getOrNMate(board, remainingDepth);
		if (depth >= maxDepth) {
			if (transScore == -mate) {
				return evaluateCaptures(board, depth, lower, upper);
			}
			return transScore;
		}
		lower = max(transScore, lower);

		LinkedList<Move> allMoves = board.getAllMoves();

		if(allMoves.size()==0){
			if(board.isCheck(board.isWhitesTurn())){
				return -mate + depth;
			}
			return 0;
		}

		boolean updatedLower = false;
		for (Move move : allMoves) {
			int score = -evaluate(board.move(move), depth + 1, -upper, -lower);
			if (lower <= score) {
				updatedLower = true;
				lower = score;
			}

			if (lower >= upper) {
				break;
			}
		}
		if (updatedLower) {
			transpositionTable.add(board, lower, remainingDepth);
		}
		return lower;
	}

	private int evaluateCaptures(Board board, int depth, int lower, int upper) {
		int remainingDepth = maxDepth - depth;
		int transScore = transpositionTable.getOrNMate(board, remainingDepth);

		lower = max(transScore, lower);

		lower = max(elementaryEvaluation(board), lower);

		LinkedList<Move> allCaptures = board.getAllCaptures();

		for (Move move : allCaptures) {
			int score = -evaluateCaptures(board.move(move), depth + 1, -upper, -lower);
			if (lower <= score) {
				lower = score;
			}

			if (lower >= upper) {
				break;
			}
		}
		return lower;
	}

	private int elementaryEvaluation(Board board) {
		int materialBalance = 0;
		int totalMaterial = 0;
		Position position = new Position(0, 0);
		while (position.isOnBoard()) {
			int val = board.getPiece(position).getValue();
			totalMaterial += abs(val);
			materialBalance += board.isWhitesTurn() ? val : -val;
			position = position.nextInBoardIteration();
		}
		totalMaterial = max(totalMaterial, 1);
		float materialRatio = materialBalance / (float) totalMaterial;


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

		return (int) ((moveRatio + 11 * materialRatio) * maxScoreWithoutForcedMate / 12.0);
	}

	public int getScore() {
		return score;
	}
}
