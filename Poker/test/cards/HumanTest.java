package cards;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class HumanTest {

	@Test
	public void testValidDecideBet() {
		
		
        byte[] data = "123".getBytes();
        
        InputStream input = new ByteArrayInputStream(data);
        
		Human me = new Human("Me", 200, input);

        int madeBet = me.decideBet(10,10);

        assertEquals(123, madeBet);
	}
	
	@Test
	public void testDecideBetWithInsuffientFunds() {
		
		
        byte[] data = "201\n201\n201".getBytes();
        
        InputStream input = new ByteArrayInputStream(data);
        
		Human me = new Human("Me", 200, input);

        int madeBet = me.decideBet(10,10);

        assertEquals(-1, madeBet);
	}
	
	@Test
	public void testDecideBetWithLessThanCurrentBet() {
		
		
        byte[] data = "20\n20\n20".getBytes();
        
        InputStream input = new ByteArrayInputStream(data);
        
		Human me = new Human("Me", 200, input);

        int madeBet = me.decideBet(21,10);

        assertEquals(-1, madeBet);
	}
	
	@Test
	public void testDecideBetWithValidBetOnThirdAttempt() {
		
		
        byte[] data = "201\n20\n40".getBytes();
        
        InputStream input = new ByteArrayInputStream(data);
        
		Human me = new Human("Me", 200, input);

        int madeBet = me.decideBet(21,10);

        assertEquals(40, madeBet);
	}
	
	@Test
	public void testDecideBetWithText() {
		
		
        byte[] data = "twenty\nblah\nblah".getBytes();
        
        InputStream input = new ByteArrayInputStream(data);
        
		Human me = new Human("Me", 200, input);

        int madeBet = me.decideBet(21,10);

        assertEquals(-1, madeBet);
	}
	
	@Test
	public void testDecideBetWithCoveringPot() {
		
		
        byte[] data = "NotUsed".getBytes();
        
        InputStream input = new ByteArrayInputStream(data);
        
		Human me = new Human("Me", 20, input);

        int madeBet = me.decideBet(21,10);

        assertEquals(20, madeBet);
	}

}
