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
	public final String ROOM = "Room";		// txt format card types
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

	public void initialize() throws BadConfigFormatException {
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
		if (bcString.length() > 1) {	// we only want to evaluate strings with two or more chars, they are the doors, centers, etc.
			Character lastChar = bcString.charAt(bcString.length() -1);

			switch (lastChar) {
			// First four cases correspond to the enumerated types which represent door directions
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
		else {	// else only one character, space card or room card
			String type = roomMap.get(bcString.charAt(0)).getCardType();
			switch (type) {
			case ROOM: {
				bc.setRoom(true);
				break;
			}
			case SPACE: {
				if (!(bcString.charAt(0) == 'X')) {		// x is universal to unused space, other boards use different chars for walkways
					bc.setPath(true);
				}
				break;
			}
			}
		}
	}

	public void loadSetupConfig() throws BadConfigFormatException {  // txt file loader
		roomMap = new HashMap<Character, Room>();
		try {
			File setup = new File("data/" + setupConfigFile);
			Scanner sc = new Scanner(setup);

			// this loop goes through each line of setup .txt, grabs data, and checks if file is formatted correctly
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				// check if not comment lines
				if (!(line.split(" ")[0].equals("//"))) {
					// split the line string into an array of strings to use for rooms
					String[] roomInfo = line.split(", ");
					if (!(roomInfo[0].equals(SPACE)) && !(roomInfo[0].equals(ROOM))) {
						throw new BadConfigFormatException(setupConfigFile + " does not have only " + SPACE + " or " + ROOM + " card types.");
					}
					Room room = new Room(roomInfo[1]);
					room.setCardType(roomInfo[0]);		// used in generateBoardCellType to check if first char of boardCell is a room or a space card type
					roomMap.put(roomInfo[2].charAt(0), room);
				}
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File, " + setupConfigFile + " could not be opened.");
		}
	}

	public void loadLayoutConfig() throws BadConfigFormatException {  // CSV file loader
		int colLen = 0;
		int rowLen = 0;
		try {
			File layout = new File("data/" + layoutConfigFile);
			Scanner sc = new Scanner(layout);
			String in = sc.nextLine();
			String[] column = in.split(",");
			colLen = column.length;
			rowLen = 1;		// Because we used the sc.nextLine() before, we are now down one row.
			// this loop checks if csv file is formatted correctly with number of columns and gives us our row length
			while (sc.hasNextLine()) {
				rowLen ++;
				String[] colCheck = sc.nextLine().split(",");	// calls sc.nextLine() and gives a string of len of col for checking to see if all cols are same length
				if (colCheck.length != colLen) {
					throw new BadConfigFormatException(layoutConfigFile + " does not have correctly formated columns.");
				}
			}

			sc.close();
			board = new BoardCell[rowLen][colLen];
			// we use a boardString later in generateBoardCellType() to check if a cell is a door, room. etc.
			boardString = new String[rowLen][colLen];

			int b = 0;
			sc = new Scanner(layout);
			while (sc.hasNextLine()) {
				boardString[b] = sc.nextLine().split(",");	 // .split returns our row of strings for boardString
				b++;
			}
			sc.close();

			checkRooms();
		}
		catch(FileNotFoundException e) {
			System.out.println("File " + layoutConfigFile + " cannot be opened.");
		}

		this.numColumns = colLen;
		this.numRows = rowLen;
	}

	public void checkRooms() throws BadConfigFormatException {
		// check if csv file had correct characters

		for (int i = 0; i < boardString.length; i++) {
			for (int j = 0; j < boardString[0].length; j++) {
				// if the first char is given to roomMap as a key, and it returns null, roomMap does not have that value
				if (roomMap.get(boardString[i][j].charAt(0)) == null) {
					//System.out.println("hello");
					throw new BadConfigFormatException(layoutConfigFile + " has incorrect rooms");
				}
			}
		}
	}

	public void generateBoardAdjList() {
		// Iterates through each index of 2D array to create comprehensive adjacency list
		// Called in TestBoard() constructor
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				calcAdjListBounds(getCell(i,j));
			}
		}
	}

	public void calcAdjListBounds(BoardCell boardCell) {
		// Up, down, left, right
		if (boardCell.getRow() - 1 >= 0) { 
			calcAdjListLogic(getCell(boardCell.getRow() - 1, boardCell.getColumn()), boardCell);
		}
		if ((boardCell.getRow() + 1) <= this.numRows - 1) {
			calcAdjListLogic(getCell(boardCell.getRow() + 1, boardCell.getColumn()), boardCell);
		}
		if (boardCell.getColumn() - 1 >= 0) {
			calcAdjListLogic(getCell(boardCell.getRow(), boardCell.getColumn() - 1), boardCell);
		}
		if ((boardCell.getColumn() + 1) <= this.numColumns - 1) {
			calcAdjListLogic(getCell(boardCell.getRow(), boardCell.getColumn() + 1), boardCell);
		}
	}
	
	public void calcAdjListLogic(BoardCell adjCell, BoardCell boardCell) {

		
		/*if (bc.isOccupied() == false && bc.isRoom() == false) {
			// TestBoardCell object added to adjacency list iff spot is not occupied or a marked room
			adjList.add(bc);
		}
		else {
			// TestBoardCell object is otherwise removed (not applicable in adjacency list)
			this.adjList.remove(bc);
		}*/
	}

	public Set<BoardCell> getAdjList(int row, int col){
		return getCell(row, col).getAdjList();
	}

	public BoardCell getCell(int row, int col) {
		return board[row][col];
	}

	public void calcTargets(BoardCell startCell, int pathLength) {
		visited.add(startCell);

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
		visited.remove(startCell);
	}

	public void clearTargets() {
		targets.clear();
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
