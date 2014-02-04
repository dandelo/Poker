package cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cards.Card.Suit;


class Hand implements Comparable<Hand> {
	
	private Card[] cards;
	private String madeHand;
	
	private int[] values = new int[14];		// 13 card values, but don't use 0
	private int hearts=0, diamonds=0, spades=0, clubs=0;
	private Suit flushSuit;					//if there's a flush, it's this suit
	private List<Integer> flushValues = new ArrayList<Integer>(); //if flush, these face values
	private int[] madeHandValue = new int[6]; 	//0=rank, 1-4=ordered cards. 
												//Ranking:
												//1=High Card, 2=Pair, 3=Two Pair, 4=Trips, 5=Straight,
												//6=Flush, 7=Full-House, 8=Quads 9=Straight Flush
	
	Hand(Card[] cards) {
		this.cards = cards;
		
		for(Card card: cards) {
			values[card.getValue()]++;
			
			switch(card.getSuit()) {
				case HEARTS: 	hearts++;
								break;
				case DIAMONDS: 	diamonds++;
								break;
				case SPADES: 	spades++;
								break;
				case CLUBS: 	clubs++;
								break;
			}
		}
		
		findMatchingValues();						//Checks for pair(s), trips, quads, full-house and high card hands
		if(isFlush() & isStraight()) {
			checkIfStraightFlush();
		}
		
		setHandValue();		
	}
	
	/*
	 * Sets the value of madeHand: a String representation of best five-card hand
	 */
	private void setHandValue() {
		String hand = "";
		Card[] madeHandCards = new Card[5];
		List<Card> cards = new ArrayList<Card>(Arrays.asList(this.cards));
		
		if(madeHandValue[0] == 6) {		// if flush then looking for particular suits of values
			for(int i=1; i<madeHandValue.length; i++) {
				for(Card c: cards) {
					if(c.getSuit() == flushSuit && (c.getValue() == madeHandValue[i]) || 
					  (c.getValue() == 1 && madeHandValue[i] == 14)) { //ace=1&14
						madeHandCards[i-1] = c;
						cards.remove(c);
						break;
					}
				}
			}
		}
		else {
			for(int i=1; i<madeHandValue.length; i++) {
				for(Card c: cards) {
					if((c.getValue() == madeHandValue[i]) || (c.getValue() == 1 && madeHandValue[i] == 14)) {
						madeHandCards[i-1] = c;
						cards.remove(c);
						break;
					}
				}
			}
		}
		
		for(Card c: madeHandCards) {
			hand += c.toString() + " ";
		}
		madeHand = hand.trim();		
	}

	/*
	 * Checks whether there is a flush and sets made flush hand if true
	 */
	private boolean isFlush() {
		if(hearts>=5) {
			setFlushHand(Suit.HEARTS);
			return true;
		}
		else if (diamonds>=5) {
			setFlushHand(Suit.DIAMONDS);
			return true;
		}
		else if (spades>=5) {
			setFlushHand(Suit.SPADES);
			return true;
		}
		else if (clubs>=5) {
			setFlushHand(Suit.CLUBS);
			return true;
		}
		return false;
	}
	
	/*
	 * Does the setting of made hand for flushes
	 */
	private void setFlushHand(Suit s) {
		
		flushSuit = s;
				
		for(Card c: cards) {
			if(c.getSuit() == s) {
				flushValues.add(c.getValue());
			}
		}
		if(flushValues.contains(1)) { // add high ace
			flushValues.add(14);
		}
		Collections.sort(flushValues, Collections.reverseOrder());
		
		int[] madeHandValue = {6,flushValues.get(0),flushValues.get(1),flushValues.get(2),flushValues.get(3),flushValues.get(4)};
		
		if(this.madeHandValue[0] < 6) {
			this.madeHandValue = madeHandValue;
		}
	}
	
	/*
	 * Checks whether there is a straight and sets made hand if true
	 */
	private boolean isStraight() {
		//ace high straight
		if (values[10]>0 && values[11]>0 && values[12]>0 && values[13]>0 && values[1]>0) {
			if(madeHandValue[0] < 5) { 					//only set hand if it's less than a straight
				int[] hand = {5,14,13,12,11,10};
				madeHandValue = hand;
			}
	        return true;
		}
		for (int x=9; x>=1; x--) { 						//can't have straight with lowest value of more than 10
		    if (values[x]>0 && values[x+1]>0 && values[x+2]>0 && values[x+3]>0 && values[x+4]>0) {
		    	if(madeHandValue[0] < 5) { 				//only set hand if it's less than a straight
					int[] hand = {5,x+4,x+3,x+2,x+1,x};
					madeHandValue = hand;
				}
		        return true;
		    }
		}
		return false;
	}
	
	/*
	 * if Hand isFlush and isStraight, walk through flushValues to see if it's a striaght.
	 */
	private void checkIfStraightFlush() {
		for (int x=0; x<=flushValues.size()-5; x++) {
		    if ((flushValues.get(x)-1 == flushValues.get(x+1)) && (flushValues.get(x)-2 == flushValues.get(x+2)) &&
		    	(flushValues.get(x)-3 == flushValues.get(x+3)) && (flushValues.get(x)-4 == flushValues.get(x+4))) {
		    	
		    	int[] hand = {9,flushValues.get(x),flushValues.get(x+1),flushValues.get(x+2),flushValues.get(x+3),flushValues.get(x+4)};
		    	madeHandValue = hand;
		    	break;
		    }
		}
	}
	
	/*
	 * Find matching card values.
	 * Will find pair(s), trips, quads, full houses and high card hands
	 */
	private void findMatchingValues() {
		int matchingCardsCount=1, matchingCardsCount2=1; 	// two in case of two pair and full-house
		int largeGroupValue=0, smallGroupValue=0;			//value of matching cards
		int[] highCards = new int[5];						// highest five non-paired cards (for kickers and high card hand)

		if (values[1] > matchingCardsCount) {
			matchingCardsCount = values[1];
        	largeGroupValue = 14;
		}
		for (int x=13; x>=2; x--) {
		     if (values[x] > matchingCardsCount) {
		         if (matchingCardsCount != 1) {					//if not the first match found
		             matchingCardsCount2 = matchingCardsCount;
		             smallGroupValue = largeGroupValue;
		         }
		         
		         matchingCardsCount = values[x];
		         largeGroupValue = x;
		         
		     } else if (values[x] > matchingCardsCount2) {
		         matchingCardsCount2 = values[x];
		         smallGroupValue = x;
		     }
		}
		
		int index = 0;										
		if (14 != largeGroupValue && 14 != smallGroupValue && values[1] > 0) { //ace kicker
			highCards[index] = 14;
			index++;
		}
		for (int x=13; x>=2; x--) { 
			if (x != largeGroupValue && x != smallGroupValue && values[x] > 0) {
				highCards[index] = x;
				index++;
				if(index == 5) break;
			}
		}
		
		if(largeGroupValue == 1 ) {
			largeGroupValue = 14;			//set Aces to value of 14
		}
		if(smallGroupValue == 1 ) {
			smallGroupValue = largeGroupValue;
			largeGroupValue = 14;
		}
		
		setMatchingValuesMadeHand(matchingCardsCount, matchingCardsCount2, largeGroupValue, smallGroupValue, highCards);
	}
	
	/*
	 * Once all matching card values found, this will create made hands
	 */
	private void setMatchingValuesMadeHand(int matchingCardsCount, int matchingCardsCount2, int largeGroupValue, int smallGroupValue, int[] highCards) {
		if(matchingCardsCount == 4 || matchingCardsCount2 == 4) {			//quads
			int[] hand = {8,largeGroupValue,largeGroupValue,largeGroupValue,largeGroupValue,Math.max(highCards[0],smallGroupValue)};
			madeHandValue = hand;
		}
		else if(matchingCardsCount == 3 || matchingCardsCount2 == 3) {
			if((matchingCardsCount == 3 && matchingCardsCount2 >= 2) || (matchingCardsCount >= 2 && matchingCardsCount2 == 3)) {		//full house
				int[] hand = {7,largeGroupValue,largeGroupValue,largeGroupValue,smallGroupValue,smallGroupValue};
				madeHandValue = hand;
			}
			else {										//trips
				int[] hand = {4,largeGroupValue,largeGroupValue,largeGroupValue,highCards[0],highCards[1]};
				madeHandValue = hand;
			}
		}
		else if(matchingCardsCount == 2 || matchingCardsCount2 == 2) {
			if(matchingCardsCount == 2 && matchingCardsCount2 == 2) {		//two pair
				int[] hand = {3,largeGroupValue,largeGroupValue,smallGroupValue,smallGroupValue,highCards[0]};
				madeHandValue = hand;
			}
			else {										//one pair
				int[] hand = {2,largeGroupValue,largeGroupValue,highCards[0],highCards[1],highCards[2]};
				madeHandValue = hand;
			}

		}
		else {											//high card
			int[] hand = {1,highCards[0],highCards[1],highCards[2],highCards[3],highCards[4]};
			madeHandValue = hand;			
		}
		
	}
	
	/*
	 * @overrides
	 */
	public boolean equals(Object hand) {
		if (!(hand instanceof Hand)) {
			return false;
		}
		
		for(int i=0; i<=5; i++) {		// Hand value represented by best 5-card hand (madeHandValue[0] = rank)
			if(madeHandValue[i] != ((Hand)hand).madeHandValue[i]) {
				return false;
			}
		}
		return true;
	}
	
	public int hashCode() {
		return this.getLongMadeHandValue().intValue();
	}
	
	public String toString() {
		return madeHand;
	}

	@Override
	public int compareTo(Hand h) {
		return h.getLongMadeHandValue().compareTo(this.getLongMadeHandValue());
	}
	
	/*
	 * Method to allow easy comparison of MadeHands
	 * @returns the MadeHandValue array as a single long value
	 */
	public Long getLongMadeHandValue() {
		String s = "" + madeHandValue[0];
		for(int i=1; i<madeHandValue.length; i++) {
			if(madeHandValue[i] < 10) s += 0;
			s += madeHandValue[i];
		}
		return Long.valueOf(s);
	}
	
	/*
	 * @returns madeHandValue: Array containing face value of a hand (used for testing)
	 */
	public int[] getMadeHandValue() {
		return madeHandValue;
	}

}
