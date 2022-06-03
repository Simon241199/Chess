package org.ui;

import org.main.IBoardDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class ChessWindow extends JFrame {
	BoardDisplay boardDisplay;

	public ChessWindow(PlayerInput playerInput){
		boardDisplay = new BoardDisplay(playerInput);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setBounds(100,100,9*100,8*100+39);
		boardDisplay.setBounds(0,0,800,800);
		this.add(boardDisplay);
		this.setBackground(Color.red);
		this.setVisible(true);
	}

	public IBoardDisplay getIBoardDisplay(){
		return boardDisplay;
	}

	public void close(){
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
