package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import clueGame.BoardCell;

public class Board {
	private static Board theInstance = new Board();
	private int numRows;
	private int numColumns;
	public final String ROOM = "Room";
	public final String SPACE = "Space";
	private String layoutConfigFile;
	private String setupConfigFile;
	private BoardCell[][] board;
	private String[][] boardString;
	private Map<Character, Room> roomMap;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;

	private Board() {}

	public static Board getInstance() {
		return theInstance;
	}

	public void setConfigFiles(String csv, String txt) {
		this.setupConfigFile = txt;
		this.layoutConfigFile = csv;
	}

	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());

	}

	public Room getRoom(Character c) {

		return roomMap.get(c);
	}

	public void initialize() throws BadConfigFormatException{
		loadSetupConfig();
		loadLayoutConfig();
		this.targets = new HashSet<BoardCell>();
		this.visited = new HashSet<BoardCell>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new BoardCell(i, j, boardString[i][j].charAt(0));
				generateBoardCellType(board[i][j]);
			}
		}

		generateBoardAdjList();
	}

	public void generateBoardCellType(BoardCell bc) {
		int row = bc.getRow();
		int col = bc.getColumn();
		String bcString = boardString[row][col];
		if (bcString.length() > 1) {			// we only want to evaluate strings with two or more chars, they are the doors, centers, etc.
			Character lastChar = bcString.charAt(bcString.length() -1);

			switch (lastChar) {
			case 'v': {
				bc.setDoorDirection(DoorDirection.DOWN);
				bc.setDoorway(true);
				break;
			}
			case '>': {
				bc.setDoorDirection(DoorDirection.RIGHT);
				bc.setDoorway(true);
				break;
			}
			case '<': {
				bc.setDoorDirection(DoorDirection.LEFT);
				bc.setDoorway(true);
				break;
			}
			case '^': {
				bc.setDoorDirection(DoorDirection.UP);
				bc.setDoorway(true);
				break;
			}
			case '*':{
				roomMap.get(bc.getInitial()).setCenterCell(bc);
				bc.setRoomCenter(true);
				bc.setRoom(true);
				break;
			}
			case '#': {
				roomMap.get(bc.getInitial()).setLabelCell(bc);
				bc.setRoomLabel(true);
				bc.setRoom(true);
				break;
			}
			default: {
				bc.setSecretPassage(lastChar);
				break;
			}
			}
		}

	}

	public void loadSetupConfig() throws BadConfigFormatException{			// txt file loader
		roomMap = new HashMap<Character, Room>();
		try {
			File setup = new File("data/" + setupConfigFile);
			Scanner sc = new Scanner(setup);

			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				if (!(line.split(" ")[0].equals("//"))) {
					String[] roomInfo = line.split(", ");
					if (!(roomInfo[0].equals(SPACE)) && !(roomInfo[0].equals(ROOM))) {
						throw new BadConfigFormatException(setupConfigFile + " does not have only " + SPACE + " or " + ROOM + " card types.");
					}
					Room room = new Room(roomInfo[1]);
					roomMap.put(roomInfo[2].charAt(0), room);
					/*
					 for (String s: roomInfo) {
						System.out.println(s);
					}
					 */
				}
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File, " + setupConfigFile + " could not be opened.");
		}

	}

	public void loadLayoutConfig() throws BadConfigFormatException {		// CSV file loader
		int colLen = 0;
		int rowLen = 0;
		try {
			File layout = new File("data/" + layoutConfigFile);
			Scanner sc = new Scanner(layout);
			String in = sc.nextLine();
			String[] column = in.split(",");
			colLen = column.length;
			rowLen = 1;		//because we used the sc.nextLine() before, we are now down one row
			while (sc.hasNextLine()) {
				rowLen ++;
				String[] colCheck = sc.nextLine().split(",");		// calls sc.nextLine() and gives a string of len of col for checking to see if all cols are same lenght
				if (colCheck.length != colLen) {
					throw new BadConfigFormatException(layoutConfigFile + " does not have correctly formated columns.");
				}
			}

			sc.close();
			board = new BoardCell[rowLen][colLen];
			boardString = new String[rowLen][colLen];

			int b = 0;
			sc = new Scanner(layout);
			while (sc.hasNextLine()) {
				boardString[b] = sc.nextLine().split(",");		// .split returns our row of strings for boardString
				b++;
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File " + layoutConfigFile + " cannot be opened.");
		}

		for (int i = 0; i < boardString.length; i++) {
			for (int j = 0; j < boardString[0].length; j++) {
				
				if (roomMap.get(boardString[i][j].charAt(0)) == null) {
					//System.out.println("hello");
					throw new BadConfigFormatException(layoutConfigFile + " has incorrect rooms");
				}
			}
		}

		this.numColumns = colLen;
		this.numRows = rowLen;
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
		return board[row][col];
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
		return this.numColumns;
	}

	public int getNumRows() {
		return this.numRows;
	}

}
