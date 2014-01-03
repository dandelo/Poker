/**
 * 
 */
package cards;

import java.util.LinkedList;

import cards.Card.Suit;

/**
 * @author dturner
 *
 */
class Deck {
	
	private LinkedList<Card> deck;
	
	protected Deck() {
		
		deck = new LinkedList<Card>();
		
		for(int i=0; i<4; i++) {
			Suit s = null;
			switch(i) {
				case(0): 	s=Suit.HEARTS;
							break;
				case(1): 	s=Suit.DIAMONDS;
							break;

				case(2): 	s=Suit.SPADES;
							break;

				case(3): 	s=Suit.CLUBS;
							break;
			}
			for(int j=1; j<=13; j++) {
				deck.add(new Card(j,s));
			}
		}
	}
	
	public void shuffle() {
		for(int i=0; i<52; i++) {
			int card = (int) (Math.random() * (52-i));
			deck.addLast(deck.remove(card));
		}
	}
	
	public void printDeck() {
		for(Card c: deck) {
			System.out.println(c.toString());
		}
	}
	
	protected LinkedList<Card> getDeck() {
		return deck;
	}

}
