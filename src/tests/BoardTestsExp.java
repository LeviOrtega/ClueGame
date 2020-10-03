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
		TestBoardCell cell1 = new TestBoardCell(0,0);
		Set<TestBoardCell> testList = cell1.getAdjList();
		
		
		Assert.assertTrue(testList.contains(board.getCell(0,1)));
		Assert.assertTrue(testList.contains(board.getCell(1,0)));
		
		TestBoardCell cell2 = board.getCell(3, 3);
		testList = cell2.getAdjList();
		
		Assert.assertTrue(testList.contains(board.getCell(0,1)));
		Assert.assertTrue(testList.contains(board.getCell(0,1)));
		
		TestBoardCell cell3 = board.getCell(1, 3);
		testList = cell3.getAdjList();
		
		Assert.assertTrue(testList.contains(board.getCell(0,1)));
		Assert.assertTrue(testList.contains(board.getCell(0,1)));
		
		TestBoardCell cell4 = board.getCell(3, 0);
		testList = cell4.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0,1)));
		Assert.assertTrue(testList.contains(board.getCell(0,1)));
	}
}
