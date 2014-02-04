package cards;

public abstract class Player {
	
	private String name;
	private int chips;
	private int betThisHand;
	private Card[] holeCards;
	private Hand madeHand;
	private boolean hasMadeBet;
	
	
	public Player(String name, int chips) {
		this.name=name;
		this.chips=chips;
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
	}
	final public void setBetThisHand(int betThisHand) {
		this.betThisHand = betThisHand;
	}
	final public int getBetThisHand() {
		return betThisHand;
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
	final public void resetPlayerBets() {
		betThisHand = 0;
		hasMadeBet = false;
	}
	
	/*
	 * Pay Blinds
	 * @return 0 if affordable or short-fall if not
	 */
	final public int payBlind(int amount) {
		if(amount > chips) {
			chips = 0;
			return amount-chips;
		}
		
		chips -= amount;
		return 0;
	}
	
	 public String toString() {
		return name;
	}
	
	abstract int decideBet(int currentBet, int bigBlind);

}
