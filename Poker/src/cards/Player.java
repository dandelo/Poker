package cards;

class Player {
	
	private String name;
	private int chips;
	private Card[] holeCards;
	private Hand madeHand;
	
	
	public Player(String name, int chips) {
		this.name=name;
		this.chips=chips;
		holeCards = new Card[2];
	}
	
	public String getName() {
		return name;
	}
	public int getChips() {
		return chips;
	}
	public void addChips(int chips) {
		this.chips += chips;
	}
	public Card[] getCards() {
		return holeCards;
	}
	public void setCards(Card card, int index) {
		holeCards[index]=card;
	}
	public void setMadeHand(Hand hc) {
		madeHand = hc;
	}
	public Hand getMadeHand() {
		return madeHand;
	}
	
	/*
	 * Make a bet.
	 * @return 0 if affordable or short-fall if not
	 */
	public int bet(int bet) {
		if(bet > chips) {
			chips = 0;
			return bet-chips;
		}
		
		chips -= bet;
		return 0;
	}

}
