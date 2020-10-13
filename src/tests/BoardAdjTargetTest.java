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
	public void testAdjacenciesRooms() {
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
	public void testAdjacencyDoor() {
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
	
	@Test
	public void testAdjacencyWalkways() {
		// Test on top of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(0, 8);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(1,8)));
		
		// Test near a door but not adjacent
		testList = board.getAdjList(18, 5);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(18, 4)));
		assertTrue(testList.contains(board.getCell(18, 6)));
		assertTrue(testList.contains(board.getCell(19, 5)));

		// Test adjacent to walkways
		testList = board.getAdjList(17, 11);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(17, 10)));
		assertTrue(testList.contains(board.getCell(17, 12)));
		assertTrue(testList.contains(board.getCell(16, 11)));
		assertTrue(testList.contains(board.getCell(18, 11)));

		// Test next to unused
		testList = board.getAdjList(10,8);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(9, 8)));
		assertTrue(testList.contains(board.getCell(11, 8)));
		assertTrue(testList.contains(board.getCell(10, 7)));
	}
	
	@Test
	public void testTargetsInGeneralStore() {
		// test a roll of 1
		board.calcTargets(board.getCell(21, 20), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(19, 18)));
		assertTrue(targets.contains(board.getCell(7, 2)));
		
		// test a roll of 3
		board.calcTargets(board.getCell(21, 20), 3);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(7, 2)));
		assertTrue(targets.contains(board.getCell(18, 17)));	
		assertTrue(targets.contains(board.getCell(19, 16)));
	
		// test a roll of 4
		board.calcTargets(board.getCell(21, 20), 4);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(7, 2)));
		assertTrue(targets.contains(board.getCell(18, 20)));	
		assertTrue(targets.contains(board.getCell(18, 16)));
		assertTrue(targets.contains(board.getCell(19, 15)));	
	}

	@Test
	public void testTargetsInHotel() {
		// test a roll of 1
		board.calcTargets(board.getCell(22, 3), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(19, 4)));
		assertTrue(targets.contains(board.getCell(3, 21)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(22, 3), 3);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 21)));
		assertTrue(targets.contains(board.getCell(18, 3)));	
		assertTrue(targets.contains(board.getCell(19, 6)));
		assertTrue(targets.contains(board.getCell(18, 5)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(22, 3), 4);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 21)));
		assertTrue(targets.contains(board.getCell(19, 5)));	
		assertTrue(targets.contains(board.getCell(18, 4)));
		assertTrue(targets.contains(board.getCell(18, 2)));	
	}
	
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(13, 5), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(15, 3)));
		assertTrue(targets.contains(board.getCell(12, 5)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(13, 5), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(15, 3)));
		assertTrue(targets.contains(board.getCell(10, 5)));
		assertTrue(targets.contains(board.getCell(11, 4)));	
		assertTrue(targets.contains(board.getCell(12, 3)));
		assertTrue(targets.contains(board.getCell(11, 6)));	
		assertTrue(targets.contains(board.getCell(12, 7)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(13, 5), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(15, 3)));
		assertTrue(targets.contains(board.getCell(11, 5)));
		assertTrue(targets.contains(board.getCell(12, 2)));	
		assertTrue(targets.contains(board.getCell(12, 8)));
		assertTrue(targets.contains(board.getCell(9, 5)));	
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(10, 21), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(10, 20)));
		assertTrue(targets.contains(board.getCell(10, 22)));	
		assertTrue(targets.contains(board.getCell(11, 21)));	
		assertTrue(targets.contains(board.getCell(9, 21)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(10, 21), 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(11, 23)));
		assertTrue(targets.contains(board.getCell(12, 22)));
		assertTrue(targets.contains(board.getCell(11, 21)));
		assertTrue(targets.contains(board.getCell(7, 21)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(10, 21), 4);
		targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(6, 21)));
		assertTrue(targets.contains(board.getCell(11, 22)));
		assertTrue(targets.contains(board.getCell(12, 21)));	
		assertTrue(targets.contains(board.getCell(12, 19)));	
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(10, 4), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(9, 4)));
		assertTrue(targets.contains(board.getCell(10, 3)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(10, 4), 3);
		targets = board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCell(9, 4)));
		assertTrue(targets.contains(board.getCell(7, 4)));
		assertTrue(targets.contains(board.getCell(11, 2)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(10, 4), 4);
		targets = board.getTargets();
		assertEquals(20, targets.size());
		assertTrue(targets.contains(board.getCell(11, 3)));
		assertTrue(targets.contains(board.getCell(13, 5)));
		assertTrue(targets.contains(board.getCell(6, 4)));	
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		//TODO have calc targets work with generate adjList
		board.getCell(12, 4).setOccupied(true);
		board.calcTargets(board.getCell(10, 4), 4);
		board.getCell(12, 4).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(19, targets.size());
		assertTrue(targets.contains(board.getCell(11, 3)));
		assertTrue(targets.contains(board.getCell(13, 5)));
		assertTrue(targets.contains(board.getCell(6, 4)));	
		assertFalse(targets.contains( board.getCell(12, 4)));
		assertFalse(targets.contains( board.getCell(7, 3)));
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(21, 3).setOccupied(true);
		board.getCell(19, 6).setOccupied(true);
		board.calcTargets(board.getCell(19, 5), 1);
		board.getCell(21, 3).setOccupied(false);
		board.getCell(19, 6).setOccupied(false);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(21, 3)));	
		assertTrue(targets.contains(board.getCell(18, 5)));	
		assertTrue(targets.contains(board.getCell(19, 4)));	
		
		// check leaving a room with a blocked doorway
		board.getCell(5, 21).setOccupied(true);
		board.calcTargets(board.getCell(3, 21), 3);
		board.getCell(5, 21).setOccupied(false);
		targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(7, 20)));
		assertTrue(targets.contains(board.getCell(6, 21)));	
	}
}
