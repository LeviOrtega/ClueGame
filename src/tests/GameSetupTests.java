package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.PlayerType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Solution;

class GameSetupTests {

	private static Board board;
	private final static int NUM_WEAPONS = 6;
	private final static int NUM_PLAYERS = 6;
	private final static int NUM_PEOPLE = 6;
	private final static int NUM_ROOMS = 6;
	private final static int NUM_CARDS = 18;
	private final static int HUMAN_COUNT = 1;
	private final static int COMPUTER_COUNT = 5;


	
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
		assertEquals(NUM_WEAPONS, board.getNumWeapons());
		assertEquals(NUM_PEOPLE, board.getNumPeople());
		assertEquals(NUM_ROOMS, board.getNumRooms());
		
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
	
	@Test
	void testCorrectCardsDelt() {
		int totalCards = NUM_WEAPONS + NUM_PEOPLE + NUM_ROOMS;
		int averageCards = (totalCards - 3)/NUM_PLAYERS;
		Set<Player> players = board.getPlayers();
		
		assertEquals(NUM_PLAYERS, players.size());
		assertEquals(0, board.getDeck().size());
		assertEquals(NUM_CARDS, board.getDeltCards().size());
		
		for (Player player: players) {
			int cardCount = 0;
			for (Card card: player.getHand()) {
				cardCount++;
			}
			// if average cards is 2.5, average is 2 but some will get one more 
			assertFalse(cardCount >= averageCards);
			assertTrue(cardCount <= averageCards + 1);
		}
		
		
	}
	
	@Test 
	void testPlayerTypes() {
		int humanCount = 0;
		int compCount = 0;
		Set<Player> players = board.getPlayers();
		
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

}
