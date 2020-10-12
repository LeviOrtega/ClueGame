package clueGame;

public class Room {
	private String name;
	private String cardType;
	private char secretRoom;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	public Room(String name) {
		this.name = name;
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
}
