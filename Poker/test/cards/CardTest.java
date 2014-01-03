package cards;

import static org.junit.Assert.*;

import org.junit.Test;

import cards.Card.Suit;

public class CardTest {

	//♠ ♢ ♡ ♣
	
	@Test
	public void testCardZero() {
		try {
			@SuppressWarnings("unused")
			Card ace = new Card(0, Suit.CLUBS);
			fail();
		} catch (Error e) {
			assertEquals(e.getMessage(), "Card value 0 not in range");
		}
	}
	
	@Test
	public void testCardOne() {
		Card ace = new Card(1, Suit.CLUBS);
		assertEquals(ace.toString(), "A ♣");
	}

	@Test
	public void testCardTwo() {
		Card two = new Card(2, Suit.SPADES);
		assertEquals(two.toString(), "2 ♠");
	}
	
	@Test
	public void testCardTen() {
		Card ten = new Card(10, Suit.HEARTS);
		assertEquals(ten.toString(), "10 ♡");	
	}
	
	@Test
	public void testCardEleven() {
		Card jack = new Card(11, Suit.DIAMONDS);
		assertEquals(jack.toString(), "J ♢");
	}
	
	@Test
	public void testCardTwelve() {
		Card queen = new Card(12, Suit.DIAMONDS);
		assertEquals(queen.toString(), "Q ♢");
	}
	
	@Test
	public void testCardThirteen() {
		Card king = new Card(13, Suit.DIAMONDS);
		assertEquals(king.toString(), "K ♢");
	}
}
