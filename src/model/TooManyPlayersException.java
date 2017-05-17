package model;

/*
 * @author Eric M Evans
 * 
 * For when too many players are chosen to start a game
 */
public class TooManyPlayersException extends Exception {
	
	private static final long serialVersionUID = -6293649712385789175L;
	
	
	public String getReason(){
		return "Too Few or Too Many Players to Play Durak";
	}
}
