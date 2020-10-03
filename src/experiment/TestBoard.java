package experiment;

import java.util.Set;

public class TestBoard {
	private int rowNum;
	private int columnNum;
	private TestBoardCell[][] board;
	private Set<TestBoardCell> targets;
	
	public TestBoard(int rowNum, int columnNum) {
		this.rowNum = rowNum;
		this.columnNum = columnNum;
		
		board = new TestBoardCell[rowNum][columnNum];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new TestBoardCell(i, j);
			}
		}
	}
	
	public TestBoardCell getCell(int row, int col) {
		return board[row][col];
	}
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		
		
	}
	
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
	
	
	
}
