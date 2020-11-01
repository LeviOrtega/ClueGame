package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import clueGame.Card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Solution;

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
		board.loadSetupConfig();
	}

	@Test
	void testNumCards() {
		assertEquals(numWeapons, board.getNumWeapons());
		assertEquals(numPeople, board.getNumPeople());
		assertEquals(numRooms, board.getNumRooms());
		
	}
	
	@Test
	void testSolution() {
		Solution answer = board.getAnswer();
		Set<Card> deck = board.getDeck();
		Set<Card> deltCards = board.getDeltCards();
		
		
		assertTrue(deltCards.contains(answer.getPerson()));
		assertTrue(deltCards.contains(answer.getWeapon()));
		assertTrue(deltCards.contains(answer.getRoom()));
		assertFalse(deck.contains(answer.getPerson()));
		assertFalse(deck.contains(answer.getWeapon()));
		assertFalse(deck.contains(answer.getRoom()));
		
		
		
	}

}
