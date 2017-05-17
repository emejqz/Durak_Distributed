//package model;

/*
 * @author Eric M Evans
 * 
 * Exception for Invalid Defenses
 */
public class InvalidDefenseException extends Exception {
	
	private static final long	serialVersionUID	= 5912747587843236028L;
	private String				reason;
	
	
	
	public InvalidDefenseException(String string) {
		
		this.reason = string;
	}
	
	
	
	public String getReason() {
		
		return this.reason;
	}
	
}
