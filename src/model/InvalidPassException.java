package model;

/*
 * @author Eric M Evans
 * 
 * For Invlid Passing Attempts
 */
public class InvalidPassException extends Exception {
	
	public String getReason() {
		
		return this.reason;
	}
	
	
	
	private static final long	serialVersionUID	= -2234572629948754374L;
	private String				reason;
	
	
	
	public InvalidPassException(String str) {
		
		this.reason = str;
	}
}
