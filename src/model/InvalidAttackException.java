package model;

/*
 * @author Eric M Evans
 * 
 * For invalid attacks, too few cards in attackee's hand
 */
public class InvalidAttackException extends Exception {
	
	private static final long	serialVersionUID	= 1023832951646199425L;
	private String				reason;
	
	
	
	public InvalidAttackException(String string) {
		this.reason = string;
	}
	
	
	
	public String getReason() {
		
		return this.reason;
	}
	
}
