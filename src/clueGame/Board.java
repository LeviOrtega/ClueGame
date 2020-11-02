/*
 * Driver class for clue game
 */


package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import clueGame.BoardCell;

public class Board {
	private static Board theInstance = new Board();
	private int numRows;
	private int numColumns;
	private int numPeople, numWeapons, numRooms; // numRooms != roomMap.size(). roomMap holds unused rooms
	private Solution answer;
	public final String ROOM = "Room";		// txt format room types
	public final String SPACE = "Space";
	public final String WEAPON = "Weapon";
	public final String PEOPLE = "People";
	public final String PLAYER_CHARACTER = "Cowboy";
	private String layoutConfigFile;
	private String setupConfigFile;
	private BoardCell[][] board;
	private String[][] boardString;
	private Map<Character, Room> roomMap;
	private ArrayList<Card> deck;
	private Set<Card> deltCards;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	private ArrayList<Player> players;

	private Board() {}

	public static Board getInstance() {
		return theInstance;
	}


	public void initialize() throws BadConfigFormatException {
		// Initializes board by creating new cells
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		roomMap = new HashMap<Character, Room>();
		deck = new ArrayList<Card>();
		deltCards = new HashSet<Card>();
		players = new ArrayList<Player>();
		numPeople = 0;
		numWeapons = 0;
		numRooms = 0;
		
		loadSetupConfig();
		loadLayoutConfig();
		// initialize board cell with indexes only
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new BoardCell(i, j, boardString[i][j].charAt(0));
			}
		}

		// give each board cell their type
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
			determineSecondCharType(boardCell, bcString);
		}
		else {	// else only one character, space card or room card
			determineFirstCharType(boardCell, bcString);
		}
	}

	// if the cell at index of boardcell is only one char, boardCell is a room or a space
	public void determineFirstCharType(BoardCell boardCell, String bcString) {
		String type = roomMap.get(bcString.charAt(0)).getRoomType();
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

	/*
	 *  if the cell at index of boardcell has more than one char, 
	 *  it is a special char i,e door, room center, room label, secret room
	 */
	public void determineSecondCharType(BoardCell boardCell, String bcString) {
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
		if (adjCell.isUnused()) {return;}		// we never want to add unused cells to adj list
		else if (checkIfDoor(adjCell, boardCell)) {return;}
		else if(checkIfPath(adjCell, boardCell)) {return;}
		else if (checkIfRoomCenter(boardCell)) {return;}
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
				for (BoardCell targetBoardCell: startCell.getAdjList()) {
					if (targetBoardCell.isDoorway()) {		// call this function with doors
						calcTargets(targetBoardCell, pathLength -1);
					}
					else if (targetBoardCell.isRoomCenter()){
						targets.add(targetBoardCell);		// add secret passage room to targets 
					}
				}
			}
			else {
				targets.add(startCell);		// else, if the size is not 1, then we have landed in the room and only want to jump there
			}
		}
		else {	// if not a room center, then deal with it normally 
			for (BoardCell targetBoardCell: startCell.getAdjList()) {
				if (!(visited.contains(targetBoardCell))) {
					visited.add(targetBoardCell);
					// Recursive call to calcTargets() until path length reaches 0
					calcTargets(targetBoardCell, pathLength -1);

					visited.remove(targetBoardCell);
				}
			}
		}
		visited.remove(startCell);		// always remove cell from visited 
	}

	public void deal() {
		Collections.shuffle(deck);
		Card[] answerCards = getThreeCards(null,null, null);
		answer = new Solution(answerCards[0], answerCards[1], answerCards[2]);
		
		int playerIndex = 0;
		for (int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			if (!(deltCards.contains(card))) {
				players.get(playerIndex).updateHand(card);
				deltCards.add(card);
				playerIndex++;
				playerIndex %= players.size();
			}
		}
		for(Player player: players) {
			System.out.println((player.getHand().toString()));
		}
		
	}

	public Card[] getThreeCards(Card roomCard, Card weaponCard, Card peopleCard) {
		Card[] cards = new Card[3];
		for (Card card: deck) {
			switch(card.getCardType()) {
			case ROOM:{
				if (roomCard == null && !(deltCards.contains(card))) {
					roomCard = card;
					cards[0] = card;
					deltCards.add(card);
				}
				break;
			}
			case WEAPON:{
				if (weaponCard == null && !(deltCards.contains(card))) {
					weaponCard = card;
					cards[1] = card;
					deltCards.add(card);
				}
				break;
			}
			case PEOPLE:{
				if (peopleCard == null && !(deltCards.contains(card))) {
					peopleCard = card;
					cards[2] = card;
					deltCards.add(card);
				}
				break;
			}
			
			}
		}
		return cards;
		
	}


	public void loadSetupConfig() throws BadConfigFormatException {  // txt file loader
		
		try {
			File setup = new File("data/" + setupConfigFile);
			Scanner sc = new Scanner(setup);

			// this loop goes through each line of setup .txt, grabs data, and checks if file is formatted correctly
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				// check if not comment lines
				if (!(line.contains("//"))) {
					getCardTypes(line);
				}
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File, " + setupConfigFile + " could not be opened.");
		}
	}

	public void getCardTypes(String line) throws BadConfigFormatException {
		// split the line string into an array of strings to use for rooms
		String[] roomInfo = line.split(", ");
		String type = roomInfo[0];
		String name = roomInfo[1];
		String key = roomInfo[2];
		CardType cardType;
		switch(type) {

		case SPACE:{
			Room room = new Room(name);
			room.setRoomType(type);		// used in generateBoardCellType to check if first char of boardCell is a room or a space card type
			roomMap.put(key.charAt(0), room);
			break;
		}
		case ROOM:{
			Room room = new Room(name);
			room.setRoomType(type);		// used in generateBoardCellType to check if first char of boardCell is a room or a space card type
			roomMap.put(key.charAt(0), room);
			// Pass in upper case type to CardType to return enum value and give to card
			cardType = CardType.valueOf(type.toUpperCase());
			deck.add(new Card(name, cardType));
			numRooms++;
			break;
		}
		case WEAPON:{
			cardType = CardType.valueOf(type.toUpperCase());
			deck.add(new Card(name, cardType));
			numWeapons++;
			break;
		}
		case PEOPLE:{
			cardType = CardType.valueOf(type.toUpperCase());
			deck.add(new Card(name, cardType));
			Player player;
			
			if (name.equals(PLAYER_CHARACTER)) {
				player = new HumanPlayer(name, 0,0, PlayerType.HUMAN);
			}
			else {
				player = new ComputerPlayer(name,0,0,PlayerType.COMPUTER);
			}
			players.add(player);
			numPeople++;
			break;
		}
		default:
			// if none of these f
			throw new BadConfigFormatException(setupConfigFile + " does not have correct card types.");
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

	public int getNumRooms() {
		return this.numRooms;
	}

	public int getNumPeople() {
		return this.numPeople;
	}

	public int getNumWeapons() {
		return this.numWeapons;
	}

	public int getNumPlayers() {
		return this.players.size();
	}

	public Solution getAnswer() {
		return this.answer;
	}

	public ArrayList<Card> getDeck(){
		return this.deck;
	}

	public Set<Card> getDeltCards(){
		return this.deltCards;
	}

	public ArrayList<Player> getPlayers(){
		return this.players;
	}
}
