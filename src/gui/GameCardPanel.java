package gui;

import java.awt.BorderLayout;
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
		
		//setBorder(new TitledBorder (new EtchedBorder(), ""));
		setLayout(new GridLayout(3,0));
		this.setBorder(new TitledBorder (new EtchedBorder(), "Known Cards"));
		JPanel panel = new JPanel();
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
		room = new JPanel();
		roomHand = new JPanel();
		roomSeen = new JPanel();
		
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
	
	public void addPeopleHand(Card card) {
		JTextField cardText = new JTextField(card.getCardName());
		cardText.setEditable(false);
		peopleHand.add(cardText);
	}
	
	public void addPeopleSeen(Card card) {
		JTextField cardText = new JTextField(card.getCardName());
		cardText.setEditable(false);
		peopleSeen.add(cardText);
	}
	
	public void addRoomHand(Card card) {
		JTextField cardText = new JTextField(card.getCardName());
		cardText.setEditable(false);
		roomHand.add(cardText);
	}
	
	public void addRoomSeen(Card card) {
		JTextField cardText = new JTextField(card.getCardName());
		cardText.setEditable(false);
		roomSeen.add(cardText);
	}
	
	public void addWeaponHand(Card card) {
		JTextField cardText = new JTextField(card.getCardName());
		cardText.setEditable(false);
		weaponHand.add(cardText);
	}
	
	public void addWeaponSeen(Card card) {
		JTextField cardText = new JTextField(card.getCardName());
		cardText.setEditable(false);
		weaponSeen.add(cardText);
	}
 	

    public static void main(String[] args) {
    	GameCardPanel panel = new GameCardPanel();  // create the panel
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(panel);  // put the panel in the frame
        frame.setSize(180, 800);      // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
      

        panel.addPeopleHand(new Card("P1", CardType.PEOPLE));
        panel.addPeopleHand(new Card("P2", CardType.PEOPLE));

        panel.addPeopleSeen(new Card("P3", CardType.PEOPLE));
        panel.addPeopleSeen(new Card("P4", CardType.PEOPLE));
        panel.addPeopleSeen(new Card("P5", CardType.PEOPLE));
        
        
        panel.addRoomHand(new Card("R1", CardType.ROOM));

        panel.addRoomSeen(new Card("R2", CardType.ROOM));

        
        panel.addWeaponHand(new Card("W2", CardType.WEAPON));
        panel.addWeaponHand(new Card("W1", CardType.WEAPON));
        panel.addWeaponHand(new Card("W3", CardType.WEAPON));
        
        panel.addWeaponSeen(new Card("W4", CardType.WEAPON));
        panel.addWeaponSeen(new Card("W5", CardType.WEAPON));
      
        

        
        
        
        frame.setVisible(true);       // make it visible
        
    }
}
