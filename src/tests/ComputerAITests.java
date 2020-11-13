package tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Card;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.PlayerType;
import clueGame.Suggestion;

class ComputerAITests {

	private static Board board;
	private static ArrayList<Card> deck;

	@BeforeAll
	static void setUp(){
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		deck = board.getDeck();

	}

	@Test
	void testSelectTarget() {
		Player computer = new ComputerPlayer("AI", 18, 16, PlayerType.COMPUTER, null);
		// we give selectTarget a variable for hardCodedRoll for testing purposes

		BoardCell room1 = board.getRoom('S').getCenterCell();
		BoardCell room2 = board.getRoom('G').getCenterCell();
		assertTrue(room1 == board.getCell(15, 20));


		for (int i = 0; i < 500; i++) {
			// check to see if the locaitons are rooms, should only choose rooms if they have them in range
			computer.updatePosition(18, 16);
			computer.selectTarget(4);
			assertTrue(
					room1 == board.getCell(computer.getRow(), computer.getColumn()) ||
					room2 == board.getCell(computer.getRow(), computer.getColumn()));
		}

		BoardCell w1 = board.getCell(18, 17);
		BoardCell w2 = board.getCell(18, 15);
		BoardCell w3 = board.getCell(17, 16);
		BoardCell w4 = board.getCell(19, 16);

		for (int i = 0; i < 500; i++) {
			// loop through to check for randomness, if not one of these locations, fail
			computer.updatePosition(18, 16);
			computer.selectTarget(1);
			assertTrue(
					w1 == board.getCell(computer.getRow(), computer.getColumn()) ||
					w2 == board.getCell(computer.getRow(), computer.getColumn()) ||
					w3 == board.getCell(computer.getRow(), computer.getColumn()) ||
					w4 == board.getCell(computer.getRow(), computer.getColumn())
					);
		}

	}
	@Test
	void testMakeASuggestion() {
		// know these things from clueSetup.txt
		Card saloon = deck.get(0);
		Card revolver = deck.get(9);
		Card sheriff = deck.get(15);

		assertTrue(saloon.toString().equals("Saloon"));
		assertTrue(revolver.toString().equals("Revolver"));
		assertTrue(sheriff.toString().equals("Cowboy"));

		Suggestion suggestion = new Suggestion(sheriff, saloon, revolver);
		Player computer = new ComputerPlayer("AI", 15,20,PlayerType.COMPUTER, null);
		Suggestion aiSuggestion = computer.createSuggestion();

		/* because the deck has not been shuffled, and the computer has not been given any cards
		 it will make a suggestion in the room its in, saloon, and the first weapon and people card
		revolver and cowboy
		 */ 
		assertNotEquals(null, aiSuggestion);
		assertTrue(suggestion.equals(aiSuggestion));
		// adding the revolver to its hand, it will no longer make the same suggesiton
		computer.updateHand(revolver);
		aiSuggestion = computer.createSuggestion();
		assertFalse(suggestion.equals(aiSuggestion));

		
		// put computer outside of room, should return null if you create suggestion
		computer.updatePosition(16, 14);
		aiSuggestion = computer.createSuggestion();
		assertEquals(null, aiSuggestion);

		
		
		
	}

}
