package clueGame;

public class Card {

	private CardType cardType;
	private String cardName;
	
	public Card(String cardName, CardType cardType) {
		this.cardName = cardName;
		this.cardType = cardType;
	}
	
	public Card() {}
	
	public boolean equals(Card card) {
		//TODO check equality of cards
		return false;
	}
	
	public CardType getCardType() {
		return this.cardType;
	}
	
	public String toString() {
		return this.cardName;
	}
	
}
