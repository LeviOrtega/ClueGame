package clueGame;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, int row, int column, PlayerType playerType) {
		super(name, row, column);
		this.playerType = playerType;
		// TODO Auto-generated constructor stub
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

}
