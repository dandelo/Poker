package cards;

import java.util.*;

class Table {
	
	private static final int STARTING_CHIPS = 100;
	private static final String[] STARTING_PLAYERS = {"Dylan","Lola","Rupert"};
	
	private Deck deck;
	private List<Player> players, winners;
	private Card[] communalCards;
	private int pot = 0;
	private int dealerPosition = (int) (Math.random()*2);
	private int blind = 5;

	public Table() {
		communalCards = new Card[5];
		
		players = new ArrayList<Player>();
		winners = new ArrayList<Player>();
		for(String s : STARTING_PLAYERS) {
			players.add(new Player(s,STARTING_CHIPS));
		}

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Table table = new Table();

		while(table.players.size() > 1) {
			table.dealRound();
		}
	}
	
	/*
	 * Deals a single round
	 */
	private void dealRound() {
		
		deck = new Deck();
		deck.shuffle();
		//Collections.shuffle(deck.getDeck());
		
		payBlinds();
		deal();
		flop();
		turn();
		river();
				
		printTable();
		winners = findWinner();
		printWinner(winners);
		payWinner(winners);
		
		for(int i=0; i<players.size(); i++) {
			if(players.get(i).getChips() == 0) players.remove(i);
		}
		
		if(dealerPosition +1 > players.size()-1) {
			dealerPosition = 0;
		}
		else dealerPosition++;
	}

	private void payBlinds() {
		// TODO condition for player chips < blind (covered pot)
		int shortfall1, shortfall2;
		
		if((dealerPosition +1) >= players.size()) {
			shortfall1 = players.get(0).bet(blind);
			shortfall2 = players.get(1).bet(blind*2);
		}
		else if((dealerPosition +2) >= players.size()) {
			shortfall1 = players.get(dealerPosition+1).bet(blind);
			shortfall2 = players.get(0).bet(blind*2);
		}
		else {
			shortfall1 = players.get(dealerPosition+1).bet(blind);
			shortfall2 = players.get(+2).bet(blind*2);
		}
		pot+= blind*3 - shortfall1 - shortfall2;
	}

	private List<Player> findWinner() {
		List<Player> winners = new ArrayList<Player>();

		TreeSet<Hand> allPlayerHands = new TreeSet<Hand>();
		
		for(Player player: players) {
			Card[] cards = new Card[7];
			System.arraycopy(communalCards, 0, cards, 0, 5);
			cards[5] = player.getCards()[0];
			cards[6] = player.getCards()[1];
			player.setMadeHand(new Hand(cards));
			allPlayerHands.add(player.getMadeHand());
		}
		
		for(Player player: players) {
			if(player.getMadeHand().equals(allPlayerHands.first())) {
				winners.add(player);
			}
		}
		
		return winners;
	}
	
	private void printWinner(List<Player> winners) {
		
		System.out.print("\nWinners: ");
		for(Player p: winners) {
			System.out.print(p.getName() + " " + "$" + pot/winners.size() + " ");
		}
		System.out.println("\n" + winners.get(0).getMadeHand().toString());

		System.out.println("\n---------------\n\n");
	}
	
	private void payWinner(List<Player> winners) {
		for(Player p: winners) {
			p.addChips(pot/winners.size());
		}
		pot=0;
	}

	private void deal() {
		for(int i=0; i<=1; i++) {
			for(int j=dealerPosition+1; j<players.size(); j++) {
				players.get(j).setCards(deck.getDeck().poll(), i);
			}
			for(int j=0; j<=dealerPosition; j++) {
				players.get(j).setCards(deck.getDeck().poll(), i);
			}
		}
	}
	
	private void flop() {
		burn();
		communalCards[0] = deck.getDeck().poll();
		communalCards[1] = deck.getDeck().poll();
		communalCards[2] = deck.getDeck().poll();
	}
	
	private void turn() {
		burn();
		communalCards[3] = deck.getDeck().poll();
	}
	
	private void river() {
		burn();
		communalCards[4]= deck.getDeck().poll();

	}
	
	private void burn() {
		deck.getDeck().poll();
		
	}
	
	private void printTable() {
		for(Player player: players) {
			System.out.println(player.getName() + " " + "$" +player.getChips());
			
			for(Card c: player.getCards()) {
				System.out.print(c + " ");
			}
			
			System.out.println("\n");
		}
		
		for(Card c: communalCards) {
			System.out.print(c + " ");
		}
		System.out.println("\n");
	}

}
