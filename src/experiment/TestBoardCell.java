package experiment;

import java.util.Set;
import java.util.TreeSet;

public class TestBoardCell {
	
	private int row;
	private int column;
	private Set<TestBoardCell> adjList;
	
	private TestBoardCell(int row, int column) {
		this.row = row;
		this.column = column;
		this.adjList = new TreeSet<TestBoardCell>();
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
	
}
