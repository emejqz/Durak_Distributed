package model;

import java.io.Serializable;
/*
 * @author Eric M Evans
 * 
 * For storing attack cards paired with corresponding defense card
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BattleCards implements Serializable {
	
	private static final long	serialVersionUID	= 1211025106761571259L;
	private List<BattlePair>	pairs				= new ArrayList<>();
	private Rules				rules;
	
	
	
	public BattleCards(Rules rules) {
		
		this.rules = rules;
	}
	
	
	
	/*
	 * add attack card
	 */
	public int attack(Card card) throws InvalidAttackException {
		
		rules.validateAttack(card);
		
		addAttackCard(card);
		return pairs.size();
	}
	
	
	
	/*
	 * add card to battle pair
	 */
	private void addAttackCard(Card card) {
		
		pairs.add(new BattlePair(card));
	}
	
	
	
	/*
	 * for defending an attack
	 * returns true if defending is valid
	 */
	public void defend(Card defense, Card attack)
	        throws InvalidDefenseException {
		
		BattlePair pair = getPairByAttackCard(attack);
		
		if (pair == null)
			throw new InvalidDefenseException(
			        "Indicated Attack Card Is Not Present");
		
		pair.defendWith(defense); // may throw InvalidDefenseException
	}
	
	
	
	/*
	 * for passing battle cards
	 * returns true if passing is valid
	 */
	public void pass(Card card) throws InvalidPassException {
		
		rules.validatePass(card);
		this.addAttackCard(card);
		
	}
	
	
	
	/*
	 * return number of unbeaten attack cards
	 */
	int getNumUnbeatenAttacks() {
		
		int n = 0;
		
		for (BattlePair pair : pairs) {
			// System.out.println(pair.getAttack());
			if (!pair.hasDefenseCard()) {
				n++;
			}
			// else
			// System.out.println("\t" + pair.getDefense());
		}
		
		return n;
	}
	
	
	
	/*
	 * return true if all attack cards were beaten aka defender won
	 */
	public boolean endBattle() {
		
		for (BattlePair pair : pairs)
			if (!pair.hasDefenseCard())
				return false;
			
		return true;
	}
	
	
	
	/*
	 * return all cards of all pairs and reset pairs
	 */
	public CardList getAllCardsAndReset() {
		
		CardList all = new CardList();
		
		for (BattlePair pair : pairs) {
			all.add(pair.getAttack());
			
			if (pair.hasDefenseCard())
				all.add(pair.getDefense());
		}
		
		// reset
		this.pairs = new ArrayList<>();
		
		return all;
	}
	
	
	
	/*
	 * return CardList of attack cards
	 */
	public CardList getAttackCards() {
		
		CardList attacks = new CardList();
		
		for (BattlePair pair : pairs)
			attacks.add(pair.getAttack());
		
		return attacks;
	}
	
	
	
	/*
	 * return Card corresponding to argument Card, may be null
	 */
	public Card getDefenseCard(Card attack) {
		
		BattlePair pair = getPairByAttackCard(attack);
		
		if (pair == null)
			return null;
		
		return pair.getDefense();
	}
	
	
	
	/*
	 * returns true if card is in pairs as an attack card
	 */
	private BattlePair getPairByAttackCard(Card card) {
		
		for (BattlePair pair : pairs)
			if (pair.getAttack().equals(card))
				return pair;
			
		return null;
	}
	
	
	
	/*
	 * return stylized String of battle cards
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		StringBuilder strb = new StringBuilder();
		
		for (BattlePair pair : pairs) {
			strb.append("[");
			
			strb.append(pair.getAttack() + " ");
			
			strb.append("vs ");
			
			if (pair.hasDefenseCard())
				strb.append(pair.getDefense() + "");
			else
				strb.append("none");
			
			strb.append("] ");
		}
		
		return strb.toString();
	}
	
	
	
	/*
	 * holds attack card and defense card, in a pair
	 */
	private class BattlePair implements Serializable {
		
		private static final long	serialVersionUID	= 9180831708036024741L;
		private Card				attack;
		private Card				defense;
		
		
		
		public Card getAttack() {
			
			return this.attack;
		}
		
		
		
		private boolean hasDefenseCard() {
			
			return defense != null;
		}
		
		
		
		public BattlePair(Card attack) {
			this.attack = attack;
		}
		
		
		
		public Card getDefense() {
			
			return this.defense;
		}
		
		
		
		public void defendWith(Card defense) throws InvalidDefenseException {
			
			if (hasDefenseCard())
				throw new InvalidDefenseException(
				        "Attack Card Has Already Been Beaten");
			
			rules.validateDefense(defense, getAttack());
			
			this.defense = defense;
		}
		
	}
	
	
	
	/*
	 * return true if no cards have been played
	 */
	public boolean isEmpty() {
		
		return pairs.isEmpty();
	}
	
	
	
	/*
	 * return all current ranks in cards in play
	 */
	public Set<Rank> getAllRanks() {
		
		Set<Rank> ranks = new HashSet<>();
		
		for (BattlePair pair : this.pairs) {
			ranks.add(pair.getAttack().getRank());
			if (pair.hasDefenseCard())
				ranks.add(pair.getDefense().getRank());
		}
		
		return ranks;
	}
	
	
	
	/*
	 * return all current ranks in attack cards in play
	 */
	public Set<Rank> getUnbeatenAttackRanks() {
		
		Set<Rank> ranks = new HashSet<>();
		
		for (BattlePair pair : this.pairs)
			if (!pair.hasDefenseCard())
				ranks.add(pair.getAttack().getRank());
			
		return ranks;
	}
	
}
