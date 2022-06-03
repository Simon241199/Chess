package org.ui;

import org.main.Board;
import org.main.Move;
import org.main.Movable;
import org.main.Position;

import java.util.concurrent.Semaphore;

public class PlayerInput implements Movable {
	Position from = new Position(-1,-1);
	boolean isFromSet = false;
	Position to = new Position(-1,-1);

	Semaphore sem = new Semaphore(0);
	@Override
	public Move getMove(Board board){
		int x = 0;
		int y = 0;

		Move move = new Move(from,to);
		sem.drainPermits();
		try {
			do {
				isFromSet = false;
				sem.acquire();
				move = new Move(from,to);
			} while (!board.isValid(move));
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
		return move;
	}
	public void set(int file, int rank) {
		if(!isFromSet){
			from = new Position(file,rank);
			isFromSet = true;
			return;
		}
		to = new Position(file,rank);
		sem.release();;
	}
}
