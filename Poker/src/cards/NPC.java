package cards;

import java.util.ArrayList;
import java.util.List;

public class NPC extends Player {

	private int foldWeight, callWeight, raiseWeight, raiseAmount;
	private final int FOLD_BET=0, CALL_BET=1, RAISE_BET=2;
	private String lastAction;



	public NPC(String name, int chips) {
		super(name,chips);
	}

	@Override
	int decideBet(final int toPay, final int bigBlind) {
		if (!affordable(toPay)) {
			makeBet(getChips());
			return getChips();
		}

		decideActionWeights(toPay,bigBlind);

		int decision = getWeightedRandomNumber();

		switch(decision) {
			case(FOLD_BET):		setShowCards(false);
								lastAction = "Fold";
								return -1;
			case(CALL_BET):		makeBet(toPay);
								if (toPay == 0) {
									lastAction = "Check";
								} else {
									lastAction = "Call $" + toPay;
								}
								return toPay;
			case(RAISE_BET):	makeBet(raiseAmount);
								lastAction = "Raise to $" + raiseAmount;
								return raiseAmount;
			default: throw new Error("WTF");
		}

	}

	private void decideActionWeights(final int currentBet, final int bigBlind) {
		// TODO Make this smart
		if (currentBet == 0) {
			foldWeight = 0;
		}
		else {
			foldWeight = 20;
		}

		callWeight = 60;
		raiseWeight = 20;
		raiseAmount =  Math.max(bigBlind * 2, currentBet * 2);
		if (!affordable(raiseAmount)) {
			raiseAmount = getChips();
		}
	}

	private int getWeightedRandomNumber() {
		List<Integer> weightedDecisionSelector = new ArrayList<Integer>();

		for (int i = 0; i < foldWeight; i++) {
			weightedDecisionSelector.add(FOLD_BET);
		}
		for (int i = 0; i < callWeight; i++) {
			weightedDecisionSelector.add(CALL_BET);
		}
		for (int i = 0; i < raiseWeight; i++) {
			weightedDecisionSelector.add(RAISE_BET);
		}

		int index = (int) (Math.random() * weightedDecisionSelector.size());
		return weightedDecisionSelector.get(index);
	}

	@Override
	String getLastAction() {
		return lastAction;
	}



}
