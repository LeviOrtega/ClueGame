package clueGame;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, int row, int column, PlayerType playerType) {
		super(name, row, column);
		this.playerType = playerType;
		
	}
	
	@Override
	public void selectTarget(int hardCodedRoll) {
		
	}

	@Override
	public PlayerType getPlayerType() {
		return this.playerType;
	}

	@Override
	public Card disproveSuggestion(Suggestion suggestion) {
		
		return new Card();
	}

}
