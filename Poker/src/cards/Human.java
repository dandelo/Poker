package cards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Human extends Player {
	
	InputStream input;
	
	public Human(String name, int chips, InputStream input) {
		super(name,chips);
		this.input = input;
	}

	@Override
	int decideBet(int currentBet, int bigBlind) {
		int bet = currentBet;
		
		if (bet > getChips()) {
			bet = getChips();
			makeBet(bet);
			return bet;
		}
		
		boolean validBet = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		
		int betAttempts = 0;
		do {
			switch(betAttempts) {
			case(2): 	System.out.println("Two failed bet attemps, a third will result in an automatic fold.");
						break;
			case(3): 	System.out.println("Folded");
						return -1;
			}
			
			try {
				System.out.println("Current bet is $" + currentBet + "\nEnter bet: ");
				
				String enteredBet = br.readLine();
				if (enteredBet.equalsIgnoreCase("deck")) {
					return -1;
				}
				bet = Integer.parseInt(enteredBet);
				
				if (bet > getChips()) {
					System.out.println("You do not have enough chips for that bet, you only have $" + getChips());
				}
				else if (bet < currentBet) {
					System.out.println("Bet too low, current bet is $" + currentBet);
				}
				else {
					validBet = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Bet not recognised, please enter an int value");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				betAttempts++;
			}
		} while (!validBet);
		
		makeBet(bet);

		return bet;
	}

	private void makeBet(int bet) {
		setChips(getChips() - bet);
		setBetThisHand(getBetThisHand() + bet);
		setMadeBet(true);
	}
	
}