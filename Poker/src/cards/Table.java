package cards;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;



class Table {

	private static final int STARTING_CHIPS = 100;
	private static final int HANDS_PER_BLIND = 20;
	private static final int STARTING_BLIND = 5;
	private static final String[] COMPUTER_PLAYERS = {"Dylan", "Lola", "Rupert"};

	private Deck deck;
	private final List<Player> players;
	private List<Player> playersInHand;
	private List<List<Player>> winners;
	private final List<Card> communalCards;
	private int pot = 0;
	private int dealerPosition = (int) (Math.random() * 2);
	private int smallBlind = STARTING_BLIND;		// bigBlind=smallBlind*2

	public Table() {
		communalCards =  new ArrayList<Card>();
		players = new ArrayList<Player>();
		winners = new ArrayList<List<Player>>();
	}


	/**
	 * @param args name of player
	 */
	public static void main(final String[] args) {
		Table table = new Table();
		int handsPlayed = 0;

		for (String s : COMPUTER_PLAYERS) {
			table.addPlayer(new NPC(s, STARTING_CHIPS));
		}
		if (args.length == 1) {
			table.addPlayer(new Human(args[0], STARTING_CHIPS, System.in));
		}

		while (table.players.size() > 1) {
			table.dealRound();
			if (handsPlayed % HANDS_PER_BLIND == 0) {
				table.smallBlind *= 2;
			}
			handsPlayed++;
		}
//System.out.println("Tournament Winner: " + table.players.get(0) + " $" + table.players.get(0).getChips());
		System.out.println("Tournament Winner: " + table.players.get(0));
	}

	/**
	 * @param player - Player to add to table
	 */
	public void addPlayer(final Player player) {
		players.add(player);
	}

	/*
	 * Deals a single round
	 */
	private void dealRound() {

		deck = new Deck();
		Collections.shuffle(deck.getDeck());

		playersInHand = new ArrayList<Player>(players);

		for (Player p : playersInHand) {
			p.ResetBetThisHand();
		}

		dealPlayerCards();
		dealCommunalCards(3);
		dealCommunalCards(1);
		dealCommunalCards(1);

		printTable(true);

		//TODO replace below with something that works when hand doesn't finish.
		if (playersInHand.size() > 1) {
			winners = findWinner();
		} else {
			List<Player> soleWinner = new ArrayList<Player>();
			soleWinner.add(playersInHand.get(0));
			List<Player> losers = new ArrayList<Player>();
			for (Player p: players) {
				if (p != playersInHand.get(0)) {
					losers.add(p);
				}
			}
			winners.add(soleWinner);
			winners.add(losers);
		}
		payWinner(winners);
		printWinner(winners);


		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getChips() == 0) {
				players.remove(i);
			}
		}

		if (dealerPosition + 1 > players.size() - 1) {
			dealerPosition = 0;
		} else {
			dealerPosition++;
		}

		communalCards.clear();
		winners.clear();
	}

	private void dealPlayerCards() {
		for (int i = 0; i <= 1; i++) {
			for (int j = dealerPosition + 1; j < players.size(); j++) {
				players.get(j).setCards(((LinkedList<Card>) deck.getDeck()).poll(), i);
			}
			for (int j = 0; j <= dealerPosition; j++) {
				players.get(j).setCards(((LinkedList<Card>) deck.getDeck()).poll(), i);
			}
		}
		printTable(false);
		payBlinds();
		collectBets(true);
	}

	private void dealCommunalCards(int cardsToDeal) {
		if (playersInHand.size() > 1) {
			burn();
			for (int i = 0; i < cardsToDeal; i++) {
				communalCards.add(((LinkedList<Card>) deck.getDeck()).poll());
			}
			printTable(false);
			collectBets(false);
		}
	}

	private void burn() {
		((LinkedList<Card>) deck.getDeck()).poll();
	}

	private void payBlinds() {
		for (int blindLevel = 1, index = dealerPosition + 1; blindLevel <= 2; blindLevel++, index++) {
			if (index == players.size()) {
				index = 0;
			}
			int bet = smallBlind * blindLevel;
			if (players.get(index).getChips() < bet) {
				bet = players.get(index).getChips();
			}
			players.get(index).makeBet(bet);
			//pot += bet;

			if (blindLevel == 1) {
				System.out.println(players.get(index).getName() + " posts Small Blind $" + smallBlind);
			} else {
				System.out.println(players.get(index).getName() + " posts Big Blind $" + (smallBlind * blindLevel) + "\n");
			}
		}
	}

	private void collectBets(boolean preFlop){
		if (playersLeftToBet() < 2) {
			return;
		}
		final int skipOverBlinds = 3;
		int currentBet, playerToAct;
		//TODO blinds shoulnd't set madeBet to true
		if (preFlop) {
			currentBet = smallBlind * 2;
			playerToAct = dealerPosition + skipOverBlinds;
		} else {
			currentBet = 0;
			playerToAct = dealerPosition + 1;
		}

		int totalBet = smallBlind * 3;

		do {
			if (playerToAct >= playersInHand.size()) {   //cyclic loop until all bets collected
				playerToAct %= playersInHand.size();
			}
			if (playersInHand.get(playerToAct).getChips() == 0) {
				playersInHand.get(playerToAct).setMadeBet(true);
				playerToAct++;
				continue;
			}
			Player actingPlayer = playersInHand.get(playerToAct);

			int playerBet = actingPlayer.getBetThisRound() + actingPlayer.decideBet(currentBet - actingPlayer.getBetThisRound(), smallBlind * 2);

			if (playerBet == -1) {		//player folded
				playersInHand.remove(playerToAct);
				System.out.println(actingPlayer.getName() + " " + actingPlayer.getLastAction());
				continue;
			}
			totalBet += (actingPlayer.getBetThisRound() - playerBet);
			if (playerBet > currentBet) {
				currentBet = playerBet;
			}
			System.out.println(actingPlayer.getName() + " " + actingPlayer.getLastAction());
			playerToAct++;
		} while(!haveCollectedAllBets(currentBet, (playerToAct)));

		System.out.println();

//		if (preFlop) {
//			totalBet -= smallBlind * 3;
//		}
//		pot += totalBet;

		for (Player p: players) {
			pot += p.getBetThisRound();
			p.resetBetsAfterRound();
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int playersLeftToBet() {
		int playersLeftToBet = 0;
		for (Player p: playersInHand) {
			if (p.getChips() > 0) {
				playersLeftToBet++;
			}
		}
		return playersLeftToBet;
	}

	private boolean haveCollectedAllBets(int currentBet, int i) {
		if (i >= playersInHand.size()) {
			i = 0;
		}
		return (playersInHand.size() == 1
				|| playersInHand.get(i).getChips() == 0
				|| (playersInHand.get(i).hasMadeBet() && playersInHand.get(i).getBetThisRound() == currentBet));
	}

	private List<List<Player>> findWinner() {
		List<List<Player>> rankedPlayers = new ArrayList<List<Player>>();
		for (int i = 0; i < players.size(); i++) {
			rankedPlayers.add(new ArrayList<Player>());
		}

		//TreeSet<Hand> sortedPlayerHands = new TreeSet<Hand>();
		List<Hand> sortedPlayerHands = new ArrayList<Hand>();

		for (Player player: playersInHand) {
			Card[] cards = new Card[7];
			for (int i = 0; i < communalCards.size(); i++) {
				cards[i] = communalCards.get(i);
			}
			cards[5] = player.getCards()[0];
			cards[6] = player.getCards()[1];

			player.setMadeHand(new Hand(cards));
			sortedPlayerHands.add(player.getMadeHand());
		}

		Collections.sort(sortedPlayerHands);

		// TODO above and below should be separate methods

		for (Player player: playersInHand) {
			int position = sortedPlayerHands.indexOf(player.getMadeHand());
			List<Player> playerHandRanking = rankedPlayers.get(position);
			playerHandRanking.add(player);
			rankedPlayers.set(position, playerHandRanking);
			//if(player.getMadeHand().equals(sortedPlayerHands.first())) {
			//	winners.add(player);
			//}
		}
		int position = new ArrayList<Hand>(sortedPlayerHands).size() - 1;
		List<Player> playerHandRanking = rankedPlayers.get(position);
		for (Player player: players) {
			if (playerFolded(player)) {
				playerHandRanking.add(player);
			}
		}
		rankedPlayers.set(position, playerHandRanking);
		System.out.print(rankedPlayers);

		return rankedPlayers;
	}


	private boolean playerFolded(Player player) {
		return !playersInHand.contains(player);
	}

	public void payWinner(final List<List<Player>> rankedPlayers) {
		do {
			List<Player> playersToPay = rankedPlayers.get(0);
			Collections.sort(playersToPay);
			do {
				int numberOfWinners = playersToPay.size();
				Player smallestWinner = playersToPay.get(0);
				int shareOfPot = smallestWinner.getPotContributionMinusBet(smallestWinner.getPotContribution());
				int winnings = shareOfPot;

				for (Player p : players) {
					winnings += p.getPotContributionMinusBet(shareOfPot);
				}

				for (int i = 0; i < playersToPay.size();) {
					playersToPay.get(i).addChips(winnings / numberOfWinners);
					if (playersToPay.get(i).getPotContribution() == 0) {
						playersToPay.remove(i);
					} else {
						i++;
					}
				}
				pot -= winnings;
			} while(playersToPay.size() > 0);

			rankedPlayers.remove(0);
		} while(pot > 0);
	}

	private void printTable(boolean showDown) {
		final String ESC = "\033[";
		System.out.print(ESC + "2J");

		for (Player player: playersInHand) {
			//System.out.println(player.getName() + " " + "$" + player.getChips());
			System.out.println(player);

			for (Card c: player.getCards()) {
				if (showDown && player.willShowCards()) {
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

	private void printWinner(List<List<Player>> winners) {

		System.out.print("\nWinners: ");
		//for (List<Player> rank : winners) {
		//	for (Player p: rank) {
		for (Player p: playersInHand) {
				if (p.willShowCards() && p.getLastWinSize() > 0) {
					System.out.print(p.getName() + " " + "$" + p.getLastWinSize() + " ");
				}
		//	}
		}

		System.out.println("\n---------------\n\n");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

}
