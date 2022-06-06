package org.com;

import org.main.Board;
import org.main.Move;
import org.main.Movable;
import org.main.Position;

import java.util.ArrayList;
import java.util.LinkedList;


public class COM implements Movable {

	ISharedTranspositionTable transpositionTable = new SharedTranspositionTable();
	public int maxDepth = 3;

	@Override
	public Move getMove(Board board) {
		transpositionTable.clear();

		LinkedList<Move> allMoves = board.getAllMoves();
		Move bestMove;
		int depth = 0;
		do{

			bestMove = new Move(new Position(0, 0), new Position(0, 0));
			int bestScore = -Evaluator.mate;

			ArrayList<Evaluator> evaluators = new ArrayList<>(allMoves.size());

			for (Move move : allMoves) {
				Evaluator evaluator = new Evaluator(board.move(move), depth, transpositionTable);
				evaluator.start();
				evaluators.add(evaluator);
			}
			try {
				for (int i = 0; i < evaluators.size(); i++) {
					Evaluator evaluator = evaluators.get(i);
					evaluator.join();
					int score = -evaluator.getScore();
					if (score > bestScore) {
						bestScore = score;
						bestMove = allMoves.get(i);
					}
				}
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
				System.err.println("InterruptedException in COM");
				throw new RuntimeException(e);
			}
			depth++;
		}while(depth<maxDepth);

		return bestMove;
	}
}
