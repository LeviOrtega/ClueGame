package clueGame;

import java.awt.Color;

public class HumanPlayer extends Player {


	
	public HumanPlayer(String name, int row, int column, PlayerType playerType, Color color) {
		super(name, row, column, playerType, color);

	}

	

	@Override
	public Card disproveSuggestion(Suggestion suggestion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void selectTarget(int roll) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Suggestion createSuggestion() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
