/*
 * Driver class for clue game
 */


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


	public void initialize() throws BadConfigFormatException {
		// Initializes board by creating new cells
		loadSetupConfig();
		loadLayoutConfig();
		this.targets = new HashSet<BoardCell>();
		this.visited = new HashSet<BoardCell>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new BoardCell(i, j, boardString[i][j].charAt(0));
			}
		}

		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				generateBoardCellType(board[i][j]);
			}
		}
		// Generates adjacency list
		generateBoardAdjList();
		
	}

	
	// Give each boardcell its type, door, center, label, etc.
	public void generateBoardCellType(BoardCell boardCell) {
		int row = boardCell.getRow();
		int col = boardCell.getColumn();
		String bcString = boardString[row][col];
		if (bcString.length() > 1) {	// we only want to evaluate strings with two or more chars, they are the doors, centers, etc.
			Character lastChar = bcString.charAt(bcString.length() - 1);

			switch (lastChar) {
			// First four cases correspond to the enumerated types which represent door directions
			case 'v': {
				boardCell.setDoorDirection(DoorDirection.DOWN);
				boardCell.setDoorway(true);
				boardCell.setPath(true);
				addDoorToRoom(boardCell);
				break;
			}
			case '>': {
				boardCell.setDoorDirection(DoorDirection.RIGHT);
				boardCell.setDoorway(true);
				boardCell.setPath(true);
				addDoorToRoom(boardCell);
				break;
			}
			case '<': {
				boardCell.setDoorDirection(DoorDirection.LEFT);
				boardCell.setDoorway(true);
				boardCell.setPath(true);
				addDoorToRoom(boardCell);
				break;
			}
			case '^': {
				boardCell.setDoorDirection(DoorDirection.UP);
				boardCell.setDoorway(true);
				boardCell.setPath(true);
				addDoorToRoom(boardCell);
				break;
			}
			case '*':{
				roomMap.get(boardCell.getInitial()).setCenterCell(boardCell);
				boardCell.setRoomCenter(true);
				boardCell.setRoom(true);
				break;
			}
			case '#': {
				roomMap.get(boardCell.getInitial()).setLabelCell(boardCell);
				boardCell.setRoomLabel(true);
				boardCell.setRoom(true);
				break;
			}
			default: {
				boardCell.setSecretPassage(lastChar);
				roomMap.get(bcString.charAt(0)).setSecretRoom(lastChar);
				break;
			}
			}
		}
		else {	// else only one character, space card or room card
			String type = roomMap.get(bcString.charAt(0)).getCardType();
			switch (type) {
			case ROOM: {
				boardCell.setRoom(true);
				break;
			}
			case SPACE: {
				if (!(bcString.charAt(0) == 'X')) {		// x is universal to unused space, other boards use different chars for walkways
					boardCell.setPath(true);
				}
				else {
					//else, the boardCell is an unused type
					boardCell.setUnused(true);
				}
				break;
			}
			}
		}
	}

	
	public void addDoorToRoom(BoardCell doorCell) {
		// Rooms have a set of connected doors, we want to add each door connected to their associated rooms
		BoardCell roomCell = findCellAtDoorDirection(doorCell);
		roomMap.get(roomCell.getInitial()).addDoor(doorCell);
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

	// Handle all logic for a cell and its adjacent cell. Should a boardcell add an adj cell to its adj list
	public void calcAdjListLogic(BoardCell adjCell, BoardCell boardCell) {
		if (adjCell.isUnused()) {		// we never want to add unused cells to adj list
			return;
		}

		if (checkIfDoor(adjCell, boardCell)) {return;}

		if(checkIfPath(adjCell, boardCell)) {return;}

		if (checkIfRoomCenter(boardCell)) {return;}


	}

	/*
	 *  check if boardcell is a room center, if so, check all doors connected
	 *  to see if they are occupied. Also check if the room has a secret room connected
	 */
	
	public boolean checkIfRoomCenter(BoardCell boardCell) {
		if (boardCell.isRoom() && boardCell.isRoomCenter()) {
			// if boardCell is center room, get room initial to find room, then get rooms list of all connecting doors and check if 
			// the door is occupied, if not, add it to the center rooms adj list
			Room roomOfCenter = roomMap.get(boardCell.getInitial());

			for (BoardCell doorCell : roomOfCenter.getDoorList()) {
				if (!(doorCell.isOccupied())) {  
					// add cell if occupied
					boardCell.addAdj(doorCell);
				}
				else {                           
					// remove cell if not
					boardCell.removeAdj(doorCell);
				}
			}

			if (roomMap.get(roomOfCenter.getSecretRoom()) != null) {		// the room with boardCell HAS a secret room
				// if there IS a secret room, add the center cell of the secret passage room to the adj list of our boardCell
				boardCell.addAdj(roomMap.get(roomOfCenter.getSecretRoom()).getCenterCell());
			}
			return true;
		}
		return false;
	}

	// check if boardcell and adjcell are both path cells, if so, check if adjCell is occupied and handle accordingly
	public boolean checkIfPath(BoardCell adjCell, BoardCell boardCell) {
		if (boardCell.isPath() && adjCell.isPath()) {
			if (!(adjCell.isOccupied())) {
				boardCell.addAdj(adjCell);
			}
			else {
				// remove the occupied cell from adj list if it was already in adjList before
				boardCell.removeAdj(adjCell);
			}
			return true;
		}
		return false;
	}

	// check if boardcell is door and if adj cell is the cell the door points to. If so, add the room's center cell adjcell is in
	public boolean checkIfDoor(BoardCell adjCell, BoardCell boardCell) {
		if (boardCell.isDoorway()) {
			if (adjCell == findCellAtDoorDirection(boardCell)) {
				// now we want to get room that adjcell is in
				adjCell = roomMap.get(adjCell.getInitial()).getCenterCell(); // get room at adjCell initial, then get the rooms center cell 
				boardCell.addAdj(adjCell);
				return true;
			}
		}
		return false;
	}

	// Given a boardcell that was determinded to be a door, return the cell it points to
	public BoardCell findCellAtDoorDirection(BoardCell boardCell) {
		DoorDirection dd = boardCell.getDoorDirection();

		// this function is dependent on the csv file being formatted correctly, i,e no doors to non rooms
		switch (dd) {
		case UP:{
			return getCell(boardCell.getRow() - 1, boardCell.getColumn());
		}

		case DOWN:{
			return getCell(boardCell.getRow() + 1, boardCell.getColumn());
		}

		case LEFT:{
			return getCell(boardCell.getRow(), boardCell.getColumn() - 1);
		}

		case RIGHT:{
			return getCell(boardCell.getRow(), boardCell.getColumn() + 1);
		}
		default:
			// this should never happen
			return boardCell;
		}
	}

	// Recursive function, within a pathlen find all possible targets given a start cell
	public void calcTargets(BoardCell startCell, int pathLength) {
		if (visited.size() == 0) { // prepare calcTargets by getting adjLists and clearing prev targets
			targets.clear();
			generateBoardAdjList();
		}

		if (pathLength == 0) {
			// if we reach the end of our pathlength, add the cell into our targets.
			targets.add(startCell);
			return;
		}


		visited.add(startCell);		// add every cell in visited
		if (startCell.isRoomCenter()) {
			if (visited.size() == 1) {		// if we start off in a room, visited will only contain the room center cell
				for (BoardCell tbc: startCell.getAdjList()) {
					if (tbc.isDoorway()) {		// call this function with doors
						calcTargets(tbc, pathLength -1);
					}
					else if (tbc.isRoomCenter()){
						targets.add(tbc);		// add secret passage room to targets 
					}
				}
			}
			else {
				targets.add(startCell);		// else, if the size is not 1, then we have landed in the room and only want to jump there
			}
		}
		else {	// if not a room center, then deal with it normally 
			for (BoardCell tbc: startCell.getAdjList()) {
				if (!(visited.contains(tbc))) {
					visited.add(tbc);
					// Recursive call to calcTargets() until path length reaches 0
					calcTargets(tbc, pathLength -1);

					visited.remove(tbc);
				}
			}
		}
		visited.remove(startCell);		// always remove cell from visited 
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

	public Set<BoardCell> getTargets(){
		return targets;
	}
	
	public Set<BoardCell> getAdjList(int row, int col){
		return getCell(row, col).getAdjList();
	}

	public BoardCell getCell(int row, int col) {
		return board[row][col];
	}

	public int getNumColumns() {
		return this.numColumns;
	}

	public int getNumRows() {
		return this.numRows;
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
}
