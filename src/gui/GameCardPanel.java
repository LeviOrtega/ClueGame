package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.PlayerType;

public class GameCardPanel extends JPanel {
	JPanel people;
	JPanel peopleHand;
	JPanel peopleSeen; 
	JPanel roomHand;
	JPanel roomSeen;
	JPanel weaponHand;
	JPanel weaponSeen;
	
	public GameCardPanel() {
		
		//setBorder(new TitledBorder (new EtchedBorder(), ""));
		setLayout(new GridLayout(3,0));
		this.setBorder(new TitledBorder (new EtchedBorder(), "Known Cards"));
		JPanel panel = new JPanel();
		panel = createPeoplePanel();
		add(panel);
		panel = createRoomPanel();
		//add(panel);
		panel = createWeaponPanel();
		//add(panel);
	}
	
	public JPanel createPeoplePanel() {
		people = new JPanel();
		peopleHand = new JPanel();
		peopleSeen = new JPanel();
		
		people.setLayout(new GridLayout(2,0));
		people.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		// keep max number of objects in col to be 1 so when we do an add, we put it below 
		peopleHand.setLayout(new GridLayout(0,1));
		peopleSeen.setLayout(new GridLayout(0,1));
		
		// center label
		JLabel label = new JLabel("In Hand", SwingConstants.CENTER);
		peopleHand.add(label);
		
		label = new JLabel("Seen", SwingConstants.CENTER);
		peopleSeen.add(label);
		
		// add both the hand and seen to the people label
		people.add(peopleHand);
		people.add(peopleSeen);
		
		return people;
	}
	
	public JPanel createRoomPanel() {
		JPanel room = new JPanel();
		roomHand = new JPanel();
		roomSeen = new JPanel();
		
		room.setLayout(new GridLayout(2,0));
		room.setBorder(new TitledBorder (new EtchedBorder(), "Room"));
		// keep max number of objects in col to be 1 so when we do an odd, we put it below
		
		
		return null;
	}
	
	public JPanel createWeaponPanel() {
		return null;
	}
	
	public void addPeopleHand(Player player) {
		JTextField playerText = new JTextField(player.getName());
		playerText.setEditable(false);
		peopleHand.add(playerText);
	}
	
	public void addPeopleSeen(Player player) {
		JTextField playerText = new JTextField(player.getName());
		playerText.setEditable(false);
		peopleSeen.add(playerText);
	}
 	

    public static void main(String[] args) {
    	GameCardPanel panel = new GameCardPanel();  // create the panel
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(panel);  // put the panel in the frame
        frame.setSize(180, 800);      // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
      

        panel.addPeopleHand(new ComputerPlayer("P1", 0,0, PlayerType.COMPUTER));
        panel.addPeopleHand(new ComputerPlayer("P2", 0,0, PlayerType.COMPUTER));

        panel.addPeopleSeen(new ComputerPlayer("P3", 0,0, PlayerType.COMPUTER));
        panel.addPeopleSeen(new ComputerPlayer("P4", 0,0, PlayerType.COMPUTER));
        panel.addPeopleSeen(new ComputerPlayer("P5", 0,0, PlayerType.COMPUTER));



        
        
        
        frame.setVisible(true);       // make it visible
        
    }
}
