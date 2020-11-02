package clueGame;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, int row, int column) {
		super(name, row, column);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PlayerType getPlayerType() {
		return this.playerType;
	}

}
