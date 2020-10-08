package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private int rowNum;
	private int columnNum;
	private TestBoardCell[][] board;
	private Set<TestBoardCell> visited;
	private Set<TestBoardCell> targets;
	
	public TestBoard(int rowNum, int columnNum) {
		this.rowNum = rowNum;
		this.columnNum = columnNum;
		
		this.targets = new HashSet<TestBoardCell>();
		this.visited = new HashSet<TestBoardCell>();
		board = new TestBoardCell[rowNum][columnNum];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new TestBoardCell(i, j);
				//System.out.println(board[i][j].getRow() + " " + board[i][j].getColumn());
			}
		}
		
		generateBoardAdjList();
	}

	public void generateBoardAdjList() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				calcAdjList(getCell(i,j));
			}
		}
	}
	
	public TestBoardCell getCell(int row, int col) {
		return board[row][col];
	}
	

	
	public void calcAdjList(TestBoardCell tbc) {
		// up down left right
		
		if (tbc.getRow() - 1 >= 0) {
			tbc.addToAdjList(getCell(tbc.getRow() - 1, tbc.getColumn()));
		}
		if ((tbc.getRow() + 1) <= this.rowNum - 1) {
			tbc.addToAdjList(getCell(tbc.getRow() + 1, tbc.getColumn()));
		}
		if (tbc.getColumn() - 1 >= 0) {
			tbc.addToAdjList(getCell(tbc.getRow(), tbc.getColumn() - 1));
		}
		if ((tbc.getColumn() + 1) <= this.columnNum - 1) {
			tbc.addToAdjList(getCell(tbc.getRow(), tbc.getColumn() + 1));
		}
	}
	
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		visited.add(startCell);
		
		for (TestBoardCell tbc: startCell.getAdjList()) {
			if (!(visited.contains(tbc))) {
				visited.add(tbc);
				if (pathLength == 1) {
					targets.add(tbc);
				}
				else {
					calcTargets(tbc, pathLength -1);
				}
				visited.remove(tbc);
			}
		}
		visited.remove(startCell);
	}

	public void clearTargets() {
		targets.clear();
	}
	
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
}
