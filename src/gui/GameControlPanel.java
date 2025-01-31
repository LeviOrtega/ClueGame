package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.PlayerType;

public class GameControlPanel extends JPanel {
	JTextField turn;
	JTextField roll;
	JTextField result;
	JTextField guess;
	JTextField song;
	JButton leftButton;
	JButton rightButton;
	Player player;
	ClueGame clueGame = ClueGame.getInstance();


	public GameControlPanel() {
		// ensure enough room for 2 items to be stored in grid, in this case two sub-panels
		// placed in the form of 2 rows with 0 columns
		setLayout(new GridLayout(2,0));
		
		JPanel panel = createTopPanel();
		add(panel);
		panel = createGuessPanel();
		add(panel);
		
	}

	public JPanel createTopPanel() {
		JPanel top = new JPanel();
		// have enough room for 4 items 
		top.setLayout(new GridLayout(1,3));

		JPanel turnSubPan = makeTurnPanel();
		// must have JLabel and JTextField with orientation -> JLabel (top), JTF (bottom)

		JPanel rollSubPan = makeRollPanel();

		makeButtons();
		JPanel musicSubPan = makeMusicPanel();
		
		top.add(turnSubPan);
		top.add(rollSubPan);
		top.add(leftButton);
		top.add(rightButton);
		top.add(musicSubPan);
		return top;
	}

	public JPanel makeTurnPanel() {
		JPanel leftSubPan = new JPanel();
		// we want the left sub panel to have 2 rows so panel stacks on text
		leftSubPan.setLayout(new GridLayout(2,0));
		// center the label 
		JLabel label = new JLabel("Whos turn?", SwingConstants.CENTER);
		leftSubPan.add(label);
		turn = new JTextField(15);
		turn.setHorizontalAlignment(JTextField.CENTER);
		// this makes it so players cannot type in text field
		turn.setEditable(false);
		leftSubPan.add(turn);
		return leftSubPan;
	}

	public JPanel makeRollPanel() {
		JLabel label;
		JPanel rightSubPan = new JPanel();
		// must have JLabel and JTextField with orientation -> JLabel (left), JTF (right)
		// have the room label right next to its textfield 
		label = new JLabel("Roll", SwingConstants.RIGHT);
		rightSubPan.add(label);
		roll = new JTextField(5);
		roll.setHorizontalAlignment(JTextField.CENTER);
		roll.setEditable(false);
		rightSubPan.add(roll);
		return rightSubPan;
	}

	public void makeButtons() {
		leftButton = new JButton("Make Accusation");
		rightButton = new JButton("Next!");

		rightButton.addActionListener(new NextListener());

		leftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AccusationPrompt acc = new AccusationPrompt();
				Player player = Board.getInstance().getCurrentPlayer();
				if (player.getPlayerType() == PlayerType.HUMAN && player.isFinishedTurn() == false) {
					acc.displayAccusationPrompt(player);
				}
				else {
					// display error 
					clueGame.displayErrorSplash("Cannot make accusation");
				}
			}

		});
	}

	public JPanel makeMusicPanel() {
		JPanel musicSubPan = new JPanel();
		musicSubPan.setLayout(new GridLayout(0,1));
		musicSubPan.setBorder(new TitledBorder (new EtchedBorder(), "Music"));
		
		JPanel pauseAndSongPanel = new JPanel();
		pauseAndSongPanel.setLayout(new GridLayout(0,2));
		JButton pause = new JButton("Mute");
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean paused = clueGame.getMusic().isPaused();
				if(paused) {
					// if music was paused, play resume it
					clueGame.getMusic().setPaused(false);
					clueGame.getMusic().resumeAudio();
					// set the button to display pause for player
					pause.setText("Pause");
				}
				else {
					clueGame.getMusic().setPaused(true);
					clueGame.getMusic().pause();
					pause.setText("Resume");
				}
				
			}

		});
		
		pause.setText("Pause");
		
		pauseAndSongPanel.add(pause);
		
		song = new JTextField(5);
		song.setEditable(false);
		//set 
		song.setBackground(new Color(220,220,220));
		pauseAndSongPanel.add(song);
		
		musicSubPan.add(pauseAndSongPanel);
		
		JButton nextSong = new JButton("Next Song");
		nextSong.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clueGame.getMusic().nextSong();
			}
			
		});
		musicSubPan.add(nextSong);
		return musicSubPan;
	}


	public JPanel createGuessPanel() {
		JPanel bottom = new JPanel();  // create the frame (serves as bottom guess panel)
		JPanel left = new JPanel();  
		JPanel right = new JPanel();

		bottom.setLayout(new GridLayout(0,2));  // Dimensions make room for a left (guess) and right (result) panel
		guess = new JTextField(20);   // maximum length of 20 characters for guess message
		guess.setEditable(false);     // user is not allowed to change text in guess message
		guess.setHorizontalAlignment(JTextField.CENTER);
		left.add(guess);
		result = new JTextField(20);  // maximum length of 20 characters for result message
		result.setEditable(false);    // user is not allowed to change text in result message
		result.setHorizontalAlignment(JTextField.CENTER);
		right.add(result);

		// both guess and result panels added to "bottom" as sort of sub-panels
		// the terms "Guess" and "Result" are etched 
		left.setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
		right.setBorder(new TitledBorder (new EtchedBorder(), "Result"));

		bottom.add(left);
		bottom.add(right);
		return bottom;
	}

	public void setTurn(Player player, int dieRoll) {
		this.player = player;
		// set our text fields for the player and roll
		turn.setText(player.getName());
		roll.setText(String.valueOf(dieRoll));
	}
	
	public void setSong() {
		song.setText("Song: " + clueGame.getMusic().getSongNumber());
	}

	public void setGuess(String playerGuess) {
		// set the guess text field
		guess.setText(playerGuess);
	}


	public void setResult(String playerResult) {
		// set the result text field
		result.setText(playerResult);
	}


	public JTextField getTurn() {
		return turn;
	}

	public JTextField getRoll() {
		return roll;
	}

	public JTextField getResult() {
		return result;
	}

	public JTextField getGuess() {
		return guess;
	}


	public JButton getLeftButton() {
		return leftButton;
	}

	public JButton getRightButton() {
		return rightButton;
	}

	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame
		frame.setContentPane(panel);  // put the panel in the frame
		frame.setSize(750, 180);      // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true);       // make it visible

		// test filling in the data
		panel.setTurn(new ComputerPlayer( "Col. Mustard", 0, 0, PlayerType.COMPUTER, null), 5);
		panel.setGuess( "I have no guess!");
		panel.setResult( "So you have nothing?");

	}
}
