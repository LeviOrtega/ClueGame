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
import clueGame.Suggestion;

public class SuggestionPrompt extends JDialog{
	JComboBox peopleBox;
	JComboBox weaponBox;
	private Suggestion suggestion;

	private Board board = Board.getInstance();


	public SuggestionPrompt() {
		suggestion = new Suggestion();
	}
	// called by human player to display the suggestion prompt to make suggestion
	public void displaySuggestionPrompt(Player player) {
		setSize(300,150);
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
				// send suggestion to handleSuggestion 
				player.setFinishedTurn(true);
				player.setSuggestion(suggestion);
				Board.getInstance().handleSuggestion(player);
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
		suggestion.setWeapon(weaponCards[0]);
		weaponBox.setSelectedIndex(0);
		weaponBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				//give suggestion the 
				suggestion.setWeapon(weaponCards[weaponBox.getSelectedIndex()]);

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
		suggestion.setPeople(peopleCards[0]);
		peopleBox.setSelectedIndex(0);
		peopleBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				//give suggestion the 
				suggestion.setPeople(peopleCards[peopleBox.getSelectedIndex()]);

			}
		});

		panel2.add(peopleBox);
		return panel2;
	}
	public JPanel makePanel1(Player player) {
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(0,2));

		// telling player which room they're in
		JLabel label = new JLabel("Current Room:");
		panel1.add(label);

		// current room
		BoardCell playerCell = board.getCell(player.getRow(), player.getColumn());
		Room room = board.getRoom(playerCell);
		String roomName = room.getName();
		Card roomCard = room.getRoomCard();

		suggestion.setRoom(roomCard);

		label = new JLabel(roomName);
		panel1.add(label);
		return panel1;
	}

	public Suggestion getSuggestion() {
		return this.suggestion;
	}


}
