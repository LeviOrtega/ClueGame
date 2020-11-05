/*
 * Room objects hold information about individual rooms given from the setup files
 */

package clueGame;

import java.util.HashSet;
import java.util.Set;

public class Room {
	private String name;
	private String roomType;
	private char secretRoom;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private Set<BoardCell> doorList;
	private Card roomCard;
	
	public Room(String name) {
		this.name = name;
		doorList = new HashSet<BoardCell>();
	}
	
	public Card getRoomCard() {
		return this.roomCard;
	}
	
	public void setRoomCard(Card roomCard) {
		this.roomCard = roomCard;
	}
	
	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String cardType) {
		this.roomType = cardType;
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
