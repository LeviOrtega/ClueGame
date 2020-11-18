package gui;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.Room;
import clueGame.Solution;
import clueGame.Suggestion;

public class AccusationPrompt extends JDialog{
	JComboBox peopleBox;
	JComboBox weaponBox;
	JComboBox roomBox;
	private Solution solution;

	private Board board = Board.getInstance();


	public AccusationPrompt() {
		solution = new Solution();
	}
	// called by the action preformed in accusation button
	public void displayAccusationPrompt(Player player) {
		setSize(400,200);
		setLocationRelativeTo(null);
		//suggestion.setLayout(new GridLayout(4,2));

		JPanel main = new JPanel();
		main.setLayout(new GridLayout(4,2));

		JPanel panel1 = makePanel1(player);
		JPanel panel2 = makePanel2();
		JPanel panel3 = makePanel3();
		JPanel panel4 = makePanel4(player);
		
		
		
		main.add(panel1);
		main.add(panel2);
		main.add(panel3);
		main.add(panel4);

		add(main);
		setVisible(true);

	}

	public JPanel makePanel4(Player player) {
		JPanel panel4 = new JPanel();
		
		JButton doneButton = new JButton("Done");
		JButton cancelButton = new JButton("Cancel");
		
		doneButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.setFinishedTurn(true);
				Boolean win = Board.getInstance().makeAccusation(solution);
				if (win) {
					ClueGame.getInstance().displayVictoryScreen(player);
				}
				else {
					ClueGame.getInstance().displayLoseScreen();
				}
				dispose();
			}
			
		});
		
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.setFinishedTurn(true);
				dispose();
			}
			
		});
		
		panel4.add(doneButton);
		panel4.add(cancelButton);
		return panel4;
		
	}

	public JPanel makePanel3() {
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(0,2));

		JLabel label3 = new JLabel("Weapon:");
		panel3.add(label3);

		Card[] weaponCards = board.getTypeCards(CardType.WEAPON);
		weaponBox = new JComboBox(weaponCards);
		// set default selection to be the first item
		solution.setWeapon(weaponCards[0]);
		weaponBox.setSelectedIndex(0);
		weaponBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				//give suggestion the 
				solution.setWeapon(weaponCards[weaponBox.getSelectedIndex()]);
				
			}
		});

		panel3.add(weaponBox);
		return panel3;
	}
	public JPanel makePanel2() {
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(0,2));

		JLabel label2 = new JLabel("Person:");
		panel2.add(label2);

		Card[] peopleCards = board.getTypeCards(CardType.PEOPLE);
		peopleBox = new JComboBox(peopleCards);
		// set default selection to be the first item
		solution.setPeople(peopleCards[0]);
		peopleBox.setSelectedIndex(0);
		peopleBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				//give suggestion the 
				solution.setPeople(peopleCards[peopleBox.getSelectedIndex()]);

			}
		});

		panel2.add(peopleBox);
		return panel2;
	}
	public JPanel makePanel1(Player player) {
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(0,2));

		JLabel label1 = new JLabel("Person:");
		panel1.add(label1);

		Card[] roomCards = board.getTypeCards(CardType.ROOM);
		roomBox = new JComboBox(roomCards);
		// set default selection to be the first item
		solution.setRoom(roomCards[0]);
		roomBox.setSelectedIndex(0);
		roomBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				//give suggestion the 
				solution.setRoom(roomCards[roomBox.getSelectedIndex()]);

			}
		});

		panel1.add(roomBox);
		return panel1;
	}

	public Solution getSolution() {
		return this.solution;
	}
	
	
}
