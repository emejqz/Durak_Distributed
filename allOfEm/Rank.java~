package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Author : Eric M. Evans
 * Date : 01-23-2017 (mdy)
 *
 * Rank for a playing card, Card.java.
 * 
 * static mapping code from:
 * http://stackoverflow.com/questions/11047756/getting-enum-associated-with-int-value
 *
 */
public enum Rank {
	
	DEUCE(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(
	        9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);
	
	
	
	private final int					val;
	
	private static Map<Integer, Rank>	map	= new HashMap<Integer, Rank>();
	
	static {
		for (Rank rank : Rank.values()) {
			map.put(rank.val, rank);
		}
	}
	
	
	
	Rank(int val) {
		
		this.val = val;
		
	}
	
	
	
	/*
	 * getter
	 */
	public int getValue() {
		
		return val;
		
	}
	
	
	
	public static Rank valueOf(int v) {
		
		return map.get(v);
	}
	
	
	
	public Rank get_rank_less(int i) {
		
		if (this.val - i < Rank.DEUCE.val)
			throw new IndexOutOfBoundsException("no card for value of " + i);
		
		return valueOf(this.val - i);
	}
	
}
