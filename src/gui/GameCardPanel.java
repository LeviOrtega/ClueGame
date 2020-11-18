package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import clueGame.Card;
import clueGame.CardType;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.PlayerType;

public class GameCardPanel extends JPanel {
	JPanel people;
	JPanel room;
	JPanel weapon;
	JPanel peopleHand;
	JPanel peopleSeen; 
	JPanel roomHand;
	JPanel roomSeen;
	JPanel weaponHand;
	JPanel weaponSeen;
	
	public GameCardPanel() {
		setLayout(new GridLayout(3,0));
		this.setBorder(new TitledBorder (new EtchedBorder(), "Known Cards"));
		JPanel panel = new JPanel();
		// create the three panels and add them to the gameCardPanel
		panel = createPeoplePanel();
		add(panel);
		panel = createRoomPanel();
		add(panel);
		panel = createWeaponPanel();
		add(panel);
	}
	
	public JPanel createPeoplePanel() {
		people = new JPanel();
		peopleHand = new JPanel();
		peopleSeen = new JPanel();
		
		people.setLayout(new GridLayout(2,0));
		people.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		// keep max number of objects in column to be 1 so when we do an add, we put it below 
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
		// ensuring space for room JPanel objects declared in GameCardPanel class
		room = new JPanel();
		roomHand = new JPanel();
		roomSeen = new JPanel();
		
		// "In Hand" and "Seen" panels will be displayed on top of each other in a row-type fashion
		room.setLayout(new GridLayout(2,0));
		room.setBorder(new TitledBorder (new EtchedBorder(), "Room"));
		// keep max number of objects in column to be 1 so when we do an odd, we put it below
		roomHand.setLayout(new GridLayout(0,1));
		roomSeen.setLayout(new GridLayout(0,1));
		
		// center label
		JLabel label = new JLabel("In Hand", SwingConstants.CENTER);
		roomHand.add(label);
		
		label = new JLabel("Seen", SwingConstants.CENTER);
		roomSeen.add(label);
		
		// add both the hand and seen to the room label
		room.add(roomHand);
		room.add(roomSeen);
		
		return room;
	}
	
	public JPanel createWeaponPanel() {
		weapon = new JPanel();
		weaponHand = new JPanel();
		weaponSeen = new JPanel();
		
		weapon.setLayout(new GridLayout(2,0));
		weapon.setBorder(new TitledBorder (new EtchedBorder(), "Weapon"));
		// giving each a gridlayout of (0,1) makes it so when we add to them, they add under 
		weaponHand.setLayout(new GridLayout(0,1));
		weaponSeen.setLayout(new GridLayout(0,1));
		
		JLabel label = new JLabel("In Hand", SwingConstants.CENTER);
		weaponHand.add(label);
		
		label = new JLabel("Seen", SwingConstants.CENTER);
		weaponSeen.add(label);
		
		// add both the hand labels and seen labels to the weapon label
		weapon.add(weaponHand);
		weapon.add(weaponSeen);
		
		return weapon;
	}
	
	
	public void addHand(Card card, Player mainPlayer) {
		JTextField cardText = new JTextField(card.getCardName());
		cardText.setEditable(false);
		// set the color of the card, eventually will be set to color of players
		cardText.setBackground(mainPlayer.getColor());
		switch (card.getCardType()) {
		case PEOPLE:{
			peopleHand.add(cardText);
			break;
		}
		case ROOM:{
			roomHand.add(cardText);
			break;
		}
		case WEAPON:{
			weaponHand.add(cardText);
			break;
		}
		}
	}
	
	public void addSeen(Card card, Player disprovePlayer) {
		JTextField cardText = new JTextField(card.getCardName());
		cardText.setEditable(false);
		// set the color of the card, eventually will be set to color of players
		cardText.setBackground(disprovePlayer.getColor());
		switch (card.getCardType()) {
		case PEOPLE:{
			peopleSeen.add(cardText);
			break;
		}
		case ROOM:{
			roomSeen.add(cardText);
			break;
		}
		case WEAPON:{
			weaponSeen.add(cardText);
			break;
		}
		}
		this.revalidate();
		this.repaint();
		
	}
 	

    public static void main(String[] args) {
    	GameCardPanel panel = new GameCardPanel();  // create the panel
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(panel);  // put the panel in the frame
        frame.setSize(180, 800);      // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
      
        // add cards to peopleHand and peopleSeen
        panel.addHand(new Card("P1", CardType.PEOPLE), null);
        panel.addHand(new Card("P2", CardType.PEOPLE), null);

        panel.addSeen(new Card("P3", CardType.PEOPLE), null);
        panel.addSeen(new Card("P4", CardType.PEOPLE), null);
        panel.addSeen(new Card("P5", CardType.PEOPLE), null);
        
        // add cards to roomHand and roomSeen
        panel.addHand(new Card("R1", CardType.ROOM), null);

        panel.addSeen(new Card("R2", CardType.ROOM), null);

        // add cards to weaponHand and weaponSeen
        panel.addHand(new Card("W2", CardType.WEAPON), null);
        panel.addHand(new Card("W1", CardType.WEAPON), null);
        panel.addHand(new Card("W3", CardType.WEAPON), null);
        
        panel.addSeen(new Card("W4", CardType.WEAPON), null);
        panel.addSeen(new Card("W5", CardType.WEAPON), null);
        
        frame.setVisible(true);       // make it visible
    }
}
