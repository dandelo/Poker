package cards;

import java.util.*;


class Table {
	
	private static final int STARTING_CHIPS = 100;
	private static final String[] COMPUTER_PLAYERS = {"Dylan","Lola","Rupert"};
	
	private Deck deck;
	private List<Player> players, playersInHand, winners;
	private List<Card> communalCards;
	private int pot = 0;
	private int dealerPosition = (int) (Math.random()*2);
	private int smallBlind = 5;		// bigBlind=smallBlind*2

	public Table() {
		communalCards =  new ArrayList<Card>();
		
		players = new ArrayList<Player>();
		
		winners = new ArrayList<Player>();
		for(String s : COMPUTER_PLAYERS) {
			players.add(new NPC(s,STARTING_CHIPS));
		}

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Table table = new Table();
		int handsPlayed = 0;
		
		if (args.length == 1) {
			table.players.add(new Human(args[0],STARTING_CHIPS, System.in));
		}

		while(table.players.size() > 1) {
			table.dealRound();
			if(handsPlayed % 20 == 0) table.smallBlind*=2;
			handsPlayed++;
		}
	}
	
	/*
	 * Deals a single round
	 */
	private void dealRound() {
		
		deck = new Deck();
		Collections.shuffle(deck.getDeck());
		
		playersInHand = new ArrayList<Player>(players);
		
		payBlinds();
		dealPlayerCards();
		dealCommunalCards(3);
		dealCommunalCards(1);
		dealCommunalCards(1);
				
		printTable(true);
		if(playersInHand.size() > 1) {
			winners = findWinner();
			printWinner(winners);
		}
		else {
			winners.clear();
			winners.add(playersInHand.get(0));
		}
		payWinner(winners);
		
		for(int i=0; i<players.size(); i++) {
			if(players.get(i).getChips() == 0) players.remove(i);
		}
		
		if(dealerPosition +1 > players.size()-1) {
			dealerPosition = 0;
		}
		else dealerPosition++;
		
		communalCards.clear();
	}

	private void payBlinds() {
		// TODO condition for player chips < blind (covered pot)
		int shortfall1, shortfall2;
		
		if((dealerPosition +1) >= players.size()) {
			shortfall1 = players.get(0).payBlind(smallBlind);
			shortfall2 = players.get(1).payBlind(smallBlind*2);
		}
		else if((dealerPosition +2) >= players.size()) {
			shortfall1 = players.get(dealerPosition+1).payBlind(smallBlind);
			shortfall2 = players.get(0).payBlind(smallBlind*2);
		}
		else {
			shortfall1 = players.get(dealerPosition+1).payBlind(smallBlind);
			shortfall2 = players.get(dealerPosition+2).payBlind(smallBlind*2);
		}
		pot+= smallBlind*3 - shortfall1 - shortfall2;
	}

	private List<Player> findWinner() {
		List<Player> winners = new ArrayList<Player>();

		TreeSet<Hand> allPlayerHands = new TreeSet<Hand>();
		
		for(Player player: playersInHand) {
			Card[] cards = new Card[7];
			for(int i=0; i<communalCards.size();i++) {
				cards[i]= communalCards.get(i);
			}
			cards[5] = player.getCards()[0];
			cards[6] = player.getCards()[1];
			
			player.setMadeHand(new Hand(cards));
			allPlayerHands.add(player.getMadeHand());
		}
		
		for(Player player: playersInHand) {
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
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void payWinner(List<Player> winners) {
		for(Player p: winners) {
			p.addChips(pot/winners.size());
		}
		pot=0;
	}

	private void dealPlayerCards() {
		for(int i=0; i<=1; i++) {
			for(int j=dealerPosition+1; j<players.size(); j++) {
				players.get(j).setCards(((LinkedList<Card>) deck.getDeck()).poll(), i);
			}
			for(int j=0; j<=dealerPosition; j++) {
				players.get(j).setCards(((LinkedList<Card>) deck.getDeck()).poll(), i);
			}
		}
		printTable(false);
		collectBets();
	}
	
	private void dealCommunalCards(int cardsToDeal) {
		if (playersInHand.size() > 1) {
			burn();
			for(int i=0; i<cardsToDeal; i++) {
				communalCards.add(((LinkedList<Card>) deck.getDeck()).poll());
			}
			printTable(false);
			collectBets();
		}
	}
	
	private void burn() {
		((LinkedList<Card>) deck.getDeck()).poll();
	}
	
	private void collectBets(){
		int currentBet = 0;
		int totalBet = 0;

		int i = dealerPosition+1;
		
		while(true) {
			if(i >= playersInHand.size()) {   //cyclic loop until all bets collected
				i=0;
			}
			if((playersInHand.get(i).getBetThisHand() == currentBet && playersInHand.get(i).hasMadeBet()) || (playersInHand.size() == 1)) {
				break;
			}
			if(playersInHand.get(i).getChips() == 0) {
				playersInHand.get(i).setMadeBet(true);
				i++;
				continue;
			}
			
			int playerBet = playersInHand.get(i).decideBet(currentBet,smallBlind*2);

			if (playerBet == -1) {		//player folded
				playersInHand.remove(i);
				i--;
			} else if(playerBet < currentBet) {		//insufficient funds (covered pot)
				totalBet += playerBet;
			}
			else {
				currentBet = playerBet;
				totalBet += playerBet;
			}
			
			i++;
		}
		
		pot += totalBet;
		
		for(Player p: players) {
			p.resetPlayerBets();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void printTable(boolean showHidden) {
		final String ESC = "\033[";
		System.out.print(ESC + "2J"); 		
		
		for(Player player: players) {
			System.out.println(player.getName() + " " + "$" +player.getChips());
			
			for(Card c: player.getCards()) {
				if(showHidden || player instanceof Human) {
					System.out.print(c + " ");
				}
				else {
					System.out.print("🂠 ");
				}
			}
			
			System.out.println("\n");
		}
		
		System.out.print(communalCards);
		System.out.println("\nPot: $" + pot + "\n");
	}

}
