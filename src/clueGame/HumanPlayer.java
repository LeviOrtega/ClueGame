package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import gui.ClueGame;
import gui.SuggestionPrompt;

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

	// called every move
	@Override
	public Suggestion createSuggestion() {
		BoardCell playerCell = Board.getInstance().getCell(this.row, this.column);
		// if the player isn't in a roomCenter, they can't make a suggestion
		if (!(playerCell.isRoomCenter())) {
			// if player isn't in a room, then their turn is done
			finishedTurn = true;
			return null;
		}

		// the dialogue box will return a solution from the drop down menus 
		SuggestionPrompt input = new SuggestionPrompt();
		input.displaySuggestionPrompt(this);

		// suggestion will be handled in suggestionprompt and once answer is submitted it will be sent to handleSuggestion
		return null;
	}

	@Override
	public void checkIfPlayerShouldHandleSuggestion() {
		// only check if currentPlayer is this player
		// we do this because players can be pulled into rooms
		if (Board.getInstance().getCurrentPlayer() == this && finishedTurn == false) {
			// handle suggestion is called in event listeners
			// createSuggestion returns null if player isn't in room
			suggestion = createSuggestion();
		}
	}

}
