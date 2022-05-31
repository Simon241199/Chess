package org.ui;

import org.main.Board;
import org.main.IBoardDisplay;
import org.main.Piece;
import org.main.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardDisplay extends JPanel implements IBoardDisplay, MouseListener {
	Font font = new Font("SansSerif", Font.BOLD, 97);
	JLabel[][] field = new JLabel[8][8];

	PlayerInput player;

	public BoardDisplay(PlayerInput player) {

		this.player = player;

		GridLayout layout = new GridLayout(8, 8);
		this.setLayout(layout);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				field[j][i] = new JLabel();
				field[j][i].setHorizontalAlignment(JLabel.CENTER);
				field[j][i].setVerticalAlignment(JLabel.CENTER);
				field[j][i].setFont(font);
				field[j][i].setOpaque(true);
				field[j][i].setText("Hallo");
				field[j][i].setForeground(Color.black);
				this.add(field[j][i]);
				if (player != null)
					field[j][i].addMouseListener(this);
			}
		}
	}

	@Override
	public void display(Board board, boolean flipped) {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}

		for (int fi = 0; fi < 8; fi++) {
			for (int ri = 0; ri < 8; ri++) {
				int di = fi;
				int dj = 7-ri;
				if(flipped){
					di = 7-di;
					dj = 7-dj;
				}
				field[di][dj].setBackground((ri + fi) % 2 == 0 ? Color.lightGray : Color.WHITE);
				Piece piece = board.getPiece(new Position(fi, ri));
				field[di][dj].setForeground(piece.isWhite() ? Color.darkGray : Color.black);
				field[di][dj].setText(piece.getCharacter() + "");
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (field[x][y].equals(e.getComponent())) {
					player.setLastClickLocation(x, y);
					return;
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
