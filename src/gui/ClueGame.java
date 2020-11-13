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

public class ClueGame extends JFrame{
	private GameCardPanel gameCardPanel;
	private GameControlPanel gameControlPanel;
	private Player currentPlayer;
	private static int currentPlayerIndex;
	private Board board = Board.getInstance();

	public ClueGame() {
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ClueGame CSCI 306");
		gameCardPanel = new GameCardPanel();
		gameControlPanel = new GameControlPanel();
		gameCardPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));

		
		// we use indexes of the players arraylist to loop through the players in the game
		// the first player in the arrayList is the cowboy, the human player
		currentPlayerIndex = 0;
		
	}


	public void gameLoop() {
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
		currentPlayer = board.getPlayers().get(currentPlayerIndex);
		gameControlPanel.getTurn().setBackground(currentPlayer.getColor());
		gameControlPanel.getTurn().setText(currentPlayer.getName());
		gameControlPanel.getTurn().setDisabledTextColor((Color.WHITE));
		/*gameControlPanel.getRightButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentPlayerIndex++;
				currentPlayerIndex %= board.getPlayers().size();
				System.out.println(currentPlayerIndex);
			}
		});*/
		
		

	}





	public static void main(String[] args) {
		ClueGame clueGame = new ClueGame();
		clueGame.setSize(1000, 800);
		clueGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clueGame.setTitle("ClueGame CSCI 306");
		GameCardPanel gameCardPanel = new GameCardPanel();
		GameControlPanel gameControlPanel = new GameControlPanel();
		gameCardPanel.setPreferredSize(new Dimension(clueGame.getWidth()/6, clueGame.getHeight()));


		clueGame.add(gameControlPanel, BorderLayout.SOUTH);
		clueGame.add(gameCardPanel, BorderLayout.EAST);

		clueGame.gameLoop();
	}

}
