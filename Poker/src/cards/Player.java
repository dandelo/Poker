package cards;

public abstract class Player implements Comparable<Player> {

	private final String name;
	private int chips, potContribution, betThisRound, lastWinSize;
	private final Card[] holeCards;
	private Hand madeHand;
	private boolean hasMadeBet, showCards;


	public Player(String name, int chips) {
		this.name = name;
		this.chips = chips;
		holeCards = new Card[2];
	}

	final public String getName() {
		return name;
	}
	final public int getChips() {
		return chips;
	}
	final protected void setChips(int chips) {
		this.chips = chips;
	}
	final public void addChips(int chips) {
		this.chips += chips;
		lastWinSize += chips;
	}
	final public int getLastWinSize() {
		return lastWinSize;
	}
	final public int getPotContributionMinusBet(int toPay) {
		if(toPay > potContribution) {
			int tmp = potContribution;
			potContribution = 0;
			return tmp;
		}
		else {
			potContribution -= toPay;
			return toPay;
		}
	}
	final public int getBetThisRound() {
		return betThisRound;
	}
	final public int getPotContribution() {
		return potContribution;
	}
	final public Card[] getCards() {
		return holeCards;
	}
	final public void setCards(Card card, int index) {
		holeCards[index]=card;
	}
	final public void setMadeHand(Hand hc) {
		madeHand = hc;
	}
	final public Hand getMadeHand() {
		return madeHand;
	}
	final public boolean hasMadeBet() {
		return hasMadeBet;
	}
	final public void setMadeBet(boolean hasMadeBet) {
		this.hasMadeBet = hasMadeBet;
	}
	final public boolean willShowCards() {
		return showCards;
	}
	final public void setShowCards(boolean showCards) {
		this.showCards = showCards;
	}
	final public void resetBetsAfterRound() {
		betThisRound = 0;
		hasMadeBet = false;
	}
	final public void ResetBetThisHand() {
		potContribution = 0;
		showCards = true;
		lastWinSize = 0;
	}

	/*
	 * Pay Blinds
	 * @return 0 if affordable or short-fall if not
	 */
//	final public int payBlind(int amount) {
//		if(amount > chips) {
//			chips = 0;
//			return amount-chips;
//		}
//
//		chips -= amount;
//		return 0;
//	}

	@Override
	public String toString() {
		return name + " ($" + chips + ")";
	}

	final public boolean affordable(int toPay) {
		return toPay <= chips;
	}

	final protected void makeBet(int toPay) {
		chips -= toPay;
		potContribution += toPay;
		betThisRound += toPay;
		hasMadeBet = true;
	}

	@Override
	final public int compareTo(final Player p) {
		return Integer.compare(this.potContribution, p.potContribution);
	}

	abstract int decideBet(int currentBet, int bigBlind);
	abstract String getLastAction();

}
