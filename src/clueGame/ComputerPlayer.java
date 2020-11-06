package clueGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, int row, int column, PlayerType playerType) {
		super(name, row, column);
		this.playerType = playerType;

	}



	@Override
	public void selectTarget(int roll) {
		BoardCell playerCell = Board.getInstance().getCell(this.row, this.column);
		Board.getInstance().calcTargets(playerCell, roll);
		Set<BoardCell> targets = Board.getInstance().getTargets();
		ArrayList<BoardCell> rooms = new ArrayList<BoardCell>();
		ArrayList<BoardCell> spaces = new ArrayList<BoardCell>();
		for (BoardCell target: targets) {
			if (target.isRoomCenter()) {
				rooms.add(target);
			}
			else spaces.add(target);
		}
		BoardCell newLocation;
		if (rooms.size() != 0) {
			Collections.shuffle(rooms);
			newLocation = rooms.get(0);
			this.updatePosition(newLocation.getRow(), newLocation.getColumn());
		}
		else if (spaces.size() != 0){
			Collections.shuffle(spaces);
			newLocation = spaces.get(0);
			this.updatePosition(newLocation.getRow(), newLocation.getColumn());
		}


	}

	@Override
	public PlayerType getPlayerType() {
		return this.playerType;
	}

	@Override
	public Card disproveSuggestion(Suggestion suggestion) {

		return new Card();
	}



	@Override
	public Suggestion createSuggestion() {
		return new Suggestion(new Card(), new Card(), new Card());
	}

}
