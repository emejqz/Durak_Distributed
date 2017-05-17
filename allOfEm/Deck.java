//package model;

import java.io.Serializable;
import java.util.Collections;

/*
 * Author : Eric M. Evans
 * Date : 01-28-2017 (mdy)
 * 
 * Holds 52 Card objects. includes methods for playing a game of poker.
 */
public class Deck implements Serializable {
	
	private static final long	serialVersionUID	= -8797651252928281753L;
	private CardList			cards				= new CardList();
	private int					index				= 0;
	private Rules				rules;
	private static final int	TOTAL_CARDS			= 52;
	
	
	
	public Deck() {
		
		for (Rank rank : Rank.values())
			for (Suit suit : Suit.values())
				cards.add(new Card(rank, suit));
			
		this.reset();
	}
	
	
	
	/*
	 * shuffles deck
	 * resets this.index to 0.
	 */
	private void reset() {
		
		Collections.shuffle(cards);
		this.index = 0;
	}
	
	
	
	/*
	 * return Card last to be drawn
	 */
	public Card getBottomCard() {
		
		return cards.get(cards.size() - 1);
	}
	
	
	
	/*
	 * returns a Card at this.index, post-increments this.index
	 */
	private Card deal() {
		
		if (index == cards.size())
			return null;
		
		return cards.get(this.index++);
		
	}
	
	
	
	/*
	 * returns CardList of number of indicated cards
	 */
	public CardList deal(int num) {
		
		if (num < 1)
			return null;
		
		CardList cards = new CardList();
		final int dealSize = Math.min(num, availableCards());
		
		for (int i = 0; i < dealSize; i++)
			cards.add(this.deal());
		
		return cards;
	}
	
	
	
	/*
	 * returns number of available cards
	 */
	private int availableCards() {
		
		return TOTAL_CARDS - index;
	}
	
	
	
	/*
	 * return number of cards yet to be used
	 */
	public int size() {
		
		return this.availableCards();
	}
	
	
	
	/*
	 * return true if there are no available cards
	 */
	public boolean isEmpty() {
		
		return size() == 0;
	}
	
	
	
	/*
	 * return true if no card has been dealt
	 */
	public boolean isFull() {
		
		return index == 0;
	}
	
	
	
	/*
	 * set rules
	 */
	public void setRules(Rules rules) {
		
		this.rules = rules;
		
		for (Card card : cards)
			card.setRules(rules);
	}
	
}
