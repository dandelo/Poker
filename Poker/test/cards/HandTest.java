package cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import cards.Card.Suit;

public class HandTest {
	
	Suit s = Suit.SPADES;
	Suit c = Suit.CLUBS;
	Suit h = Suit.HEARTS;
	Suit d = Suit.DIAMONDS;
	
	//♠ ♢ ♡ ♣
	
	@Test
	public void testFullHouse00() {
		
		Card[] cards = {new Card(2,s),new Card(2,d),new Card(2,h),
						new Card(5,c),new Card(5,s)};
		Hand h = new Hand(cards);

		int[] ans = {7,2,2,2,5,5};
		assertArrayEquals(ans, h.getMadeHandValue());		
	}
	
	@Test
	public void testFullHouse01() {
		
		Card[] cards = {new Card(2,s),new Card(2,d),new Card(5,h),
						new Card(5,c),new Card(5,s)};
		Hand h = new Hand(cards);

		int[] ans = {7,5,5,5,2,2};
		assertArrayEquals(ans, h.getMadeHandValue());		
	}
	
	@Test
	public void testTwoPair0() {
		
		Card[] cards = {new Card(10,s),new Card(1,c),new Card(6,s),
						new Card(10,h),new Card(1,s),new Card(6,c),new Card(13,d)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 3);
	}
	
	@Test
	public void testTwoPair00() {
		
		Card[] cards = {new Card(6,c),new Card(10,s),new Card(13,h),
						new Card(12,d),new Card(10,s),new Card(13,s),new Card(7,c)};
		int[] ans = {3,13,13,10,10,12};
		Hand h = new Hand(cards);
		assertArrayEquals(ans, h.getMadeHandValue());
		assert(h.getMadeHandValue()[0] == 3);
	}
	
	@Test
	public void testTwoPair01() {
		
		Card[] cards = {new Card(4,s),new Card(3,c),new Card(2,c),
						new Card(12,d),new Card(4,c),new Card(10,c),new Card(2,d)};
		int[] ans = {3,4,4,2,2,12};
		Hand h = new Hand(cards);
		assertArrayEquals(ans, h.getMadeHandValue());
	}
	
	@Test
	public void testStriaghtFlush() {
		
		Card[] cards = {new Card(1,s),new Card(2,s),new Card(3,s),
						new Card(4,s),new Card(5,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 9);
		
		int[] ans = {9,5,4,3,2,1};
		
		assertArrayEquals(ans, h.getMadeHandValue());
	}
	
	@Test
	public void testStriaghtFlushwithAce() {
		
		Card[] cards = {new Card(1,s),new Card(3,s),new Card(6,s),new Card(5,s),
						new Card(4,s),new Card(7,s),new Card(10,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 9);
		
		assertEquals("7 ♠ 6 ♠ 5 ♠ 4 ♠ 3 ♠", h.toString());
	}
	
	@Test
	public void testStriaghtFlushWithHigherStraight() {
		
		Card[] cards = {new Card(8,d),new Card(3,s),new Card(6,s),new Card(5,s),
						new Card(4,s),new Card(7,s),new Card(10,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 9);
		
		assertEquals("7 ♠ 6 ♠ 5 ♠ 4 ♠ 3 ♠", h.toString());
	}
	
	@Test
	public void testStriaghtFlush2() {
		
		Card[] cards = {new Card(1,s),new Card(9,c),new Card(13,h),new Card(2,s),
						new Card(3,s),new Card(4,s),new Card(5,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 9);
	}
	
	@Test
	public void testQuads() {
		
		Card[] cards = {new Card(1,s),new Card(1,d),new Card(1,h),
						new Card(1,c),new Card(5,s)};
		Hand h = new Hand(cards);
		
		int[] ans = {8,14,14,14,14,5};
		assertArrayEquals(ans, h.getMadeHandValue());	
	}
	
	@Test
	public void testQuads2() {
		
		Card[] cards = {new Card(6,s),new Card(12,h),new Card(6,d),new Card(6,h),
				new Card(1,c),new Card(6,c),new Card(12,s)};
		Hand h = new Hand(cards);
		
		int[] ans = {8,6,6,6,6,14};
		assertArrayEquals(ans, h.getMadeHandValue());	
	}
	
	@Test
	public void testQuads3() {
		
		Card[] cards = {new Card(12,s),new Card(6,h),new Card(12,d),new Card(12,h),
				new Card(5,c),new Card(12,c),new Card(6,s)};
		Hand h = new Hand(cards);
		
		int[] ans = {8,12,12,12,12,6};
		assertArrayEquals(ans, h.getMadeHandValue());	
	}
	
	@Test	
	public void testFullHouse() {
		
		Card[] cards = {new Card(1,s),new Card(1,d),new Card(1,h),
						new Card(5,c),new Card(5,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 7);
	}
	
	@Test
	public void testFlush() {
		
		Card[] cards = {new Card(10,s),new Card(6,s),new Card(3,s),
						new Card(4,s),new Card(1,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 6);
	}
	
	@Test
	public void testStriaght() {
		
		Card[] cards = {new Card(10,s),new Card(11,c),new Card(13,s),
						new Card(1,h),new Card(12,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 5);
	}
	
	@Test
	public void testFlushAndStriaght() {
		
		Card[] cards = {new Card(10,s),new Card(11,c),new Card(13,s),
						new Card(1,h),new Card(12,s),new Card(7,s),new Card(8,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 6);
		
		assertEquals("K ♠ Q ♠ 10 ♠ 8 ♠ 7 ♠", h.toString());
	}
	
	@Test
	public void testStriaght2() {
		
		Card[] cards = {new Card(3,s),new Card(4,c),new Card(7,s),
						new Card(6,h),new Card(5,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 5);
	}
	
	@Test
	public void testTrips() {
		
		Card[] cards = {new Card(10,s),new Card(10,c),new Card(13,s),
						new Card(10,h),new Card(12,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 4);
	}
	
	@Test
	public void testTwoPair() {
		
		Card[] cards = {new Card(10,s),new Card(12,c),new Card(6,s),
						new Card(10,h),new Card(12,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 3);
	}
	
	@Test
	public void testPair() {
		
		Card[] cards = {new Card(10,s),new Card(12,c),new Card(6,s),
						new Card(3,h),new Card(12,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 2);
	}
	
	@Test
	public void testHighCard() {
		
		Card[] cards = {new Card(10,s),new Card(7,c),new Card(6,s),
						new Card(3,h),new Card(12,s)};
		Hand h = new Hand(cards);
		assert(h.getMadeHandValue()[0] == 1);
		assertEquals(Long.valueOf("11210070603"),h.getLongMadeHandValue());
	}
	
	@Test
	public void testTwoPairWithAces() {
		
		Card[] cards = {new Card(12,s),new Card(1,s),new Card(9,d),new Card(13,c),
						new Card(12,d),new Card(1,c),new Card(9,h)};
		Hand h = new Hand(cards);
		
		int[] ans = {3,14,14,12,12,13};
		assertArrayEquals(ans, h.getMadeHandValue());
	}
	
	@Test
	public void testTwoPairWithAceKicker() {
		
		Card[] cards = {new Card(12,s),new Card(7,s),new Card(9,d),new Card(1,c),
						new Card(12,d),new Card(7,c),new Card(9,h)};
		Hand h = new Hand(cards);
		
		int[] ans = {3,12,12,9,9,14};
		assertArrayEquals(ans, h.getMadeHandValue());
	}
	
	@Test
	public void testWinningHand() {
		Card[] cards1 = {new Card(13,s),new Card(10,h),new Card(7,d),new Card(13,c),
						new Card(1,s),new Card(8,d),new Card(7,s)};
		Hand h1 = new Hand(cards1);
		
		Card[] cards2 = {new Card(13,s),new Card(10,h),new Card(7,d),new Card(13,c),
				new Card(1,s),new Card(5,s),new Card(2,h)};
		Hand h2 = new Hand(cards2);
		
		List<Hand> hands = new ArrayList<Hand>();
		hands.add(h2);
		hands.add(h1);
		
		Collections.sort(hands);

		assertEquals(h1, hands.get(0));
	}
	
	@Test
	public void testToString() {
		
		Card[] cards = {new Card(12,s),new Card(7,s),new Card(9,d),new Card(1,c),
						new Card(12,d),new Card(7,c),new Card(9,h)};
		Hand h = new Hand(cards);
		
		//3,12,12,9,9,14
		
		assertEquals("Q ♠ Q ♢ 9 ♢ 9 ♡ A ♣", h.toString());
	}
	
	@Test
	public void testTwoTripsEqualsFullHouse() {
		
		Card[] cards = {new Card(2,s),new Card(13,s),new Card(13,h),new Card(2,c),
						new Card(1,c),new Card(2,h),new Card(13,c)};
		Hand h = new Hand(cards);
		
		//3,12,12,9,9,14
		
		assertEquals("K ♠ K ♡ K ♣ 2 ♠ 2 ♣", h.toString());
	}

	@Test
	public void testToStringWithFLush() {
		
		Card[] cards = {new Card(12,s),new Card(7,s),new Card(9,d),new Card(1,d),
						new Card(12,d),new Card(7,d),new Card(2,d)};
		Hand h = new Hand(cards);
		
		//3,12,12,9,9,14
		
		assertEquals("A ♢ Q ♢ 9 ♢ 7 ♢ 2 ♢", h.toString());
	}
	
	/*
	 * Dylan $92
			2 ♢ 6 ♣ 
		Rupert $92
		A ♢ 7 ♣ 
		2 ♠ 2 ♣ 10 ♠ 5 ♢ J ♢ 
	 */
	@Test
	public void testToString2() {
		
		Card[] cards = {new Card(6,d),new Card(8,s),new Card(10,h),new Card(5,s),
						new Card(1,c),new Card(6,h),new Card(6,c)};
		Hand h = new Hand(cards);
		
		//3,12,12,9,9,14
		
		assertEquals("6 ♢ 6 ♡ 6 ♣ A ♣ 10 ♡", h.toString());
	}
	
	@Test
	public void testEquals() {
		
		Card[] cards = {new Card(12,s),new Card(7,s),new Card(9,s),new Card(1,d),
				new Card(12,d),new Card(7,d),new Card(2,d)};
		Card[] cards2 = {new Card(12,s),new Card(7,s),new Card(13,s),new Card(1,d),
				new Card(12,d),new Card(7,d),new Card(11,d)};
		Hand h = new Hand(cards);
		Hand h2 = new Hand(cards2);
		
		
		//3,12,12,9,9,14
		
		assertEquals(h, h2);
	}
	
	@Test
	public void testEqualsHighCards() {
		
		Card[] cards = {new Card(12,s),new Card(2,s),new Card(10,s),new Card(1,d),
				new Card(8,d),new Card(7,d),new Card(4,d)};
		Card[] cards2 = {new Card(12,s),new Card(6,s),new Card(10,s),new Card(1,d),
				new Card(8,d),new Card(7,d),new Card(5,d)};
		Hand h = new Hand(cards);
		Hand h2 = new Hand(cards2);
		
		
		//3,12,12,9,9,14
		
		assertEquals(h, h2);
	}


}
