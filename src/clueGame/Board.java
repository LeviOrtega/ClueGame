package clueGame;

import java.util.HashSet;
<<<<<<< HEAD
import java.util.Map;
=======
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
import java.util.Set;

import clueGame.BoardCell;

public class Board {
	private static Board theInstance = new Board();
<<<<<<< HEAD
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private BoardCell[][] board;
	private Map<Character, Room> roomMap;
=======
	private int rowNum;
	private int columnNum;
	private BoardCell[][] board;
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	
	private Board() {}

	public static Board getInstance() {
		return theInstance;
	}

	public void setConfigFiles(String csv, String txt) {
		
	}
	
<<<<<<< HEAD
	public Room getRoom(Character c) {
		
		return null;
	}
	
	public void initialize() {
		/*
=======
	private void initialize() {
		
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
		this.targets = new HashSet<BoardCell>();
		this.visited = new HashSet<BoardCell>();
		board = new BoardCell[rowNum][columnNum];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new BoardCell(i, j);
			}
		}
<<<<<<< HEAD
		generateBoardAdjList();*/
=======
		generateBoardAdjList();
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
	}

	public void generateBoardAdjList() {
		// Iterates through each index of 2D array to create comprehensive adjacency list
		// Called in TestBoard() constructor
<<<<<<< HEAD
		/*for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				calcAdjList(getCell(i,j));
			}
		}*/
	}
	
	public BoardCell getCell(int row, int col) {
		return null;
		//return board[row][col];
=======
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				calcAdjList(getCell(i,j));
			}
		}
	}
	
	public BoardCell getCell(int row, int col) {
		return board[row][col];
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
	}
	
	public void calcAdjList(BoardCell bc) {
		// Up, down, left, right
<<<<<<< HEAD
		/*if (bc.getRow() - 1 >= 0) { 
=======
		if (bc.getRow() - 1 >= 0) { 
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
			bc.addToAdjList(getCell(bc.getRow() - 1, bc.getColumn()));
		}
		if ((bc.getRow() + 1) <= this.rowNum - 1) {
			bc.addToAdjList(getCell(bc.getRow() + 1, bc.getColumn()));
		}
		if (bc.getColumn() - 1 >= 0) {
			bc.addToAdjList(getCell(bc.getRow(), bc.getColumn() - 1));
		}
		if ((bc.getColumn() + 1) <= this.columnNum - 1) {
			bc.addToAdjList(getCell(bc.getRow(), bc.getColumn() + 1));
<<<<<<< HEAD
		}*/
	}
	
	public void calcTargets(BoardCell startCell, int pathLength) {
		/*visited.add(startCell);
=======
		}
	}
	
	public void calcTargets(BoardCell startCell, int pathLength) {
		visited.add(startCell);
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
		
		for (BoardCell tbc: startCell.getAdjList()) {
			if (!(visited.contains(tbc))) {
				visited.add(tbc);
				if (pathLength == 1) {
					targets.add(tbc);
				}
				else {
					// Recursive call to calcTargets() until path length reaches 1
					calcTargets(tbc, pathLength -1);
				}
				visited.remove(tbc);
			}
		}
<<<<<<< HEAD
		visited.remove(startCell);*/
	}

	public void clearTargets() {
		//targets.clear();
=======
		visited.remove(startCell);
	}

	public void clearTargets() {
		targets.clear();
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
	}
	
	public Set<BoardCell> getTargets(){
		return targets;
	}
<<<<<<< HEAD

	public int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 0;
	}
=======
>>>>>>> 351eecd8834ba2037bf0641b40a27fcacee84af2
}
