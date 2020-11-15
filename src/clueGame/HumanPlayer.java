package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import gui.ClueGame;

public class HumanPlayer extends Player {



	public HumanPlayer(String name, int row, int column, PlayerType playerType, Color color) {
		super(name, row, column, playerType, color);

	}

	// the human player must select the targets in order for game to move on
	@Override
	public void selectTarget(int roll) {
		// set the finished turn to false before anything to make sure player can't move on until 
		// target is selected
		this.finishedTurn = false;
		BoardCell playerCell = Board.getInstance().getCell(this.row, this.column);
		Board.getInstance().calcTargets(playerCell, roll);

	}

	@Override
	public Suggestion createSuggestion() {
		BoardCell playerCell = Board.getInstance().getCell(this.row, this.column);
		// if the player isn't in a roomCenter, they can't make a suggestion
		if (!(playerCell.isRoomCenter())) {
			return null;
		}

		// the dialogue box will return a solution from the drop down menus 
		return ClueGame.getInstance().displaySuggestionPrompt(this);

	}

}
