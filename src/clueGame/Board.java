/*
 * Driver class for clue game
 */


package clueGame;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JPanel;

import clueGame.BoardCell;
import gui.ClueGame;

public class Board extends JPanel{
	private static Board theInstance = new Board();
	private static boolean testing;
	private int numRows;
	private int numColumns;
	private int numPeople, numWeapons, numRooms; // numRooms != roomMap.size(). roomMap holds unused rooms
	private Solution answer;
	private Player currentPlayer;
	private static int currentPlayerIndex;
	private String layoutConfigFile;
	private String setupConfigFile;
	private BoardCell[][] board;
	private String[][] boardString;
	private ArrayList<Card> deck;
	private ArrayList<Player> players;
	private Map<Character, Room> roomMap;
	private Set<Card> dealtCards;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	public final String ROOM = "Room";		// txt format room types
	public final String SPACE = "Space";
	public final String WEAPON = "Weapon";
	public final String PEOPLE = "People";
	public final String PLAYER_CHARACTER = "Cowboy";
	public final String DIR =  "data/";


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
		dealtCards = new HashSet<Card>();
		players = new ArrayList<Player>();
		board = null;
		// for testing purposes we keep track of number of all card types
		numPeople = 0;
		numWeapons = 0;
		numRooms = 0;
		currentPlayerIndex = 0;

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

	/*
	 *------------------------------------------------------------------------------
	 * 
	 * Graphics and Drawings
	 *
	 *------------------------------------------------------------------------------
	 */

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (BoardCell[] cells: board) {
			for (BoardCell cell: cells) {
				// determine the color of the cell every time we change things on the board
				determineCellColor(cell);
				cell.draw(g);
			}
		}
		for (Room room: roomMap.values()) {
			// if the room is a room and not a space
			if (room.getLabelCell() != null) {
				room.getLabelCell().drawRoomName(g, room.getName());
			}
		}

		for (Player player: players) {
			player.draw(g);
		}

	}


	// called after cell's type and position is established and in repaint
	public void determineCellColor(BoardCell boardCell) {
		// check to see if the boardCell is a target and the turn is not a computer to display blue cells

		if (targets.contains(boardCell) && currentPlayer.getPlayerType() == PlayerType.HUMAN) {
			boardCell.setColor(Color.WHITE);
		}
		else if (boardCell.isPath()) {
			boardCell.setColor(Color.ORANGE);
		} 
		else if (boardCell.isRoom()) {
			// check if the targets has this rooms center cell, if so, make the whole room white
			// we also only want to show this if its a human player
			Room potentialRoom = getRoom(boardCell);
			if (targets.contains(potentialRoom.getCenterCell()) && currentPlayer.getPlayerType() == PlayerType.HUMAN){
				boardCell.setColor(Color.white);
			}
			else {
				boardCell.setColor(new Color(50,50,50));
			}
			
			
		}
		else if (boardCell.isUnused()) {
			boardCell.setColor(Color.BLACK);
		}
	}
	
	

	/*
	 *------------------------------------------------------------------------------
	 *
	 * Players, Cards, Suggestions, and Accusations
	 * 
	 *------------------------------------------------------------------------------
	 */

	// called when nexButton is clicked on
	public void iteratePlayerIndex() {
		if (checkIfCanMoveOn()) {
			currentPlayerIndex++;
			currentPlayerIndex %= Board.getInstance().getPlayers().size();
			// bound the index by the size of players
			updateCurrentPlayer();
		}
		// if its the humans turn and they haven't finished their turn
		else if (currentPlayer.playerType != PlayerType.COMPUTER) {
			ClueGame.getInstance().displayErrorSplash("Please finish turn before moving on");
		}
	}

	// return true if we can move to next player
	public boolean checkIfCanMoveOn() {
		// if the player is a computer, it will have done what it needs to do and we can move on
		if (currentPlayer.getPlayerType() == PlayerType.COMPUTER) {
			return true;
		}
		// if the players targets is zero, then they are stuck in a room and can move onto the next player
		else if (targets.size() == 0) {
			return true;
		}
		// else player is human and need to check if they finished their turn which is triggered after target is selected
		return currentPlayer.isFinishedTurn();
	}

	public int rollDie() {
		return (int)(Math.random()*6 + 1);
	}

	public void updateCurrentPlayer() {
		currentPlayer = players.get(Board.currentPlayerIndex);
		int roll = rollDie();
		currentPlayer.selectTarget(roll);
		ClueGame.getInstance().displayPlayerAndRoll(currentPlayer, roll);

		// must repaint after changing values to allow for displaying
		repaint();
	}

	// given a point from the board being clicked on, determine what to do
	public void handleBoardClickLogic(Point point) {
		if (currentPlayer.getPlayerType() != PlayerType.HUMAN) {
			//we don't care about board clicks if its not the humans turn
			return;
		}

		// get cell at the point of clicking
		int offX = point.x % (BoardCell.getWidth());
		int offY = point.y % (BoardCell.getHeight());
		// when we click on the board, we wont always click directly on the cells origin
		// so we have to subtract the distance from the origin of the cell to find it
		int x = (point.x - offX) / BoardCell.getWidth();
		// for some reason the y cord is off by 1, so I subtract it
		int y = (point.y - offY) / BoardCell.getHeight() - 1;
		// if the player clicks off of the board, handle the exception
		try {
			BoardCell pointCell = getCell(y, x);
			validateTargetSelection(pointCell);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Clicked out of frame");
		}
	}

	// after a boardCell is clicked on, check to see if the player can move to it
	public void validateTargetSelection(BoardCell pointCell) {
		Room potentialRoom = getRoom(pointCell);

		if (targets.contains(pointCell)) {
			// clear the targets, they will be recalculated in next player
			// updatePosition repaints so targets will be pained normal colors again 
			targets.clear();
			// move the player to the point if its within the targets
			currentPlayer.updatePosition(pointCell.getRow(), pointCell.getColumn());
		}
		// if potential room isnt null, that means that the cell they clicked on was a room cell
		// and if that cell's room's center cell is in targets, we can move the player to that rooms
		// center cell
		else if(potentialRoom != null && targets.contains(potentialRoom.getCenterCell())) {
			// we don't want to move the player to a position they clicked on, we want to move them to the room
			// center of the room they clicked on
			BoardCell actualRoomCell = potentialRoom.getCenterCell();
			targets.clear();
			currentPlayer.updatePosition(actualRoomCell.getRow(), actualRoomCell.getColumn());

		}
		else {
			// display error box
			ClueGame.getInstance().displayErrorSplash("Please select valid target.");
			return;
		}
		
		// if the target selected by the human player was in a room, then set that room to their previous room
		if (potentialRoom != null) {
			currentPlayer.setPreviousRoom(potentialRoom);
		}
		// if not, then the player has moved onto a space and should set the last room to null
		else {
			currentPlayer.setPreviousRoom(null);
		}
		// after the player moved to a correct location, check if they should make a suggestion
		currentPlayer.checkIfPlayerShouldHandleSuggestion();
	}
	
	


	public Card handleSuggestion(Player player) {
		Suggestion playerSuggestion = player.getSuggestion();
		Player disprovePlayer = null;
		Card card = null;
		for (Player gamePlayer: players) {
			// do not want to handleSuggestion from player making suggestion
			if (!(gamePlayer == player)) {
				card = gamePlayer.disproveSuggestion(playerSuggestion);
				if (card != null){
					disprovePlayer = gamePlayer;
					break;
				}
			}
		}

		// if a suggestion was made, then take the player being suggested and move them to the position of the player
		// making the suggestion

		if (playerSuggestion != null && player.testing == false) {
			moveSuggestedPlayer(playerSuggestion.getPeople(), player);
		}

		// we don't want to give a null card
		if (card != null) {
			player.updateSeen(card, disprovePlayer);
		}
		// we wait until the end to return card because we wish to display everything in the control panel
		ClueGame.getInstance().updateGuessAndResult(playerSuggestion, card, player, disprovePlayer);
		return card;
	}

	public void moveSuggestedPlayer(Card playerCard, Player suggestingPlayer) {
		Player suggestedPlayer = null;
		for (Player player: players) {
			if (player.getName().equals(playerCard.getCardName())) {
				// we found player being accused
				suggestedPlayer = player;
				break;
			}
		}
		// move player who is being suggested to position of suggesting player
		suggestedPlayer.updatePosition(suggestingPlayer.getRow(), suggestingPlayer.getColumn());

	}

	public boolean makeAccusation(Solution accusation) {
		return this.answer.equals(accusation);
	}


	// deals cards from deck to players and 3 of each type of card to solution
	public void deal() {
		// use this method to shuffle deck each time. Don't need to test for random
		Collections.shuffle(deck);
		Card[] answerCards = getThreeCards();
		answer = new Solution(answerCards[0], answerCards[1], answerCards[2]);
		Collections.shuffle(deck);
		int playerIndex = 0;
		for (int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			if (!(dealtCards.contains(card))) {
				players.get(playerIndex).updateHand(card);
				dealtCards.add(card);
				playerIndex++;
				// bound playerIndex by its size. Allows for iterating through players arrayList
				playerIndex %= players.size();
			}
		}
	}
	// return the first 3 cards of each type from deck and give it to solution 
	public Card[] getThreeCards() {
		Card[] cards = new Card[3];
		for (Card card: deck) {
			switch(card.getCardType()) {
			case PEOPLE:{
				// cards[0] is people card in array
				if (cards[0] == null && !(dealtCards.contains(card))) {
					cards[0] = card;
					dealtCards.add(card);
				}
				break;
			}
			case ROOM:{
				// cards[1] is room card in array
				if (cards[1] == null && !(dealtCards.contains(card))) {
					cards[1] = card;
					dealtCards.add(card);
				}
				break;
			}
			case WEAPON:{
				// cards[2] is weapon card in array
				if (cards[2] == null && !(dealtCards.contains(card))) {
					cards[2] = card;
					dealtCards.add(card);
				}
				break;
			}

			}
		}

		return cards;
	}

	/*
	 *------------------------------------------------------------------------------
	 * 
	 * BoardCell type determination
	 * 	
	 *------------------------------------------------------------------------------
	 */


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
		// after everything is determined logically about boardcell, determine its color
		determineCellColor(boardCell);
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
			if (!(bcString.charAt(0) == 'X')) {		
				// x is universal to unused space, other boards use different chars for walkways
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
			boardCell.setRoom(true);
			roomMap.get(bcString.charAt(0)).setSecretRoom(lastChar);
			break;
		}
		}
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
					throw new BadConfigFormatException(layoutConfigFile + " has incorrect rooms");
				}
			}
		}
	}


	/*
	 *------------------------------------------------------------------------------
	 *
	 * Adjacency list and Targets calculations
	 * 
	 *------------------------------------------------------------------------------
	 */


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
				if (testing == false && getRoom(startCell) != currentPlayer.getPreviousRoom()) {
					// if the currentPlayers previous room is not the same as the room they are in, then 
					// they were pulled into that room and can make a select the room as a target
					targets.add(startCell);
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

	/*
	 * ------------------------------------------------------------------------------
	 * 
	 * File configuration
	 * 
	 * ------------------------------------------------------------------------------
	 */


	public void loadSetupConfig() throws BadConfigFormatException {  // txt file loader

		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(setupConfigFile);
			if (is == null) {
				throw new FileNotFoundException();
			}
			Scanner sc = new Scanner(is);

			// this loop goes through each line of setup .txt, grabs data, and checks if file is formatted correctly
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				// check if not comment lines
				if (!(line.contains("//"))) {
					getCardTypes(line);
				}
			}
			sc.close();
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		// we want to check each type of card using a switch case
		switch(type) {

		case SPACE:{
			Room room = new Room(name);
			room.setRoomType(type);		// used in generateBoardCellType to check if first char of boardCell is a room or a space card type
			roomMap.put(key.charAt(0), room);
			break;
		}
		case ROOM:{
			// Pass in upper case type to CardType to return enum value and give to card
			cardType = CardType.valueOf(type.toUpperCase());
			Card roomCard = new Card(name, cardType);
			deck.add(roomCard);
			Room room = new Room(name);
			room.setRoomType(type);		// used in generateBoardCellType to check if first char of boardCell is a room or a space card type
			room.setRoomCard(roomCard);
			roomMap.put(key.charAt(0), room);
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
			// if the type is PEOPLE, we want to have them as cards and players
			if (name.equals(PLAYER_CHARACTER)) {
				player = new HumanPlayer(name, 0,0, PlayerType.HUMAN, null);
			}
			else {
				player = new ComputerPlayer(name,0,0,PlayerType.COMPUTER, null);
			}
			// for debugging purposes
			player.setTesting(testing);
			players.add(player);
			numPeople++;
			break;
		}
		default:
			// if none of these, throw an exception 
			throw new BadConfigFormatException(setupConfigFile + " does not have correct card types.");
		}


	}

	public void loadLayoutConfig() throws BadConfigFormatException {  // CSV file loader
		//System.out.println(Paths.get("").toAbsolutePath());
		int colLen = 0;
		int rowLen = 0;
		try {
			Scanner sc;
			InputStream is = getClass().getClassLoader().getResourceAsStream(layoutConfigFile);
			if (is == null) {
				throw new FileNotFoundException();
			}
			sc = new Scanner(is);
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
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			board = new BoardCell[rowLen][colLen];
			// we use a boardString later in generateBoardCellType() to check if a cell is a door, room. etc.
			boardString = new String[rowLen][colLen];
			

			int b = 0;
			is = getClass().getClassLoader().getResourceAsStream(layoutConfigFile);
			sc = new Scanner(is);
			while (sc.hasNextLine()) {
				boardString[b] = sc.nextLine().split(",");	 // .split returns our row of strings for boardString
				b++;
			}
			sc.close();

			checkRooms();
		}
		catch(FileNotFoundException e) {
			System.out.println("File, " + layoutConfigFile + " could not be opened.");
		}

		this.numColumns = colLen;
		this.numRows = rowLen;
	}

	// we use this method to determine the location and color of players
	public void setPlayerInfo() {
		for (Player player: players) {
			String playerName = player.getName();
			switch (playerName) {
			// put each player into hardcoded positions and colors
			case "Sheriff":{
				// blue
				player.setColor(new Color(0,80,255));
				player.updatePosition(0, 16);
				break;
			}

			case "Harlet":{
				player.setColor(Color.MAGENTA);
				player.updatePosition(11, 23);
				break;
			}

			case "Cowboy":{
				// brown
				player.setColor(new Color(120,70,10));
				player.updatePosition(0, 8);
				break;
			}

			case "Gunsmith":{
				player.setColor(Color.LIGHT_GRAY);
				player.updatePosition(24, 15);
				break;
			}

			case "Banker":{
				// green
				player.setColor(new Color(0,225,50));
				player.updatePosition(24, 9);
				break;
			}

			case "Outlaw":{
				// maroon
				player.setColor(new Color(180,0,0));
				player.updatePosition(10, 0);
				break;
			}

			}


		}
	}

	/*
	 * ------------------------------------------------------------------------------
	 * 
	 * Getters, Setters, and Adders
	 * 
	 * ------------------------------------------------------------------------------
	 */

	public Card getCardByString(String cardString) {
		for (Card card: deck) {
			if (card.getCardName().equals(cardString)) {
				return card;
			}
		}
		return null;
	}

	// return all card types as an array 
	public Card[] getTypeCards(CardType cardType) {
		int len = 0;
		switch (cardType) {
		case PEOPLE:{
			len = numPeople;
			break;
		}
		case WEAPON:{
			len = numWeapons;
			break;
		}
		case ROOM:{
			len = numRooms;
			break;
		}

		}
		Card[] cards = new Card[len];
		int i = 0;
		for (Card card: deck) {
			if (card.getCardType() == cardType) {
				cards[i] = card;
				i++;
			}
		}
		return cards;
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

	public void setAnswer(Solution answer) {
		this.answer = answer;
	}

	public Solution getAnswer() {
		return this.answer;
	}

	public ArrayList<Card> getDeck(){
		return this.deck;
	}

	public Set<Card> getDealtCards(){
		return this.dealtCards;
	}

	public ArrayList<Player> getPlayers(){
		return this.players;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}
	public void removePlayer(Player player) {
		players.remove(players.indexOf(player));
	}

	public static int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public static void setCurrentPlayerIndex(int currentPlayerIndex) {
		currentPlayerIndex = currentPlayerIndex;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
	}

	public void setTesting(boolean testing) {
		this.testing = testing;
	}



	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();

	}



}
