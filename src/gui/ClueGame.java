package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import clueGame.Board;

public class ClueGame extends JFrame{

	
	public ClueGame() {
		
	}
	
	
	public void gameLoop() {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.deal();
		add(board, BorderLayout.CENTER);
		setVisible(true);
		
		while (true) {
			
			
			
		
		}
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		ClueGame clueGame = new ClueGame();
		clueGame.setSize(800, 800);
		clueGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameCardPanel gameCardPanel = new GameCardPanel();
		GameControlPanel gameControlPanel = new GameControlPanel();
		gameCardPanel.setPreferredSize(new Dimension(clueGame.getWidth()/6, clueGame.getHeight()));
		
		
		clueGame.add(gameControlPanel, BorderLayout.SOUTH);
		clueGame.add(gameCardPanel, BorderLayout.EAST);
		
		clueGame.gameLoop();

	}

}
