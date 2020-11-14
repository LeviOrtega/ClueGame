package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Player;
import clueGame.PlayerType;

public class ClueGame extends JFrame{
	private static ClueGame theInstance = new ClueGame();
	private Board board = Board.getInstance();
	private GameCardPanel gameCardPanel;
	private GameControlPanel gameControlPanel;


	public static ClueGame getInstance() {
		return theInstance;
	}

	private ClueGame() {}


	public void initialize() {
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ClueGame CSCI 306");
		addMouseListener(new MouseBoard());
		gameCardPanel = new GameCardPanel();
		gameControlPanel = new GameControlPanel();
		gameCardPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));


		// we use indexes of the players arraylist to loop through the players in the game
		// the first player in the arrayList is the cowboy, the human player
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

		board.updateCurrentPlayer();
	}



	public void displayErrorSplash(String error) {
		JOptionPane.showMessageDialog(new JFrame(), 
				error,
				"Error", JOptionPane.INFORMATION_MESSAGE);
	}

	public void displayPlayerAndRoll(Player player, int roll) {
		// set the turn text box to correct player
		gameControlPanel.getTurn().setBackground(player.getColor());
		gameControlPanel.getTurn().setText(player.getName());

		// give the player the roll to calc targets
		gameControlPanel.getRoll().setText(String.valueOf(roll));

	}



	public void welcomeSplash() {
		JOptionPane.showMessageDialog(new JFrame(), 
				"You are the Cowboy" +
						"\nCan you find the solution" +
						"\nbefore the computer players?",
						"Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
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
		// display a welcome splash
		clueGame.welcomeSplash();
	}




}
