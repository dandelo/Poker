/**
 * 
 */
package cards;

/**
 * @author dturner
 *
 */
class Card {
	
	public enum Suit {HEARTS, DIAMONDS, SPADES, CLUBS}
	
	private Suit suit;
	private int value;
	
	
	protected Card(int value, Suit suit) {
		
		if (value <= 0 || value >= 14) throw new Error("Card value " + value + " not in range");
		
		this.suit = suit;
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public String toString() {
		String s = "";
		
		switch(value) {
			case(1):	s = "A";
						break;
			case(11):	s = "J";
						break;
			case(12):	s = "Q";
						break;
			case(13):	s = "K";
						break;
			default:	s = Integer.toString(value);
						break;
		}
		
		char c = '0';
		
		switch(suit) {
			case HEARTS: 	c = '\u2661';
							break;
			case DIAMONDS: 	c = '\u2662';
							break;
			case SPADES: 	c = '\u2660';
							break;
			case CLUBS: 	c = '\u2663';
							break;
		}
		
		return s + " " + c;
		//return s + c;

		
	}

}
