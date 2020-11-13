/*
 * Individual cell in the game board  
 */

package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import clueGame.BoardCell;

public class BoardCell {
	private int row;
	private int column;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private boolean occupied;
	private boolean inRoom;
	private boolean isDoorway;
	private boolean isPath;
	private boolean isUnused;
	private Set<BoardCell> adjList;

	public BoardCell(int row, int column, char initial) { // Constructor
		this.row = row;
		this.column = column;
		this.initial = initial;
		this.adjList = new HashSet<BoardCell>();
	}

	public void draw(Graphics g) {
		Board board = Board.getInstance();
		int width = board.getWidth() / board.getNumColumns();
		int height = board.getHeight() / board.getNumRows();
		// the x and y position of the rectangles will be shifted by each rectangles width and height
		int x = this.column * width;
		int y = this.row * height;

		if (this.isPath) {
			g.setColor(Color.ORANGE);
		} 
		else if (this.inRoom) {
			g.setColor(Color.GRAY);
		}
		else if (this.isUnused) {
			g.setColor(Color.BLACK);
		}

		g.fillRect(x, y, width, height);

		if (this.isDoorway) {
			g.setColor(Color.RED);
			
			switch(this.doorDirection) {
			case UP:{
				// will make a rectangle that is on the top
				g.fillRect(x, y, width, (height/4));
				break;
			}
			case DOWN:{
				// will make a rectangle that is on the bottom
				g.fillRect(x, y + (height*3)/4, width, (height/4));
				break;

			}
			case LEFT:{
				// will make a rectangle that is on left
				g.fillRect(x, y, width/4, height);
				break;

			}
			case RIGHT:{
				// will make rectangle that is on right
				g.fillRect(x + (width*3)/4, y, width/4, height);
				break;

			}

			}


		}
		// we dont want to have lines in rooms
		if (!(this.inRoom)){
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		}
	}

	public void drawRoomName(Graphics g, String roomName) {
		Board board = Board.getInstance();
		int width = board.getWidth() / board.getNumColumns();
		int height = board.getHeight() / board.getNumRows();
		// the x and y position of the rectangles will be shifted by each rectangles width and height
		// we divide by 2 to get the center of the cell
		int x = this.column * (width);
		int y = this.row * (height) + (height*3)/4;

		if (this.isLabel()) {
			g.setFont(new Font("TimesRoman", Font.BOLD, 15));
			g.setColor(Color.RED);
			g.drawString(roomName, x, y);
		}
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return this.column;
	}

	public char getInitial() {
		return initial;
	}

	public DoorDirection getDoorDirection() {
		return this.doorDirection;
	}

	public void setDoorDirection(DoorDirection d) {
		this.doorDirection = d;
	}

	public boolean isLabel() {
		return this.roomLabel;
	}

	public void setRoomLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}

	public boolean isRoomCenter() {
		return this.roomCenter;
	}

	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}

	public char getSecretPassage() {
		return this.secretPassage;
	}

	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	public boolean isOccupied() {
		return this.occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public boolean isRoom() {
		return this.inRoom;
	}

	public void setRoom(boolean partOfRoom) {
		this.inRoom = partOfRoom;
	}

	public boolean isDoorway() {
		return this.isDoorway;
	}

	public void setDoorway(boolean isDoorway) {
		this.isDoorway = isDoorway;
	}

	public boolean isPath() {
		return this.isPath;
	}

	public boolean isUnused() {
		return this.isUnused;
	}

	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}

	public void setUnused(boolean isUnused) {
		this.isUnused = isUnused;
	}

	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	public void addAdj(BoardCell adjCell) {
		adjList.add(adjCell);
	}

	public void removeAdj(BoardCell adjCell) {
		if (adjList.contains(adjCell)) {
			adjList.remove(adjCell);
		}
	}
}
