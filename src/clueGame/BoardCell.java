package clueGame;

import java.util.HashSet;
import java.util.Set;

import clueGame.BoardCell;

public class BoardCell {
	private int row;
	private int column;
<<<<<<< HEAD
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
=======
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
	private boolean occupied;
	private boolean inRoom;
	private Set<BoardCell> adjList;
	
<<<<<<< HEAD
	public BoardCell(int row, int column, char initial) { // Constructor
		this.row = row;
		this.column = column;
		this.initial = initial;
=======
	public BoardCell(int row, int column) { // Constructor
		this.row = row;
		this.column = column;
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
		this.adjList = new HashSet<BoardCell>();
	}
	
	public Set<BoardCell> getAdjList(){
		return adjList;
	}
	
<<<<<<< HEAD
	public boolean isDoorway() {
		return false;
	}
	
	public DoorDirection getDoorDirection() {
		return this.doorDirection;
	}
	
	
=======
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
	public void addToAdjList(BoardCell bc) {
		if (bc.isOccupied() == false && bc.isRoom() == false) {
			// TestBoardCell object added to adjacency list iff spot is not occupied or a marked room
			adjList.add(bc);
		}
		else {
			// TestBoardCell object is otherwise removed (not applicable in adjacency list)
			this.adjList.remove(bc);
		}
	}
	
	public void setRoom(boolean partOfRoom) {
		this.inRoom = partOfRoom;
	}
	
	public boolean isRoom() {
		return this.inRoom;
	}
	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	public boolean isOccupied() {
		return this.occupied;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
}
