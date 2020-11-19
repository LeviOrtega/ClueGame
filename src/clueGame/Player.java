package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import gui.ClueGame;

public abstract class Player {
	protected String name;
	protected Color color;
	protected Suggestion suggestion;
	protected Set<Card> hand;
	protected Set<Card> seen;
	protected PlayerType playerType;
	protected int row;
	protected int column;
	protected boolean finishedTurn;
	protected boolean testing;
	protected Solution accusation;
	protected Room previousRoom;

	public Player(String name, int row, int column, PlayerType playerType, Color color) {
		this.name = name;
		this.row = row;
		this.column = column; 
		this.playerType = playerType;
		this.color = color;
		this.hand = new HashSet<>();
		this.seen = new HashSet<>();
		this.suggestion = new Suggestion();
	}

	public abstract Suggestion createSuggestion();

	public abstract void selectTarget(int roll);

	public Card disproveSuggestion(Suggestion suggestion){
		ArrayList<Card> disprovingCards = new ArrayList<>();
		// find all cards in hand that suggestion has and add it do a list of possible disproving cards
		if (this.hand.contains(suggestion.getPeople())) {
			disprovingCards.add(suggestion.getPeople());
		}
		if (this.hand.contains(suggestion.getWeapon())) {
			disprovingCards.add(suggestion.getWeapon());
		}
		if (this.hand.contains(suggestion.getRoom())) {
			disprovingCards.add(suggestion.getRoom());
		}

		if (disprovingCards.size() == 0) {
			return null;
		}

		Collections.shuffle(disprovingCards);
		return disprovingCards.get(0);
	}

	public void updatePosition(int row, int column) {
		// set current boardCell to not occupied
		Board.getInstance().getCell(this.row, this.column).setOccupied(false);
		this.row = row;
		this.column = column;

		// set new boardCell to occupied
		Board.getInstance().getCell(this.row, this.column).setOccupied(true);
		// after the players position has been updated, repaint
		Board.getInstance().repaint();
	}

	public abstract void checkIfPlayerShouldHandleSuggestion();

	public void draw(Graphics g) {
		Board board = Board.getInstance();
		int width = board.getWidth() / board.getNumColumns();
		int height = board.getHeight() / board.getNumRows();

		// having x and y multiplied gives us the adjustments for width and height
		int x = this.column * (width);
		int y = this.row * (height);
		g.setColor(this.color);

		// we use an oval to display players
		g.fillOval(x, y, width, height);
		g.setColor(Color.BLACK);
		Graphics2D g2 = (Graphics2D) g;
		int thickness = 4;
	    g2.setStroke(new BasicStroke(thickness));
		g2.drawOval(x+(thickness/2), y+(thickness/2), width-thickness, height-thickness);

	}

	public void updateHand(Card card) {
		hand.add(card);
		// the computer checks all cards its seen before when making suggestions
		if (playerType == PlayerType.COMPUTER) {
			seen.add(card);
		}
		// we only want to display the hand and seen if its a player
		if (testing == false && playerType == PlayerType.HUMAN) {
			ClueGame.getInstance().displayNewHand(card, this);	
		}
	}

	// called from a disproved card
	public void updateSeen(Card card, Player disprovePlayer) {
		if (testing == false && playerType == PlayerType.HUMAN) {
			if (!(seen.contains(card))) {
				// we don't want to add the same card that has been disproven. Humans can submit a card that has been disproven
				ClueGame.getInstance().displayNewSeen(card, disprovePlayer);
			}
		}

		seen.add(card);

	}


	public PlayerType getPlayerType() {
		return this.playerType;
	}

	public void setSuggestion(Suggestion suggestion) {
		this.suggestion = suggestion;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Suggestion getSuggestion() {
		return this.suggestion;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Color getColor() {
		return this.color;
	}

	public Set<Card> getHand(){
		return this.hand;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", color=" + color + ", playerType=" + playerType + ", row=" + row + ", column="
				+ column + "]";
	}

	public boolean isFinishedTurn() {
		return finishedTurn;
	}

	public void setFinishedTurn(boolean finishedTurn) {
		this.finishedTurn = finishedTurn;
	}

	public void setTesting(Boolean test) {
		// for testing shuffling and to not call null values not setup in JUnit tests
		testing = test;
	}
	
	public Room getPreviousRoom() {
		return this.previousRoom;
	}
	
	public void setPreviousRoom(Room room) {
		this.previousRoom = room;
	}
	



}
