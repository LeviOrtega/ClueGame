package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.ComputerPlayer;
import clueGame.PlayerType;

public class GameCardPanel extends JPanel{

	
	
	public GameCardPanel() {
		
		//setBorder(new TitledBorder (new EtchedBorder(), ""));
		setLayout(new GridLayout(3,0));
	}
	
	public JPanel createPeoplePanel() {
		
	}
	
	public JPanel createRoomPanel() {
		
	}
	
	public JPanel createWeaponPanel() {
		
	}
 	

    public static void main(String[] args) {
    	GameCardPanel panel = new GameCardPanel();  // create the panel
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(panel);  // put the panel in the frame
        frame.setSize(180, 800);      // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
        frame.setVisible(true);       // make it visible

    }
}
