package clueGame;

public class Suggestion {
	Card people, room, weapon;
	
	public Suggestion(Card people, Card room, Card weapon) {
		this.people = people;
		this.room = room;
		this.weapon = weapon;
	}

	public Card getPeople() {
		return people;
	}

	public void setPeople(Card person) {
		this.people = person;
	}

	public Card getRoom() {
		return room;
	}

	public void setRoom(Card room) {
		this.room = room;
	}

	public Card getWeapon() {
		return weapon;
	}

	public void setWeapon(Card weapon) {
		this.weapon = weapon;
	}
	
}
