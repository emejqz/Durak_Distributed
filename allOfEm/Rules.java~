package model;

import java.io.Serializable;

/*
 * @author Eric M Evans
 * 
 * Contains Durak specific and game specific rules and logic.
 */
public class Rules implements Serializable {
	
	private static final long	serialVersionUID	= -3263307938341250778L;
	private static final int	MAX_PLAYERS			= 8;
	private static final int	HAND_SIZE			= 6;
	
	private Suit				trump;
	private CardTable			cardTable;
	
	
	
	public Rules(CardTable cardTable, Suit newtrump) {
		
		this.cardTable = cardTable;
		
		this.trump = newtrump;
	}
	
	
	
	public Suit getTrump() {
		
		return trump;
	}
	
	
	
	public static int getMaxPlayers() {
		
		return MAX_PLAYERS;
	}
	
	
	
	/*
	 * throws InvalidPassException if passee does not have enough cards to pass
	 * to
	 */
	public void validatePass(Card pass) throws InvalidPassException {
		
		final int cardsToBeat = cardTable.getNumUnbeatenAttacks() + 1;
		
		if (!cardTable.getUnbeatenAttackRanks().contains(pass.getRank()))
			throw new InvalidPassException(
			        "That Rank Is Not Among The Unbeaten Attack Cards");
		
		if (cardTable.getPaseeHandSize() < cardsToBeat)
			throw new InvalidPassException(
			        "Passee Does Not Have Enough Cards To Pass To");
		
	}
	
	
	
	/*
	 * throws InvalidDefenseException if defense card is no a superior card to
	 * the attack card
	 */
	public void validateDefense(Card defense, Card attack)
	        throws InvalidDefenseException {
		
		if (!defense.isSuit(this.trump)
		        && !defense.getSuit().equals(attack.getSuit()))
			throw new InvalidDefenseException(
			        "Defense Card Suit Must Match Attack Card Suit");
		
		if (defense.compareTo(attack) <= 0)
			throw new InvalidDefenseException(
			        "Defense Card Does Not Beat Attack Card");
		
	}
	
	
	
	/*
	 * throws InvalidAttackException if defender does not have enough cards to
	 * be attacked
	 */
	public void validateAttack(Card attack) throws InvalidAttackException {
		
		final int cardsToBeat = cardTable.getNumUnbeatenAttacks() + 1;
		
		// System.out.println("cards to beat: " + cardsToBeat);
		// System.out.println("defender hand: " +
		// cardTable.getDefendersHandSize());
		
		if (cardTable.getDefendersHandSize() < cardsToBeat)
			throw new InvalidAttackException(
			        "Cannot Attack - Defender Does Not Have Enough Cards To Be Attacked");
		
		if (!cardTable.getAttacks().isEmpty()
		        && !cardTable.getCurrentRanks().contains(attack.getRank()))
			throw new InvalidAttackException(
			        "Cannot Attack - That Rank Is Not Currently In Play");
		
	}
	
	
	
	public static int getHandSize() {
		
		return HAND_SIZE;
	}
	
}
