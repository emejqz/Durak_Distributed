package model;

import java.io.Serializable;
/*
 * @author Eric M Evans
 * 
 * For organizing all cards in Durak game
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CardTable implements Serializable{
	
	private static final long serialVersionUID = 5999980966813912374L;
	private Deck			talon;
	private CardList		discard		= new CardList();
	private BattleCards		battleCards	= null;				// set
	                                                        // in
	                                                        // constructor
	private List<CardList>	hands		= new ArrayList<>();
	// private List<CardList> predrawPiles = null; // set
	// in
	// constructor
	private int				defender	= 1;				// begin
	                                                        // with
	private int				attacker	= -1;				// attacker
	                                                        // @ 0
	
	private Rules			rules		= null;				// set
	                                                        // in
	                                                        // constructor
	private Card			trump;
	// private int defenderPredrawDeficit = 0;
	private boolean[]		finished;
	
	
	
	public CardTable(int numPlayers) throws TooManyPlayersException {
		
		if (numPlayers > Rules.getMaxPlayers() || numPlayers < 3)
			throw new TooManyPlayersException();
		
		talon = new Deck();
		
		for (int i = 0; i < numPlayers; i++)
			hands.add(talon.deal(Rules.getHandSize()));
		
		// predrawPiles = newPredrawPiles();
		
		this.trump = talon.getBottomCard();
		
		rules = new Rules(this, trump.getSuit());
		talon.setRules(rules);
		battleCards = new BattleCards(rules);
		
		finished = new boolean[numPlayers];
		for (@SuppressWarnings("unused")
		Boolean b : finished)
			b = false;
	}
	
	
	
	/*
	 * getter
	 */
	public Card getTrump() {
		
		return this.trump;
	}
	
	
	
	/*
	 * getter
	 */
	public CardList getAttacks() {
		
		return this.battleCards.getAttackCards();
	}
	
	
	
	/*
	 * return true if trump has not been drawn
	 */
	public boolean talonHasTrump() {
		
		return this.talon.size() >= 1;
	}
	
	
	
	/*
	 * return true if at least 2 cards have not been drawn from talon
	 */
	public boolean talonHas2PlusCards() {
		
		return this.talon.size() >= 2;
	}
	
	
	
	/*
	 * for adding attack cards to battle cards
	 * throws InvalidAttackException when specific attack cannot be made
	 * 
	 * @return CardList of complimentary talon cards for next round's hand
	 */
	public void attack(int id, Card attack) throws InvalidAttackException {
		
		if (battleCards.isEmpty() && !isAttacker(id))
			throw new InvalidAttackException(
			        "First Attack Has Yet To Be Played");
		
		battleCards.attack(attack); // may throw InvalidAttackException
		
		hands.get(id).remove(attack);
		
		// drawFromTalon(id);
	}
	
	/*
	 * add one or no cards to id's pre-draw pile
	 *
	 * private void drawFromTalon(int id) {
	 * 
	 * final CardList hand = hands.get(id);
	 * final CardList predraw = this.predrawPiles.get(id);
	 * 
	 * final int compliment = Rules.getHandSize()
	 * - (hand.size() + predraw.size());
	 * 
	 * if (compliment < 1)
	 * return;
	 * 
	 * predraw.addAll(talon.deal(1));
	 * }
	 */
	
	
	
	/*
	 * for defending 'attack' card with 'defense' card
	 * throws InvalidDefenseException when specific defense cannot be made
	 * 
	 * @return CardList of complimentary talon cards for next round's hand
	 */
	public void defend(int id, Card defense, Card attack)
	        throws InvalidDefenseException {
		
		if (id != defender)
			throw new InvalidDefenseException("Only Defender Can Defend");
		
		battleCards.defend(defense, attack); // may throw
		                                     // InvalidDefenseException
		
		hands.get(id).remove(defense);
		
		// this.defenderPredrawDeficit++; // increment to keep track of how many
		// cards the defender should add to their
		// hand in case they win the round
	}
	
	
	
	/*
	 * return true if id matches defender
	 */
	public boolean isDefender(int id) {
		
		return id == defender;
	}
	
	
	
	/*
	 * return true if id matches attacker
	 */
	public boolean isAttacker(int id) {
		
		return id == getAttacker();
	}
	
	
	
	/*
	 * return attacker id
	 */
	private int getAttacker() {
		
		if (this.attacker >= 0)
			return attacker;
		
		int id = defender;
		
		do {
			id = (id - 1) % getNumPlayers();
			if (id < 0)
				id += getNumPlayers();
		} while (hands.get(id).isEmpty());
		
		attacker = id;
		
		return attacker;
	}
	
	
	
	/*
	 * for passing battle cards to next defender
	 * throws InvalidPassException when pass cannot be made
	 * 
	 * @return CardList of complimentary talon cards for next round's hand
	 */
	public void pass(int id, Card passCard) throws InvalidPassException {
		
		if (id != defender)
			throw new InvalidPassException("Only Defender Can Pass");
		
		battleCards.pass(passCard); // may throw InvalidPassException
		
		hands.get(defender).remove(passCard);
		
		goToNextDefender();
		
		// drawFromTalon(defender);
	}
	
	
	
	/*
	 * stop additional attacks. If defender successfully defended, discard all
	 * the battle cards, else add those cards to defenders hand.
	 * Increment Defender index accordingly.
	 */
	public int endBattle(int id) {
		
		if (id != defender || battleCards.isEmpty())
			return 0;
		
		final boolean won = battleCards.endBattle();
		this.allPickUpNextRoundDraws(won);
		
		this.attacker = -1; // change to sentinel value, to be figured by getter
		
		int netCards = 0;
		
		if (won) {
			netCards = this.discardBattleCards();
			this.goToNextDefender();
		}
		else {
			netCards = this.pickUpBattleCards();
			this.goToNextDefender();
			this.goToNextDefender(); // skip defender's chance to attack
		}
		
		for (int i = 0; i < this.getNumPlayers(); i++)
			finished[i] = this.getHand(i).isEmpty();
		
		return netCards;
	}
	
	
	
	/*
	 * Draw until each player has at least 6 cards, start with attacker and draw
	 * circularly. Defender draws last.
	 */
	private void allPickUpNextRoundDraws(boolean won) {
		
		boolean first = true;
		
		for (int i = getAttacker(); first
		        || i != getAttacker(); i = (i + 1) % getNumPlayers()) {
			first = false;
			
			if (isDefender(i))
				continue;
			
			int deficit = Rules.getHandSize() - this.getHand(i).size();
			if (deficit > 0)
				this.getHand(i).addAll(talon.deal(deficit));
		}
		
		/*
		 * Draw cards from talon for defender
		 */
		int deficit = Rules.getHandSize() - this.getHand(defender).size();
		if (deficit > 0)
			this.getHand(defender).addAll(talon.deal(deficit));
		
		/*
		 * for (int i = 0; i < hands.size(); i++) {
		 * if (i == defender && won)
		 * hands.add(talon.deal(defenderPredrawDeficit));
		 * else
		 * hands.get(i).addAll(this.predrawPiles.get(i));
		 * }
		 * 
		 * this.defenderPredrawDeficit = 0;
		 * 
		 * this.predrawPiles = newPredrawPiles();
		 */
	}
	
	/*
	 * return a new List of predraw pile CardLists
	 *
	 * private List<CardList> newPredrawPiles() {
	 * 
	 * List<CardList> list = new ArrayList<>(getNumPlayers());
	 * for (int i = 0; i < getNumPlayers(); i++) {
	 * CardList tmp = new CardList();
	 * list.add(tmp);
	 * }
	 * return list;
	 * }
	 */
	
	
	
	/*
	 * move all battle cards to defender's hand
	 */
	private int pickUpBattleCards() {
		
		final int cards = battleCards.getAttackCards().size();
		
		this.hands.get(defender).addAll(battleCards.getAllCardsAndReset());
		
		return cards;
	}
	
	
	
	/*
	 * move all battle cards to discard
	 * 
	 * return (negative) number of cards discarded
	 */
	private int discardBattleCards() {
		
		final int cards = battleCards.getAttackCards().size();
		
		this.discard.addAll(battleCards.getAllCardsAndReset());
		
		return -cards;
	}
	
	
	
	/*
	 * circularly increments defender. If next defender has no cards in hand, go
	 * on
	 */
	private void goToNextDefender() {
		
		defender = getNextDefender();
	}
	
	
	
	/*
	 * circularly finds next defender. If next defender has no cards in hand, go
	 * on
	 */
	private int getNextDefender() {
		
		int next = defender;
		
		do
			next = (next + 1) % getNumPlayers();
		while (hands.get(next).isEmpty());
		
		return next;
	}
	
	
	
	/*
	 * return pasee's (player after defender) hand size
	 */
	public int getPaseeHandSize() {
		
		final int passee = getNextDefender();
		
		return hands.get(passee).size();
	}
	
	
	
	/*
	 * getter
	 */
	public int getNumPlayers() {
		
		return hands.size();
	}
	
	
	
	/*
	 * returns the size of the defender's hand
	 */
	public int getDefendersHandSize() {
		
		return hands.get(defender).size();
	}
	
	
	
	public int getNumUnbeatenAttacks() {
		
		return this.battleCards.getNumUnbeatenAttacks();
	}
	
	
	
	/*
	 * return true if all but one player has Cards in their hand
	 */
	public boolean isOver() {
		
		int n = 0;
		for (Boolean b : finished)
			if (!b)
				n++;
			
		return n == 1;
	}
	
	
	
	/*
	 * getter for specific hand
	 */
	public CardList getHand(int hand) {
		
		hands.get(hand).sort();
		return this.hands.get(hand);
	}
	
	
	
	/*
	 * return true if cardAtk has a defense card
	 */
	public boolean hasDefense(Card cardAtk) {
		
		if (!this.battleCards.getAttackCards().contains(cardAtk))
			return false;
		
		return this.getDefense(cardAtk) != null;
	}
	
	
	
	/*
	 * return defense card paired with attack card
	 */
	public Card getDefense(Card cardAtk) {
		
		return this.battleCards.getDefenseCard(cardAtk);
	}
	
	
	
	/*
	 * return set of the ranks of all cards in play
	 */
	public Set<Rank> getCurrentRanks() {
		
		return this.battleCards.getAllRanks();
	}
	
	
	
	/*
	 * return set of the ranks of all attack cards in play
	 */
	public Set<Rank> getUnbeatenAttackRanks() {
		
		return this.battleCards.getUnbeatenAttackRanks();
	}
	
	
	
	/*
	 * return true if player has rid all their cards
	 */
	public boolean hasFinished(int id) {
		
		return finished[id];
	}
	
}
