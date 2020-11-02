package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public abstract class Player {
	private String name;
	private Color color;
	private Set<Card> hand;
	protected PlayerType playerType;
	protected int row;
	protected int column;
	
	public Player(String name, int row, int column) {
		this.name = name;
		this.row = row;
		this.column = column; 
		this.hand = new HashSet<>();
	}
	
	public abstract PlayerType getPlayerType();


	public void updateHand(Card card) {
		hand.add(card);
	}
	
	public void updatePosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public Set<Card> getHand(){
		return this.hand;
	}
	
}
