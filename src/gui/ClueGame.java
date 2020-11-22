package gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import audio.Music;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.Player;
import clueGame.PlayerType;
import clueGame.Solution;
import clueGame.Suggestion;

public class ClueGame extends JFrame{
	private static ClueGame theInstance = new ClueGame();
	private static Board board = Board.getInstance();
	private static Music music;
	private GameCardPanel gameCardPanel;
	private GameControlPanel gameControlPanel;



	public static ClueGame getInstance() {
		return theInstance;
	}

	private ClueGame() {}


	public void initialize() {
		setSize(820, 870);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ClueGame CSCI 306");
		addMouseListener(new MouseBoard());
		gameCardPanel = new GameCardPanel();
		music = new Music();
		gameControlPanel = new GameControlPanel();
		gameControlPanel.setPreferredSize(new Dimension(getWidth(), getHeight()/6));
		music.pickRandomSong();
		music.initialize();
		gameCardPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));


		// we use indexes of the players arraylist to loop through the players in the game
		// the first player in the arrayList is the cowboy, the human player
		add(gameControlPanel, BorderLayout.SOUTH);
		add(gameCardPanel, BorderLayout.EAST);

		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		// after all players are init and added, we want to set their colors and positions
		// we do this last as the players use boardcells to know where they are
		board.setPlayerInfo();
		board.deal();

		add(board);
		setVisible(true);

		board.updateCurrentPlayer();


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
				// only display disproved card if its the human
				if (suggestionPlayer.getPlayerType() == PlayerType.HUMAN) {
					// set the color of the result text field to be color of player disproving suggestion
					gameControlPanel.setResult(result.getCardName());
				}
				else {
					gameControlPanel.setResult("A card has been disproven.");
				}
				// always show who disproved it
				gameControlPanel.getResult().setBackground(disprovePlayer.getColor());
			}
			// if result IS null, then no player disproved any card in suggestion 
			else {
				gameControlPanel.setResult("Cannot be disproven!");
			}
		}
	}


	public void displayVictoryScreen(Player winner) {
		JFrame victoryFrame = new JFrame();
		victoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		victoryFrame.setSize(300, 100);
		victoryFrame.setLocationRelativeTo(null);
		victoryFrame.setLayout(new GridLayout(0,1));
		JLabel wLabel = new JLabel(winner.getName() + " has won!", SwingConstants.CENTER);
		Solution answer = board.getAnswer();
		JLabel solution = new JLabel("Answer: "  
				+ answer.getPeople().getCardName() + ", "
				+ answer.getRoom().getCardName() + ", "
				+ answer.getWeapon().getCardName()
				, SwingConstants.CENTER
				);

		victoryFrame.add(wLabel);
		victoryFrame.add(solution);
		
		if (winner.getPlayerType() == PlayerType.HUMAN) {
		victoryFrame.setTitle("Congrats!");
		}
		else {
			victoryFrame.setTitle("Aw Shucks");
		}
		
		
		victoryFrame.getContentPane().setBackground(winner.getColor());
		victoryFrame.setVisible(true);
	}

	public void displayLoseScreen() {
		JFrame loseFrame = new JFrame();
		loseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loseFrame.setSize(400, 100);
		loseFrame.setLocationRelativeTo(null);
		JLabel lLabel = new JLabel("You've lost. Maybe next time, partner.", SwingConstants.CENTER);
		loseFrame.add(lLabel);
		loseFrame.setTitle("You lost!");
		loseFrame.setVisible(true);
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

	public Music getMusic() {
		return this.music;
	}

	public void welcomeSplash() {
		JOptionPane.showMessageDialog(new JFrame(), 
				"You are the Cowboy" +
						"\nCan you find the solution" +
						"\nbefore the computer players?",
						"Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
	}





	public GameControlPanel getGameControlPanel() {
		return gameControlPanel;
	}

	public static void main(String[] args) {

		ClueGame clueGame = ClueGame.getInstance();
		clueGame.initialize();

		// display a welcome splash
		clueGame.welcomeSplash();

		System.out.println(board.getAnswer().toString());
	}




}
