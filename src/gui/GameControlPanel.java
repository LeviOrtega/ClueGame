package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameControlPanel extends JPanel{
	JTextField turn;
	JTextField roll;
	JTextField result;
	JTextField guess;
	JButton leftButton;
	JButton rightButton;
	
	
	public GameControlPanel() {
		setLayout(new GridLayout(2,0));
		JPanel panel = createTopPanel();
		add(panel);
		panel = createGuessPanel();
		//add(panel);
		
	}
	
	public JPanel createTopPanel() {
		JPanel top = new JPanel();
		// have enough room for 4 items 
		top.setLayout(new GridLayout(1,4));
		
		JPanel leftSubPan = new JPanel();
		// we want the left sub panel to have 2 rows so panel stacks on text
		leftSubPan.setLayout(new GridLayout(2,0));
		
		JLabel label = new JLabel("Whos turn?");
		leftSubPan.add(label);
		turn = new JTextField(15);
		// this makes it so players cannot type in text field
		turn.setEditable(false);
		leftSubPan.add(turn);
		// must have JLabel and JTextField with orientation -> JLabel (top), JTF (bottom)
		
		JPanel rightSubPan = new JPanel();
		// must have JLabel and JTextField with orientation -> JLabel (left), JTF (right)
		rightSubPan.setLayout(new GridLayout(0,2));
		label = new JLabel("Roll");
		rightSubPan.add(label);
		
		
		roll = new JTextField(5);
		roll.setEditable(false);
		rightSubPan.add(roll);
		
		leftButton = new JButton("Make Accusation");
		rightButton = new JButton("Next!");
		
		top.add(leftSubPan);
		top.add(rightSubPan);
		top.add(leftButton);
		top.add(rightButton);
		return top;
	}
	
	public JPanel createGuessPanel() {
		return null;
	}

    public static void main(String[] args) {
           GameControlPanel panel = new GameControlPanel();  // create the panel
           JFrame frame = new JFrame();  // create the frame
           frame.setContentPane(panel); // put the panel in the frame
           frame.setSize(750, 180);  // size the frame
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
           frame.setVisible(true); // make it visible

           // test filling in the data
          // panel.setTurn(new ComputerPlayer( "Col. Mustard", 0, 0), 5);
           //panel.setGuess( "I have no guess!");
          // panel.setGuessResult( "So you have nothing?");
    }
}
