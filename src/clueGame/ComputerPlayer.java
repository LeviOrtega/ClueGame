package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import gui.ClueGame;

public class ComputerPlayer extends Player {


	public ComputerPlayer(String name, int row, int column, PlayerType playerType, Color color) {
		super(name, row, column, playerType, color);
	}

	@Override
	public void selectTarget(int roll) {
		// this is the first funciton called for each player
		// check if previous turn a solution was found, if so, check if it was right

		if (accusation != null) {
			if (Board.getInstance().makeAccusation(accusation)) {
				ClueGame.getInstance().displayVictoryScreen(this);
			}
		}
		// else continue on with calculating targets
		else {

			// get player cell
			BoardCell playerCell = Board.getInstance().getCell(this.row, this.column);
			Board.getInstance().calcTargets(playerCell, roll);
			Set<BoardCell> targets = Board.getInstance().getTargets();
			ArrayList<BoardCell> rooms = new ArrayList<BoardCell>();
			ArrayList<BoardCell> spaces = new ArrayList<BoardCell>();
			// loop through all targets at the players starting location and determine the rooms and spaces they can move to
			for (BoardCell target: targets) {
				// set all targets to be blue
				if (target.isRoomCenter()) {
					rooms.add(target);
				}
				else spaces.add(target);
			}
			BoardCell newLocation;
			// if we have rooms to go to, choose randomly
			if (rooms.size() != 0) {
				Collections.shuffle(rooms);
				newLocation = rooms.get(0);
				this.updatePosition(newLocation.getRow(), newLocation.getColumn());
				// set the previous room of the player to the room they enter by their choice
				previousRoom = Board.getInstance().getRoom(newLocation);
				// if the computer is in a room, they should handle a suggestion
				checkIfPlayerShouldHandleSuggestion();
			}
			// else choose random space
			else if (spaces.size() != 0){
				Collections.shuffle(spaces);
				newLocation = spaces.get(0);
				this.updatePosition(newLocation.getRow(), newLocation.getColumn());
				// reset their previous room
				previousRoom = null;
			}

		}
	}



	@Override
	public Suggestion createSuggestion() {
		BoardCell playerCell = Board.getInstance().getCell(this.row, this.column);
		// if the player isn't in a roomCenter, they cant make a suggestion
		if (!(playerCell.isRoomCenter())) {
			return null;
		}
		Card room = Board.getInstance().getRoom(playerCell).getRoomCard();
		Card weapon = null;
		Card people = null;

		ArrayList<Card> deck = Board.getInstance().getDeck();
		// shuffle deck so that computers suggest different cards
		for (Card card: deck) {
			// if player has seen card, dont suggest it
			if (!(this.seen.contains(card))) {
				CardType cardType = card.getCardType();
				switch (cardType) {
				case WEAPON:{
					if (weapon == null) {
						weapon = card;
					}
					break;
				}
				case PEOPLE:{
					if (people == null) {
						people = card;
					}
				}
				}
			}
			// if found both weapon and people card, don't keep looping through dealt cards
			if (people != null && weapon != null) {
				break;
			}
		}
		this.suggestion = new Suggestion(people, room, weapon);
		return this.suggestion;
	}

	@Override
	public void checkIfPlayerShouldHandleSuggestion() {
		// only check if currentPlayer is this player
		// we do this because players can be pulled into rooms
		if (Board.getInstance().getCurrentPlayer() == this && finishedTurn == false) {

			// these function calls are not event driven and should only be used for computers.
			// createSuggestion returns null if player isn't in room
			suggestion = createSuggestion();
			if (suggestion != null) {
				Card disprovedCard = Board.getInstance().handleSuggestion(this);
				if (disprovedCard == null) {
					// we know that if the card is null, no one disproved it and that is the solution besides themselves
					if (!(seen.contains(suggestion.getRoom()))){
						// players must suggest room they are in, because of this, we don't check to see if its in seen/hand
						// this means that when attempting to disprove suggestion, the player making the suggesting
						// cannot disprove the room card they have in their hand and think they have found a solution
						accusation = new Solution(
								suggestion.getPeople(), 
								suggestion.getRoom(), 
								suggestion.getWeapon());
					}
				} 
			}
		}

	}


}
