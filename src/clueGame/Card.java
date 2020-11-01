package clueGame;

public class Card {

	private CardType cardType;
	private String cardName;
	
	public Card(String cardName, CardType cardType) {
		this.cardName = cardName;
		this.cardType = cardType;
	}
	
	public boolean equals(Card card) {
		//TODO check equality of cards
		return false;
	}
	
}
