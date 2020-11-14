package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import clueGame.Board;

public class NextListener implements ActionListener{
	private ClueGame clueGame = ClueGame.getInstance();

	public NextListener() {};

	@Override
	public void actionPerformed(ActionEvent e) {

		if (clueGame.checkIfCanMoveOn()) {
			int index = clueGame.getCurrentPlayerIndex() + 1;
			// bound the index by the size of players
			index  %= Board.getInstance().getPlayers().size();
			clueGame.setCurrentPlayerIndex(index);
			clueGame.handlePlayerLogic();
		}
		// else throw error
	}

}
