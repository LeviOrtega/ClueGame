package clueGame;

public class HumanPlayer extends Player {

	public HumanPlayer(String name, int row, int column, PlayerType playerType) {
		super(name, row, column);
		this.playerType = playerType;
	}

	@Override
	public PlayerType getPlayerType() {
		return this.playerType;
	}

	@Override
	public Card disproveSuggestion(Suggestion suggestion) {
		// TODO Auto-generated method stub
		return new Card();
	}

	@Override
	public void selectTarget(int hardCodedRoll) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Suggestion createSuggestion() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
