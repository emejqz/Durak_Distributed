package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * Author : Eric M. Evans
 * Date : 01-28-2017 (mdy)
 * 
 * Extends ArrayList containing only Card objects.
 */
public class CardList extends ArrayList<Card> implements Serializable {
	
	private static final long serialVersionUID = 5030793506049677868L;
	
	
	
	public CardList() {
		super();
	}
	
	
	
	/*
	 * quick helper method for sort(Comparator<? super Card> c)
	 */
	public void sort() {
		
		this.sort(null);
	}
	
	
	
	/*
	 * if argument is null, sorts in reverse order (descending), else sorts
	 * normally.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#sort(java.util.Comparator)
	 */
	@Override
	public void sort(Comparator<? super Card> c) {
		
		if (c != null) {
			super.sort(c);
			return;
		}
		
		super.sort(c);
		
		/*
		 * reverse sorted order, from ascending to descending
		 *
		 * for (int i = 0; i < this.size() / 2; i++) {
		 * int right_index = this.size() - 1 - i;
		 * Card temp = this.get(i);
		 * this.set(i, this.get(right_index));
		 * this.set(right_index, temp);
		 * }
		 */
	}
	
	
	
	/*
	 * returns true if this contains at least one card whose rank matches rank
	 * argument
	 */
	public boolean doesContain(Rank rank) {
		
		for (Card card : this) {
			if (card.getRank() == rank)
				return true;
		}
		return false;
	}
	
	
	
	/*
	 * returns first card whose rank matches rank argument
	 */
	public Card get(Rank rank) {
		
		for (Card card : this) {
			if (card.getRank() == rank)
				return card;
		}
		return null;
		
	}
	
	
	
	/*
	 * returns a CardList of at the most size 'size' containing only the Cards
	 * whose rank matches rank argument
	 */
	public CardList getN(int size, Rank rank) {
		
		if (size < 1 || size > 4)
			return null;
		
		CardList cards = this.getAll(rank);
		
		while (cards.size() > size)
			cards.remove(cards.size() - 1); // pop
			
		return cards;
	}
	
	
	
	/*
	 * returns an CardList of at the most size 'size' containing only the Cards
	 * whose suit matches suit argument
	 */
	public CardList getN(int size, Suit suit) {
		
		if (size < 1)
			return null;
		
		CardList cards = this.getAll(suit);
		
		while (cards.size() > size)
			cards.remove(cards.size() - 1); // pop
			
		return cards;
	}
	
	
	
	/*
	 * returns an CardList containing only the Cards whose rank
	 * matches rank argument
	 */
	public CardList getAll(Rank rank) {
		
		CardList matches = new CardList();
		
		for (Card card : this) {
			if (card.getRank() == rank)
				matches.add(card);
		}
		return matches;
	}
	
	
	
	/*
	 * returns an CardList containing only the Cards whose suit
	 * matches suit argument
	 */
	public CardList getAll(Suit suit) {
		
		CardList matches = new CardList();
		
		for (Card card : this) {
			if (card.getSuit() == suit)
				matches.add(card);
		}
		return matches;
	}
	
	
	
	/*
	 * returns the frequency of a rank in list
	 */
	public int frequency(Rank rank) {
		
		int freq = 0;
		
		for (Card card : this) {
			if (card.getRank() == rank)
				freq++;
		}
		return freq;
	}
	
	
	
	/*
	 * returns the frequency of a suit in list
	 */
	public int frequency(Suit suit) {
		
		int freq = 0;
		
		for (Card card : this) {
			if (card.getSuit() == suit)
				freq++;
		}
		return freq;
	}
	
	
	
	/*
	 * returns the highest card in cards
	 */
	public Card getHighestCard() {
		
		Card high = this.get(0);
		
		for (Card card : this) {
			if (card.compareTo(high) > 0)
				high = card;
		}
		return high;
	}
	
	
	
	/*
	 * returns set difference of whole and part
	 */
	public static CardList setDifference(CardList whole, CardList part) {
		
		CardList diff = new CardList();
		
		for (Card card : whole) {
			if (!part.contains(card))
				diff.add(card);
		}
		return diff;
	}
	
	
	
	/*
	 * returns CardList containing the contents of a and b, without accounting
	 * for duplicates.
	 */
	public static CardList rawSum(CardList a, CardList b) {
		
		CardList raw_sum = new CardList();
		
		for (Card card : a)
			raw_sum.add(card);
		
		for (Card card : b)
			raw_sum.add(card);
		
		return raw_sum;
	}
	
	
	
	/*
	 * uses Card.toString() to return list, space-separated
	 * 
	 * Example:
	 * SK DQ DT C9 H3
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		this.sort();
		
		StringBuilder strb = new StringBuilder();
		
		for (int i = this.size() - 1; i >= 0; i--) {
			Card card = this.get(i);
			strb.append(card.toString() + " ");
		}
		strb.deleteCharAt(strb.length() - 1);
		
		return strb.toString();
	}
	
	
	
	/*
	 * returns true if this contains duplicate cards
	 */
	public boolean doesContainsDuplicates() {
		
		for (Card card : this) {
			if (Collections.frequency(this, card) > 1)
				return true;
		}
		return false;
	}
	
	
	
	/*
	 * keeps the amount of cards indicated by the argument
	 */
	public CardList keepNHighestCards(int n) {
		
		this.sort();
		
		if (n >= this.size())
			return this;
		
		/*
		 * this.size() > keep
		 * now guaranteed
		 */
		
		final int popAmt = this.size() - n;
		
		for (int i = popAmt; i > 0; i--)
			this.remove(this.size() - 1);
		
		return this;
	}
	
}
