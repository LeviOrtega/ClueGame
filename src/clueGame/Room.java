package clueGame;

import java.util.HashSet;
import java.util.Set;

public class Room {
	private String name;
	private String cardType;
	private char secretRoom;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private Set<BoardCell> doorList;
	
	public Room(String name) {
		this.name = name;
		doorList = new HashSet<BoardCell>();
	}
	
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public String getName() {
		return this.name;
	}
	
	public char getSecretRoom() {
		return this.secretRoom;
	}
	
	public BoardCell getCenterCell() {
		return this.centerCell;
	}

	public BoardCell getLabelCell() {
		// TODO Auto-generated method stub
		return this.labelCell;
	}
	
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}
	
	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
	
	public void setSecretRoom(char c) {
		this.secretRoom = c;
	}
	
	public void addDoor(BoardCell doorCell) {
		this.doorList.add(doorCell);
	}

	
	public Set<BoardCell> getDoorList(){
		return this.doorList;
	}
}
