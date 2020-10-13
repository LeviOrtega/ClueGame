package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

class FileInitTests {

	// Constants that I will use to test whether the file was loaded correctly
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 25;
	public static final int NUM_COLUMNS = 24;

	// NOTE: I made Board static because I only want to set it up one
	// time (using @BeforeAll), no need to do setup before each test.
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few others
		assertEquals("Saloon", board.getRoom('S').getName() );
		assertEquals("General Store", board.getRoom('G').getName() );
		assertEquals("Bank", board.getRoom('B').getName() );
		assertEquals("Hitching Post", board.getRoom('P').getName() );
		assertEquals("Jail", board.getRoom('J').getName() );
		assertEquals("Trading Stable", board.getRoom('T').getName() );
		assertEquals("Law", board.getRoom('L').getName() );
		assertEquals("Outhouse", board.getRoom('O').getName() );
		assertEquals("Hotel", board.getRoom('H').getName() );
		assertEquals("Road", board.getRoom('R').getName() );
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus
	// two cells that are not a doorway.
	// These cells are white on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		BoardCell cell = board.getCell(17, 8);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(6, 8);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(16, 16);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(19, 18);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(18, 14);
		assertFalse(cell.isDoorway());
	}

	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway()) {
					numDoors++;
				}	
			}
		Assert.assertEquals(12, numDoors);
	}

	// Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRooms() {
		// just test a standard room location
		BoardCell cell = board.getCell(3, 22);
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Bank" ) ;
		assertFalse( cell.isLabel() );
		assertFalse( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;

		// this is a label cell to test
		cell = board.getCell(1, 5);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Law" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		
		// this is a room center cell to test
		cell = board.getCell(15, 3);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Trading Stable" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );
		
		// this is a secret passage test
		cell = board.getCell(4, 0);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Jail" ) ;
		assertTrue( cell.getSecretPassage() == 'G' );
		
		// test a walkway
		cell = board.getCell(18, 14);
		room = board.getRoom( cell ) ;
		// Note for our purposes, walkways and closets are rooms
		assertTrue( room != null );
		assertEquals( room.getName(), "Road" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		
		// test a closet
		cell = board.getCell(24, 18);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
	}
}
