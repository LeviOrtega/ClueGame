package clueGame;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import clueGame.BoardCell;

public class Board {
	private static Board theInstance = new Board();
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private BoardCell[][] board;
	private Map<Character, Room> roomMap;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	
	private Board() {initialize();}

	public static Board getInstance() {
		return theInstance;
	}

	public void setConfigFiles(String csv, String txt) {
		
	}
	
	public Room getRoom(BoardCell cell) {
		
		return new Room();
	}
	
	public Room getRoom(Character c) {
		
		return new Room();
	}
	
	public void initialize() {
		/*
		this.targets = new HashSet<BoardCell>();
		this.visited = new HashSet<BoardCell>();
		board = new BoardCell[numRows][numColumns];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new BoardCell(i, j, ' ');
			}
		}*/
		//generateBoardAdjList();
	}

	public void generateBoardAdjList() {
		// Iterates through each index of 2D array to create comprehensive adjacency list
		// Called in TestBoard() constructor
		/*for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				calcAdjList(getCell(i,j));
			}
		}*/
	}
	
	public BoardCell getCell(int row, int col) {
		return new BoardCell(0,0, ' ');
		//return board[row][col];
	}
	
	public void calcAdjList(BoardCell bc) {
		// Up, down, left, right
		/*if (bc.getRow() - 1 >= 0) { 
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
		}*/
	}
	
	public void calcTargets(BoardCell startCell, int pathLength) {
		/*visited.add(startCell);
		
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
		visited.remove(startCell);*/
	}

	public void clearTargets() {
		//targets.clear();
	}
	
	public Set<BoardCell> getTargets(){
		return targets;
	}

	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void loadSetupConfig() {
		// TODO Auto-generated method stub
		
	}

	public void loadLayoutConfig() {
		// TODO Auto-generated method stub
		
	}

	
}
