package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;

import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.PlayerType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Solution;
import gui.ClueGame;

class GameSetupTests {

	private static Board board;
	private final static int NUM_WEAPONS = 6;
	private final static int NUM_PLAYERS = 6;
	private final static int NUM_PEOPLE = 6;
	private final static int NUM_ROOMS = 9;
	private final static int NUM_CARDS = 21;
	private final static int HUMAN_COUNT = 1;
	private final static int COMPUTER_COUNT = 5;


	
	@BeforeAll
	static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		board.setTesting(true);
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		board.deal();
	}

	@Test
	void testNumCards() {
		assertEquals(NUM_WEAPONS, board.getNumWeapons());
		assertEquals(NUM_PEOPLE, board.getNumPeople());
		assertEquals(NUM_ROOMS, board.getNumRooms());
		
	}
	
	@Test
	void testCorrectCardsDelt() {
		int totalCards = NUM_WEAPONS + NUM_PEOPLE + NUM_ROOMS;
		int averageCards = (totalCards - 3)/NUM_PLAYERS;
		ArrayList<Player> players = board.getPlayers();
		
		
		assertEquals(NUM_CARDS, board.getDeck().size());
		assertEquals(NUM_PLAYERS, players.size());
		assertEquals(NUM_CARDS, board.getDealtCards().size());
		
		for (Player player: players) {
			int cardCount = 0;
			for (Card card: player.getHand()) {
				cardCount++;
			}
			// if average cards is 3.5, average is 3 but some will get one more 
			assertTrue(cardCount >= averageCards);
			assertTrue(cardCount <= averageCards + 1);
		}
		
		
	}
	
	@Test
	void testSolution() {
		Solution answer = board.getAnswer();
		ArrayList<Card> deck = board.getDeck();
		Set<Card> deltCards = board.getDealtCards();
		
		assertTrue(deltCards.contains(answer.getPeople()));
		assertTrue(deltCards.contains(answer.getWeapon()));
		assertTrue(deltCards.contains(answer.getRoom()));
		assertTrue(deck.contains(answer.getPeople()));
		assertTrue(deck.contains(answer.getWeapon()));
		assertTrue(deck.contains(answer.getRoom()));
		
	}
	
	@Test 
	void testPlayerTypes() {
		int humanCount = 0;
		int compCount = 0;
		ArrayList<Player> players = board.getPlayers();
		
		for (Player player: players) {
			PlayerType playerType = player.getPlayerType();
			switch(playerType) {
			case HUMAN:
				humanCount++;
				break;
				
			case COMPUTER:
				compCount++;
				break;
			default:
				fail();
				break;
			}
		}
		assertEquals(HUMAN_COUNT, humanCount);
		assertEquals(COMPUTER_COUNT, compCount);
		
		
	}
	@Test
	void testNoDuplicates() {
		ArrayList<Player> players = board.getPlayers();
		ArrayList<Card> usedCards = new ArrayList<>();
		
		assertEquals(NUM_PLAYERS, players.size());
		
		for(Player player: players) {
			for (Card card: player.getHand()) {
				
				assertFalse(usedCards.contains(card));
				usedCards.add(card);
			}
		}
		
	}

}
