package cards;

import java.util.LinkedList;
import java.util.List;

import cards.Card.Suit;


class Deck {
	
	private List<Card> deck;
	
	 public Deck() {
		
		deck = new LinkedList<Card>();
		
		buildDeck();
	}

	private void buildDeck() {
		final int NO_OF_SUITS = 4;
		
		for(int i=0; i<NO_OF_SUITS; i++) {
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
	
	public void printDeck() {
		for(Card c: deck) {
			System.out.println(c);
		}
	}
	
	public List<Card> getDeck() {
		return deck;
	}

}
