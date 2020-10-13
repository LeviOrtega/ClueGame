package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// Test jail which only has one door
		Set<BoardCell> testList = board.getAdjList(3, 4);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(6, 8)));
		
		// now test the bank which has 2 doors and secret passage to hotel
		testList = board.getAdjList(3, 21);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(5, 20)));
		assertTrue(testList.contains(board.getCell(5, 21)));
		assertTrue(testList.contains(board.getCell(22, 3)));
		
		// test saloon, only one entrance 
		testList = board.getAdjList(15, 20);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(16, 16)));
	}
	
	
	@Test
	public void testAdjacencyDoor() 
	{
		Set<BoardCell> testList = board.getAdjList(6, 4);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(7, 2)));			// jail center
		assertTrue(testList.contains(board.getCell(6, 5)));
		assertTrue(testList.contains(board.getCell(7, 4)));

		testList = board.getAdjList(8, 17);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 17)));		// hitching post center
		assertTrue(testList.contains(board.getCell(8, 18)));
		assertTrue(testList.contains(board.getCell(7, 17)));
		
		testList = board.getAdjList(19, 12);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(21, 11)));		// outhouse center
		assertTrue(testList.contains(board.getCell(18, 12)));
		assertTrue(testList.contains(board.getCell(20, 12)));
		assertTrue(testList.contains(board.getCell(19, 13)));
	}
	
	
	
}
