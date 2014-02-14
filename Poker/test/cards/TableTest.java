package cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cards.Card.Suit;

public class TableTest {

	private static Hand bestHand, secondBestHand, thirdBestHand, fourthBestHand, fithBestHand;
	private static Player richWinner, poorWinner,
							superRichSecondWinner, richSecondWinner, poorSecondWinner,
							richThirdWinner, playerWithFourthBestHand, playerWithFithBestHand;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bestHand = new Hand(new Card[] {new Card(1,Suit.SPADES),new Card(10,Suit.SPADES),new Card(11,Suit.SPADES),
											 new Card(13,Suit.SPADES),new Card(12,Suit.SPADES), new Card(2,Suit.CLUBS), new Card(3,Suit.CLUBS)});

		secondBestHand = new Hand(new Card[] {new Card(1,Suit.SPADES),new Card(1,Suit.DIAMONDS),new Card(1,Suit.CLUBS),
												   new Card(1,Suit.HEARTS),new Card(12,Suit.SPADES), new Card(2,Suit.CLUBS), new Card(3,Suit.CLUBS)});

		thirdBestHand = new Hand(new Card[] {new Card(1,Suit.SPADES),new Card(1,Suit.DIAMONDS),new Card(1,Suit.CLUBS),
												  new Card(12,Suit.HEARTS),new Card(12,Suit.SPADES), new Card(2,Suit.CLUBS), new Card(3,Suit.CLUBS)});

		fourthBestHand = new Hand(new Card[] {new Card(1,Suit.SPADES),new Card(1,Suit.DIAMONDS),new Card(1,Suit.CLUBS),
				 							 	   new Card(13,Suit.SPADES),new Card(12,Suit.SPADES), new Card(2,Suit.CLUBS), new Card(3,Suit.CLUBS)});

		fithBestHand = new Hand(new Card[] {new Card(1,Suit.HEARTS),new Card(10,Suit.SPADES),new Card(11,Suit.SPADES),
												 new Card(3,Suit.SPADES),new Card(12,Suit.SPADES), new Card(2,Suit.CLUBS), new Card(3,Suit.CLUBS)});

		richWinner = new NPC("Rich Winner",100);
		richWinner.setMadeHand(bestHand);
		poorWinner = new NPC("Poor Winner",50);
		poorWinner.setMadeHand(bestHand);

		superRichSecondWinner = new NPC("Super Rich Second Winner",200);
		superRichSecondWinner.setMadeHand(secondBestHand);
		richSecondWinner = new NPC("Rich Second Winner",100);
		richSecondWinner.setMadeHand(secondBestHand);
		poorSecondWinner = new NPC("Poor Second Winner",50);
		poorSecondWinner.setMadeHand(secondBestHand);

		richThirdWinner = new NPC("Rich Third Winner",100);
		richThirdWinner.setMadeHand(thirdBestHand);

		playerWithFourthBestHand = new NPC("Fourth Player 1",10);
		playerWithFourthBestHand.setMadeHand(fourthBestHand);

		playerWithFithBestHand= new NPC("Fith Player 1",60);
		playerWithFithBestHand.setMadeHand(fithBestHand);
	}

	@Before
	public void setUp() throws Exception {
		richWinner.resetBetsAfterRound();
		richWinner.ResetBetThisHand();
		richWinner.setChips(100);
		richWinner.makeBet(100);
		poorWinner.resetBetsAfterRound();
		poorWinner.ResetBetThisHand();
		poorWinner.setChips(50);
		poorWinner.makeBet(50);

		superRichSecondWinner.resetBetsAfterRound();
		superRichSecondWinner.ResetBetThisHand();
		superRichSecondWinner.setChips(200);
		superRichSecondWinner.makeBet(200);
		richSecondWinner.resetBetsAfterRound();
		richSecondWinner.ResetBetThisHand();
		richSecondWinner.setChips(100);
		richSecondWinner.makeBet(100);
		poorSecondWinner.resetBetsAfterRound();
		poorSecondWinner.ResetBetThisHand();
		poorSecondWinner.setChips(50);
		poorSecondWinner.makeBet(50);

		richThirdWinner.resetBetsAfterRound();
		richThirdWinner.ResetBetThisHand();
		richThirdWinner.setChips(100);
		richThirdWinner.makeBet(100);

		playerWithFourthBestHand.resetBetsAfterRound();
		playerWithFourthBestHand.ResetBetThisHand();
		playerWithFourthBestHand.setChips(10);
		playerWithFourthBestHand.makeBet(10);

		playerWithFithBestHand.resetBetsAfterRound();
		playerWithFithBestHand.ResetBetThisHand();
		playerWithFithBestHand.setChips(60);
		playerWithFithBestHand.makeBet(60);
	}

	@Test
	public void testJointWinnersTakingAllMoney() {
		Table table = new Table();

		List<Player> players = new ArrayList<Player>();
		players.add(richWinner);
		players.add(poorWinner);
		players.add(richSecondWinner);

		int pot = 0;
		for(Player p : players) {
			table.addPlayer(p);
			pot += p.getPotContribution();
		}

		table.setPot(pot);

		List<Player> winners = new ArrayList<Player>();
		winners.add(richWinner);
		winners.add(poorWinner);

		List<Player> secondBest = new ArrayList<Player>();
		secondBest.add(richSecondWinner);

		List<List<Player>> sortedPlayers = new ArrayList<List<Player>>();
		sortedPlayers.add(winners);
		sortedPlayers.add(secondBest);

		table.payWinner(sortedPlayers);

		assertEquals(75, poorWinner.getChips());
		assertEquals(175, richWinner.getChips());
		assertEquals(0, richSecondWinner.getChips());
	}

	@Test
	public void testJointWinnersWithSecondPlaceHavingLeftOvers() {
		Table table = new Table();

		List<Player> players = new ArrayList<Player>();
		players.add(richWinner);
		players.add(poorWinner);
		players.add(superRichSecondWinner);

		int pot = 0;
		for(Player p : players) {
			table.addPlayer(p);
			pot += p.getPotContribution();
		}

		table.setPot(pot);

		List<Player> winners = new ArrayList<Player>();
		winners.add(richWinner);
		winners.add(poorWinner);

		List<Player> secondBest = new ArrayList<Player>();
		secondBest.add(superRichSecondWinner);

		List<List<Player>> sortedPlayers = new ArrayList<List<Player>>();
		sortedPlayers.add(winners);
		sortedPlayers.add(secondBest);

		table.payWinner(sortedPlayers);

		assertEquals(75, poorWinner.getChips());
		assertEquals(175, richWinner.getChips());
		assertEquals(100, superRichSecondWinner.getChips());
	}

	@Test
	public void testJointWinnersTakingAllMoneyWithEqualSplit() {
		Player anotherRichWinner = new NPC("Rich Winner 2",100);
		anotherRichWinner.setMadeHand(bestHand);
		anotherRichWinner.makeBet(100);

		Table table = new Table();

		List<Player> players = new ArrayList<Player>();
		players.add(richWinner);
		players.add(anotherRichWinner);
		players.add(richSecondWinner);

		int pot = 0;
		for(Player p : players) {
			table.addPlayer(p);
			pot += p.getPotContribution();
		}

		table.setPot(pot);

		ArrayList<Player> winners = new ArrayList<Player>();
		winners.add(richWinner);
		winners.add(anotherRichWinner);

		ArrayList<Player> secondBest = new ArrayList<Player>();
		secondBest.add(richSecondWinner);

		List<List<Player>> sortedPlayers = new ArrayList<List<Player>>();
		sortedPlayers.add(winners);
		sortedPlayers.add(secondBest);

		table.payWinner(sortedPlayers);

		assertEquals(150, richWinner.getChips());
		assertEquals(150, anotherRichWinner.getChips());
		assertEquals(0, richSecondWinner.getChips());
	}

	@Test
	public void testJustOneWinner() {
		Table table = new Table();

		List<Player> players = new ArrayList<Player>();
		players.add(richWinner);;

		int pot = 0;
		for(Player p : players) {
			table.addPlayer(p);
			pot += p.getPotContribution();
		}

		table.setPot(pot);

		ArrayList<Player> winners = new ArrayList<Player>();
		winners.add(richWinner);

		List<List<Player>> sortedPlayers = new ArrayList<List<Player>>();
		sortedPlayers.add(winners);

		table.payWinner(sortedPlayers);

		assertEquals(100, richWinner.getChips());
	}

}
