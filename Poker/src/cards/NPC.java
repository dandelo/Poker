package cards;

import java.util.ArrayList;
import java.util.List;

public class NPC extends Player {
	
	private int foldWeight, callWeight, raiseWeight, raiseAmount;
	private final int FOLD_BET=0, CALL_BET=1, RAISE_BET=2;


	
	public NPC(String name, int chips) {
		super(name,chips);
	}

	@Override
	int decideBet(int currentBet, int bigBlind) {		
		if (!affordable(currentBet)) {
			makeBet(getChips());
			return getChips();
		}
		
		int bet = currentBet;
		
		decideActionWeights(bet,bigBlind);
		
		int decision = getWeightedRandomNumber();
		
		switch(decision) {
			case(FOLD_BET):		return -1;
			case(CALL_BET):		makeBet(currentBet);
								return currentBet;
			case(RAISE_BET):	makeBet(raiseAmount);
								return raiseAmount;
		}
		
		makeBet(bet);
		return bet;
	}

	private boolean affordable(int bet) {
		return bet <= getChips();
	}

	private void decideActionWeights(int currentBet, int bigBlind) {
		// TODO Make this smart
		foldWeight = 20;
		callWeight = 60;
		raiseWeight = 20;
		raiseAmount =  Math.max(bigBlind*2, currentBet*2);
		if(!affordable(raiseAmount)) {
			raiseAmount = getChips();
		}
	}

	private int getWeightedRandomNumber() {
		List<Integer> weightedDecisionSelector = new ArrayList<Integer>();
		
		for(int i=0; i<foldWeight; i++) {
			weightedDecisionSelector.add(FOLD_BET);
		}
		for(int i=0; i<callWeight; i++) {
			weightedDecisionSelector.add(CALL_BET);
		}
		for(int i=0; i<raiseWeight; i++) {
			weightedDecisionSelector.add(RAISE_BET);
		}
		
		int index = (int)(Math.random()*weightedDecisionSelector.size());
		return weightedDecisionSelector.get(index);
	}

	private void makeBet(int bet) {
		setChips(getChips() - bet);
		setBetThisHand(getBetThisHand() + bet);
		setMadeBet(true);
	}
	
}
