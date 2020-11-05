package tests;


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

class ComputerAITests {

	private static Board board;
	private static ArrayList<Card> deck;
	
	@BeforeAll
	static void setUp(){
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.deal();
		deck = board.getDeck();
	}

	@Test
	void testSelectTarget() {
		Player computer = new ComputerPlayer("AI", 18, 16, PlayerType.COMPUTER);
		// we give selectTarget a variable for hardCodedRoll for testing purposes
		
		BoardCell room1 = board.getRoom('S').getCenterCell();
		BoardCell room2 = board.getRoom('G').getCenterCell();
		assertTrue(room1 == board.getCell(15, 20));
		/*
		
		for (int i = 0; i < 500; i++) {
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
			computer.updatePosition(18, 16);
			computer.selectTarget(1);
			assertTrue(
			w1 == board.getCell(computer.getRow(), computer.getColumn()) ||
			w2 == board.getCell(computer.getRow(), computer.getColumn()) ||
			w3 == board.getCell(computer.getRow(), computer.getColumn()) ||
			w4 == board.getCell(computer.getRow(), computer.getColumn())
			);
		}

		*/
		
		
		/*
		if no rooms in list, select randomly
		if room in list that has not been seen, select it
		if room in list that has been seen, each target (including room) selected randomly
		*/
	}
	@Test
	void testMakeASuggestion() {
		/*
		Room matches current location
		If only one weapon not seen, it's selected
		If only one person not seen, it's selected (can be same test as weapon)
		If multiple weapons not seen, one of them is randomly selected
		If multiple persons not seen, one of them is randomly selected
		*/
	}

}
