package experiment;

import java.util.Set;
import java.util.HashSet;

public class TestBoardCell {
	private int row;
	private int column;
	private boolean occupied;
	private boolean inRoom;
	private Set<TestBoardCell> adjList;
	
	public TestBoardCell(int row, int column) { // Constructor
		this.row = row;
		this.column = column;
		this.adjList = new HashSet<TestBoardCell>();
	}
	
<<<<<<< HEAD
=======


	
>>>>>>> 73dc183a527dbf4b278fcb8d5dbdd7e1d6e1cad6
	public Set<TestBoardCell> getAdjList(){
		return adjList;
	}
	
	public void addToAdjList(TestBoardCell tbc) {
		if (tbc.isOccupied() == false && tbc.isRoom() == false) {
<<<<<<< HEAD
			// TestBoardCell object added to adjacency list iff spot is not occupied or a marked room
			adjList.add(tbc);
		}
		else {
			// TestBoardCell object is otherwise removed (not applicable in adjacency list)
=======
			adjList.add(tbc);
		}
		else {
>>>>>>> 73dc183a527dbf4b278fcb8d5dbdd7e1d6e1cad6
			this.adjList.remove(tbc);
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
