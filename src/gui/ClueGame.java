package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.Player;
import clueGame.PlayerType;
import clueGame.Suggestion;

public class ClueGame extends JFrame{
	private static ClueGame theInstance = new ClueGame();
	private GameCardPanel gameCardPanel;
	private GameControlPanel gameControlPanel;


	public static ClueGame getInstance() {
		return theInstance;
	}

	private ClueGame() {}


	public void initialize() {
		setSize(900, 900);
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
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		// after all players are init and added, we want to set their colors and positions
		// we do this last as the players use boardcells to know where they are
		board.setPlayerInfo();
		board.deal();
		add(board, BorderLayout.CENTER);
		setVisible(true);

		board.updateCurrentPlayer();
	}

	public Suggestion displaySuggestionPrompt(Player player) {
		// called by human player to display the suggestion prompt to make suggestion
		return null;
	}

	public void updateGuessAndResult(Suggestion guess, Card result, Player suggestionPlayer, Player disprovePlayer) {
		// for JUnit tests we see check if its not null
		if (gameControlPanel != null) {
			String stringGuess = 
					guess.getPeople().getCardName() + "," +
					guess.getWeapon().getCardName() + "," +
					guess.getRoom().getCardName();
			gameControlPanel.getGuess().setBackground(suggestionPlayer.getColor());
			gameControlPanel.setGuess(stringGuess);
			if (result != null) {
				// set the color of the result text field to be color of player disproving suggestion
				gameControlPanel.getResult().setBackground(disprovePlayer.getColor());
				gameControlPanel.setResult(result.getCardName());

			}
			// if result IS null, then no player disproved any card in suggestion 
			else {
				System.out.println("He");
				gameControlPanel.setResult("Cannot be disproven!");
			}
		}
	}


	public void displayErrorSplash(String error) {
		JOptionPane.showMessageDialog(new JFrame(), 
				error,
				"Error", JOptionPane.INFORMATION_MESSAGE);
	}

	public void displayPlayerAndRoll(Player player, int roll) {
		// set the turn text box to correct player
		gameControlPanel.getTurn().setBackground(player.getColor());
		gameControlPanel.setTurn(player, roll);
		
	}

	public void clearGuessAndResult() {
		gameControlPanel.getGuess().setBackground(Color.white);
		gameControlPanel.getResult().setBackground(Color.white);
		gameControlPanel.setGuess("");
		gameControlPanel.setResult("");

	}
	
	public void displayNewHand(Card card, Player player) {
		gameCardPanel.addHand(card, player);
	}
	
	public void displayNewSeen(Card card, Player player) {
		gameCardPanel.addSeen(card, player);
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
		clueGame.initialize();
		// display a welcome splash
		clueGame.welcomeSplash();
	}




}
