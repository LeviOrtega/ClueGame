package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private static int rowNum;
	private static int columnNum;
	private TestBoardCell[][] board;
	private Set<TestBoardCell> targets;
	
	public TestBoard(int rowNum, int columnNum) {
		this.rowNum = rowNum;
		this.columnNum = columnNum;
		
		this.targets = new HashSet<TestBoardCell>();
		
		board = new TestBoardCell[rowNum][columnNum];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new TestBoardCell(i, j);
				//System.out.println(board[i][j].getRow() + " " + board[i][j].getColumn());
			}
		}
	}
	
	public TestBoardCell getCell(int row, int col) {
		return board[row][col];
	}
	
	public static int getRowLen() {
		return rowNum;
	}
	
	public static int getColLen() {
		return columnNum;
	}
	
	
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		
		
	}
	
	
	
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
}
