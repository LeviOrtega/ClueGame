package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import clueGame.Board;

public class NextListener implements ActionListener{
	private ClueGame clueGame = ClueGame.getInstance();

	public NextListener() {};

	@Override
	public void actionPerformed(ActionEvent e) {
		clueGame.iteratePlayerIndex();
	}

}
