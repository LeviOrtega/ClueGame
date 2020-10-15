package clueGame;

import java.util.HashSet;
import java.util.Set;

import clueGame.BoardCell;

public class BoardCell {
	private int row;
	private int column;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private boolean occupied;
	private boolean inRoom;
	private boolean isDoorway;
	private boolean isPath;
	private boolean isUnused;
	private Set<BoardCell> adjList;
	
	public BoardCell(int row, int column, char initial) { // Constructor
		this.row = row;
		this.column = column;
		this.initial = initial;
		this.adjList = new HashSet<BoardCell>();
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public char getInitial() {
		return initial;
	}
	
	public DoorDirection getDoorDirection() {
		return this.doorDirection;
	}
	
	public void setDoorDirection(DoorDirection d) {
		this.doorDirection = d;
	}
	
	public boolean isLabel() {
		return this.roomLabel;
	}
	
	public void setRoomLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}
	
	public boolean isRoomCenter() {
		return this.roomCenter;
	}
	
	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}
	
	public char getSecretPassage() {
		return this.secretPassage;
	}
	
	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}
	
	public boolean isOccupied() {
		return this.occupied;
	}
	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	public boolean isRoom() {
		return this.inRoom;
	}
	
	public void setRoom(boolean partOfRoom) {
		this.inRoom = partOfRoom;
	}
	
	public boolean isDoorway() {
		return this.isDoorway;
	}
	
	public void setDoorway(boolean isDoorway) {
		this.isDoorway = isDoorway;
	}
	
	public boolean isPath() {
		return this.isPath;
	}
	
	public boolean isUnused() {
		return this.isUnused;
	}
	
	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}
	
	public void setUnused(boolean isUnused) {
		this.isUnused = isUnused;
	}
	
	public Set<BoardCell> getAdjList() {
		return adjList;
	}
	
	public void addAdj(BoardCell adjCell) {
		adjList.add(adjCell);
	}
	
	public void removeAdj(BoardCell adjCell) {
		if (adjList.contains(adjCell)) {
			adjList.remove(adjCell);
		}
	}
}
