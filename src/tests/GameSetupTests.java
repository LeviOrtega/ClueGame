package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;

class GameSetupTests {

	private static Board board;
	private final static int numWeapons = 6;
	private final static int numPlayers = 6;
	private final static int numPeople = 6;
	private final static int numRooms = 6;


	
	@BeforeEach
	void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout306.csv", "ClueSetup306.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	void testNumCards() {
		board.loadSetupConfig();
		
		assertEquals(numWeapons, board.getNumWeapons());
		assertEquals(numPeople, board.getNumPeople());
		assertEquals(numRooms, board.getNumRooms());
		
	}

}
