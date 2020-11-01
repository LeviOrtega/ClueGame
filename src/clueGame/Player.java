package clueGame;

import java.awt.Color;

public abstract class Player {
	private String name;
	private Color color;
	protected int row;
	protected int column;
	
	public Player(String name, int row, int column) {
		this.name = name;
		this.row = row;
		this.column = column; 
	}
	



	public void updateHand(Card card) {
		
	}
}
