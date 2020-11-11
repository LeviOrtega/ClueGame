package clueGame;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public abstract class Player {
	protected String name;
	protected Color color;
	protected Suggestion suggestion;
	protected Set<Card> hand;
	protected Set<Card> seen;
	protected PlayerType playerType;
	protected int row;
	protected int column;

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

	public abstract Card disproveSuggestion(Suggestion suggestion);

	public void updatePosition(int row, int column) {
		// set current boardCell to not occupied
		Board.getInstance().getCell(this.row, this.column).setOccupied(false);
		this.row = row;
		this.column = column;
		// set new boardCell to occupied
		Board.getInstance().getCell(this.row, this.column).setOccupied(true);

	}
	
	public void draw(Graphics g) {
		
	}

	public void updateHand(Card card) {
		hand.add(card);
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

}
