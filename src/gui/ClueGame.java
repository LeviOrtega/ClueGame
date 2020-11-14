package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import clueGame.Board;
import clueGame.BoardCell;
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
		addMouseListener(new MouseBoard());
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
		// give the player the roll to calc targets
		gameControlPanel.getRoll().setText(String.valueOf(roll));
		currentPlayer.selectTarget(roll);


		board.repaint();
	}
	
	// given a point from the board being clicked on, determine what to do
	public void handleBoardClickLogic(Point point) {
		if (currentPlayer.getPlayerType() != PlayerType.HUMAN) {
			//error box
			return;
		}
		
		// get cell at the point of clicking
		int offX = point.x % (BoardCell.getWidth());
		int offY = point.y % (BoardCell.getHeight());
		// when we click on the board, we wont always click directly on the cells orgin
		// so we have to subtract the distance from the orgin of the cell to find it
		int x = (point.x - offX) / BoardCell.getWidth();
		int y = (point.y - offY) / BoardCell.getHeight() - 1;
		// if the player clicks off of the board, handle the exception
		try {
		BoardCell pointCell = board.getCell(y, x);
		// if the player is human, and they selected a target cell, they finished their turn
		validateTargetSelection(pointCell);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Clicked out of frame");
		}
		//System.out.println(pointCell);
		
	}
	
	public void validateTargetSelection(BoardCell pointCell) {
		if (board.getTargets().contains(pointCell)) {
			// move the player to the point if its within the targets
			currentPlayer.updatePosition(pointCell.getRow(), pointCell.getColumn());
			// clear the targets and repaint the board to remove colored floors
			// targets will be recalculated in next player
			board.getTargets().clear();
			board.repaint();
			if (pointCell.isRoomCenter()) {
				// handle logic for suggestions then set finished to true
			}
			else {
			currentPlayer.setFinishedTurn(true);
			}
			
		}
	}
	
	// called when nexButton is clicked on
	public void iteratePlayerIndex() {
		if (checkIfCanMoveOn()) {
			currentPlayerIndex++;
			currentPlayerIndex %= Board.getInstance().getPlayers().size();
			// bound the index by the size of players
			handlePlayerLogic();
		}
		// else throw error
	}

	// return true if we can move to next player
	public boolean checkIfCanMoveOn() {
		// if the player is a computer, it will have done what it needs to do and we can move on
		if (currentPlayer.getPlayerType() == PlayerType.COMPUTER) {
			return true;
		}
		// else player is human and need to check if they finished their turn which is triggered after target is selected
		return currentPlayer.isFinishedTurn();
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
