package tests;

import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Card;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.CardType;
import clueGame.Solution;

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
		/*
		 * 	If player has only one matching card it should be returned
			If players has >1 matching card, returned card should be chosen randomly
			If player has no matching cards, null is returned
		 */
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
