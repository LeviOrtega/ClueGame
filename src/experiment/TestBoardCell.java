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
		calcAdjList();
	}
	
	public void calcAdjList() {
		// up down left right
		/*
		if (this.row - 1 >= 0) {
			adjList.add(TestBoard.getCell(this.row - 1, this.column));
		}
		if ((this.row + 1) <= TestBoard.getRowLen() - 1) {
			adjList.add(TestBoard.getCell(this.row + 1, this.column));
		}
		if (this.column - 1 >= 0) {
			adjList.add(TestBoard.getCell(this.row, this.column-1));
		}
		if ((this.column + 1) <= TestBoard.getColLen() - 1) {
			adjList.add(TestBoard.getCell(this.row, column + 1));
			
		}
		*/
		
	}
	

	
	public Set<TestBoardCell> getAdjList(){
		return adjList;
	}
	
	public void addToAdjList(TestBoardCell tbc) {
		adjList.add(tbc);
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
