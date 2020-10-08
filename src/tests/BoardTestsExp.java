package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import experiment.*;

public class BoardTestsExp {
	public TestBoard board;
	
	@BeforeEach
	public void BeforeEach() {
		board = new TestBoard(4, 4);
	}
	
	@Test
	public void testAdjacency() {
		TestBoardCell cell1 = board.getCell(0, 0);
		Set<TestBoardCell> testList = cell1.getAdjList();
		
		Assert.assertTrue(testList.contains(board.getCell(0,1)));
		Assert.assertTrue(testList.contains(board.getCell(1,0)));
		
		TestBoardCell cell2 = board.getCell(3, 3);
		testList = cell2.getAdjList();
		
		Assert.assertTrue(testList.contains(board.getCell(2,3)));
		Assert.assertTrue(testList.contains(board.getCell(3,2)));
		
		TestBoardCell cell3 = board.getCell(1, 3);
		testList = cell3.getAdjList();
		
		Assert.assertTrue(testList.contains(board.getCell(0,3)));
		Assert.assertTrue(testList.contains(board.getCell(2,3)));
		Assert.assertTrue(testList.contains(board.getCell(1,2)));
		
		TestBoardCell cell4 = board.getCell(3, 0);
		testList = cell4.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2,0)));
		Assert.assertTrue(testList.contains(board.getCell(3,1)));
	}
	
	@Test
	public void testNormalTarget() {
		//tests for a 4x4 board
		TestBoardCell test1 = board.getCell(0,0);
		TestBoardCell test2 = board.getCell(3,3);
		
		board.calcTargets(test1, 1);
		Set<TestBoardCell> target = board.getTargets();
		assertTrue(target.contains(board.getCell(0, 1)));
		assertTrue(target.contains(board.getCell(1, 0)));
		board.clearTargets();
		
		board.calcTargets(test2, 2);
		target = board.getTargets();
		assertTrue(target.contains(board.getCell(2, 2)));
		assertTrue(target.contains(board.getCell(1, 3)));
		assertTrue(target.contains(board.getCell(3, 1)));
		board.clearTargets();
	}
	
	@Test
	public void testMixedTargets() {
		board.getCell(1, 1).setOccupied(true);
		board.getCell(2, 2).setRoom(true);
		board.generateBoardAdjList();
		//board.clearTargets();
		
		
		board.calcTargets(board.getCell(0, 1), 2);
		
		Set<TestBoardCell> targets = board.getTargets();
		
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertFalse(targets.contains(board.getCell(1, 1)));
		assertFalse(targets.contains(board.getCell(2, 2)));
	}
}
