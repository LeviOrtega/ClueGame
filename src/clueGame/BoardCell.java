package clueGame;

import java.util.HashSet;
import java.util.Set;

import clueGame.BoardCell;

public class BoardCell {
	private int row;
	private int column;
	private boolean occupied;
	private boolean inRoom;
	private Set<BoardCell> adjList;
	
	public BoardCell(int row, int column) { // Constructor
		this.row = row;
		this.column = column;
		this.adjList = new HashSet<BoardCell>();
	}
	
	public Set<BoardCell> getAdjList(){
		return adjList;
	}
	
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
