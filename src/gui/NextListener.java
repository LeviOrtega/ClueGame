package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import clueGame.Board;

public class NextListener implements ActionListener{

	public NextListener() {};

	@Override
	public void actionPerformed(ActionEvent e) {
		ClueGame.getInstance().clearGuessAndResult();
		Board.getInstance().iteratePlayerIndex();
	}

}
