package experiment;

import java.util.Set;
import java.util.HashSet;

public class TestBoardCell {
	private int row;
	private int column;
	private boolean occupied;
	private boolean inRoom;
	private Set<TestBoardCell> adjList;
	
	public TestBoardCell(int row, int column) {
		this.row = row;
		this.column = column;
		this.adjList = new HashSet<TestBoardCell>();
	}
	
	public Set<TestBoardCell> getAdjList(){
		return adjList;
	}
	
	public void setRoom(boolean partOfRoom) {
		
	}
	
	public boolean isRoom() {
		return false;
	}
	
	
	public void setOccupied(boolean occupied) {
		
	}
	
	public boolean isOccupied() {
		return false;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
}
