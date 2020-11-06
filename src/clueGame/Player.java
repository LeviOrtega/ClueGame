package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public abstract class Player {
	private String name;
	private Color color;
	private Set<Card> hand;
	private Suggestion suggestion;
	protected PlayerType playerType;
	protected int row;
	protected int column;
	
	public Player(String name, int row, int column) {
		this.name = name;
		this.row = row;
		this.column = column; 
		this.hand = new HashSet<>();
		this.suggestion = new Suggestion();
	}
	
	public abstract Suggestion createSuggestion();
	
	public abstract void selectTarget(int roll);
	
	public abstract PlayerType getPlayerType();
	
	public abstract Card disproveSuggestion(Suggestion suggestion);


	public void updateHand(Card card) {
		hand.add(card);
	}
	
	public void setSuggestion(Suggestion suggestion) {
		this.suggestion = suggestion;
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

	public void updatePosition(int row, int column) {
		Board.getInstance().getCell(this.row, this.column).setOccupied(false);
		this.row = row;
		this.column = column;
		Board.getInstance().getCell(this.row, this.column).setOccupied(true);

	}
	
	public Set<Card> getHand(){
		return this.hand;
	}
	
}
