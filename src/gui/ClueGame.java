package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import clueGame.Board;
import clueGame.Player;
import clueGame.PlayerType;

public class ClueGame extends JFrame{
	private static ClueGame theInstance = new ClueGame();
	private GameCardPanel gameCardPanel;
	private GameControlPanel gameControlPanel;
	private Player currentPlayer;
	private static int currentPlayerIndex;
	private Board board = Board.getInstance();

	public static ClueGame getInstance() {
		return theInstance;
	}
	
	private ClueGame() {}


	public void initialize() {
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ClueGame CSCI 306");
		gameCardPanel = new GameCardPanel();
		gameControlPanel = new GameControlPanel();
		gameCardPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));

		
		// we use indexes of the players arraylist to loop through the players in the game
		// the first player in the arrayList is the cowboy, the human player
		currentPlayerIndex = 0;
		add(gameControlPanel, BorderLayout.SOUTH);
		add(gameCardPanel, BorderLayout.EAST);
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.deal();
		// after all players are init and added, we want to set their colors and positions
		// we do this last as the players use boardcells to know where they are
		board.setPlayerInfo();
		add(board, BorderLayout.CENTER);
		setVisible(true);
		
		handlePlayerLogic();
	}


	public void handlePlayerLogic() {
		currentPlayer = board.getPlayers().get(currentPlayerIndex);
		// set the turn text box to correct player
		gameControlPanel.getTurn().setBackground(currentPlayer.getColor());
		gameControlPanel.getTurn().setText(currentPlayer.getName());
		int roll = rollDie();
		System.out.println(roll);
		gameControlPanel.getRoll().setText(String.valueOf(roll));
	
	}
	
	// return true if we can move to next player
	public boolean checkIfCanMoveOn() {
		// if the player is a computer, it will have done what it needs to do and we can move on
		if (currentPlayer.getPlayerType() == PlayerType.COMPUTER) {
			return true;
		}
		// else player is human and need to check if they finished their turn which is triggered after target is selected
		return !currentPlayer.isFinishedTurn();
	}
	
	public int rollDie() {
		return (int)(Math.random()*6 + 1);
	}



	public static int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public static void setCurrentPlayerIndex(int currentPlayerIndex) {
		ClueGame.currentPlayerIndex = currentPlayerIndex;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public static void main(String[] args) {
		ClueGame clueGame = ClueGame.getInstance();
		clueGame.setSize(1000, 800);
		clueGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clueGame.setTitle("ClueGame CSCI 306");
		GameCardPanel gameCardPanel = new GameCardPanel();
		GameControlPanel gameControlPanel = new GameControlPanel();
		gameCardPanel.setPreferredSize(new Dimension(clueGame.getWidth()/6, clueGame.getHeight()));


		clueGame.add(gameControlPanel, BorderLayout.SOUTH);
		clueGame.add(gameCardPanel, BorderLayout.EAST);

		clueGame.initialize();
	}


	

}
