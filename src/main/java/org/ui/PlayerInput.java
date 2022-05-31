package org.ui;

import org.main.Board;
import org.main.Move;
import org.main.Moveable;

public class PlayerInput implements Moveable{
	@Override
	public Move getMove(Board board) {
		return null;
	}

	public void setLastClickLocation(int x, int y) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
