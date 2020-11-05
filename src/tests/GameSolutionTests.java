package tests;

import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Card;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.PlayerType;
import clueGame.Solution;
import clueGame.Suggestion;

class GameSolutionTests {

	private static Board board;

	@BeforeAll
	static void setUp() throws Exception {
		board = Board.getInstance();
	}

	@Test
	void testCheckAccustion() {
		Solution answer = new Solution(
				new Card("People", CardType.PEOPLE),
				new Card("Room", CardType.ROOM),
				new Card("Weapon", CardType.WEAPON)
				);

		Solution accusation = new Solution(
				new Card("People", CardType.PEOPLE),
				new Card("Room", CardType.ROOM),
				new Card("Weapon", CardType.WEAPON)
				);
		
		Solution wrongPeople = new Solution(
				new Card("WrongPeople", CardType.PEOPLE),
				new Card("Room", CardType.ROOM),
				new Card("Weapon", CardType.WEAPON)
				);
		
		Solution wrongRoom = new Solution(
				new Card("People", CardType.PEOPLE),
				new Card("WrongRoom", CardType.ROOM),
				new Card("Weapon", CardType.WEAPON)
				);
		
		Solution wrongWeapon = new Solution(
				new Card("People", CardType.PEOPLE),
				new Card("Room", CardType.ROOM),
				new Card("WrongWeapon", CardType.WEAPON)
				);
		
		board.setAnswer(answer);
		assertTrue(board.makeAccusation(accusation));
		assertFalse(board.makeAccusation(wrongPeople));
		assertFalse(board.makeAccusation(wrongRoom));
		assertFalse(board.makeAccusation(wrongWeapon));

	}
	@Test
	void testDisproveSuggestion() {
		Suggestion suggestion = new Suggestion(
				new Card("People1", CardType.PEOPLE),
				new Card("Room1", CardType.ROOM),
				new Card("Weapon1", CardType.WEAPON)
				);
		
		Card[] hand1 = {
				new Card("People1", CardType.PEOPLE),
				new Card("Room2", CardType.ROOM),
				new Card("Weapon2", CardType.WEAPON)
				};
		
		Card[] hand2 = {
				new Card("People1", CardType.PEOPLE),
				new Card("Room1", CardType.ROOM),
				new Card("Weapon2", CardType.WEAPON)
		};
	
		Card[] hand3 = {
				new Card("People2", CardType.PEOPLE),
				new Card("Room2", CardType.ROOM),
				new Card("Weapon2", CardType.WEAPON)
		};
				
		
		// give each player their own hand
		Player player1 = new ComputerPlayer("Player1", 0,0, PlayerType.COMPUTER);
		player1.updateHand(hand1[0]);
		player1.updateHand(hand1[1]);
		player1.updateHand(hand1[2]);
		
		Player player2 = new ComputerPlayer("Player2", 0,0, PlayerType.COMPUTER);
		player2.updateHand(hand2[0]);
		player2.updateHand(hand2[1]);
		player2.updateHand(hand2[2]);
		
		Player player3 = new ComputerPlayer("Player3", 0,0, PlayerType.COMPUTER);
		player3.updateHand(hand3[0]);
		player3.updateHand(hand3[1]);
		player3.updateHand(hand3[2]);
		
		
		
		
		Card result1 = player1.disproveSuggestion(suggestion);
		Card result2 = player2.disproveSuggestion(suggestion);
		Card result3 = player3.disproveSuggestion(suggestion);

		
		assertTrue(result1.equals(hand1[0]));
		// tests for random. This will return 1 of 2 cards that match
		for (int i = 0; i < 500; i++) {
			assertTrue(result2.equals(hand2[0]) || result2.equals(hand2[1]));
			result2 = player2.disproveSuggestion(suggestion);
		}
		assertEquals(null, result3);

		
		
		
	}
	@Test
	void testHandleSuggestion() {
		/*
		 * 	Suggestion no one can disprove returns null
			Suggestion only accusing player can disprove returns null
			Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
			Suggestion that two players can disprove, correct player (based on starting with next player in list) returns answer
		 */
	}

}
