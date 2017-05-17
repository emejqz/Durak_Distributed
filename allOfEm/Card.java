//package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * Author : Eric M. Evans
 * Date : 01-23-2017 (mdy)
 * 
 * Playing card consisting of a rank and suit.
 * 
 * needed files:
 * Rank.java
 * Suit.java
 * 
 *
 */
public class Card implements Comparable<Card>, Serializable {
	
	private static final long		serialVersionUID	= -6080692588975383358L;
	private Rank					rank;
	private Suit					suit;
	//private BufferedImage			image;
	private Rules					rules;
	private static BufferedImage	backImage;
	
	
	
	public Card(Rank rank, Suit suit) {
		
		this.rank = rank;
		this.suit = suit;
		
	}
	
	
	
	/*
	 * getter
	 */
	public Suit getSuit() {
		
		return suit;
	}
	
	
	
	/*
	 * getter
	 */
	public Rank getRank() {
		
		return rank;
	}
	
	
	
	/*
	 * Examples:
	 * 10 of Clubs : CT
	 * Jack of Diamonds : DJ
	 * 3 of Hearts : H3
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		StringBuilder strb = new StringBuilder();
		
		switch (rank) {
			
			case DEUCE:
				strb.append("2");
				break;
			case THREE:
				strb.append("3");
				break;
			case FOUR:
				strb.append("4");
				break;
			case FIVE:
				strb.append("5");
				break;
			case SIX:
				strb.append("6");
				break;
			case SEVEN:
				strb.append("7");
				break;
			case EIGHT:
				strb.append("8");
				break;
			case NINE:
				strb.append("9");
				break;
			case TEN:
				strb.append("X");
				break;
			case JACK:
				strb.append("J");
				break;
			case QUEEN:
				strb.append("Q");
				break;
			case KING:
				strb.append("K");
				break;
			case ACE:
				strb.append("A");
				break;
			default:
				return null;
		}
		
		switch (suit) {
			case CLUBS:
				strb.append('C'); // '\u2663' ♣
				break;
			case DIAMONDS:
				strb.append('D'); // '\u2666' ♦
				break;
			case HEARTS:
				strb.append('H'); // '\u2665' ♥
				break;
			case SPADES:
				strb.append('S'); // '\u2660' ♠
				break;
			default:
				return null;
		}
		
		return strb.toString();
		
	}
	
	
	
	/*
	 * comparison based on Card rank value
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Card rival) {
		
		if (this.isSuit(rules.getTrump()) && rival.isSuit(rules.getTrump()))
			return this.rank.getValue() - rival.rank.getValue();
		
		if (this.isSuit(rules.getTrump()))
			return 1;
		
		if (rival.isSuit(rules.getTrump()))
			return -1;
		
		return this.rank.getValue() - rival.rank.getValue();
	}
	
	
	
	/*
	 * returns true if two cards have the same the rank
	 */
	public boolean isSameRank(Card card) {
		
		return this.getRank().equals(card.getRank());
	}
	
	
	
	/*
	 * return true if card is of same suit
	 */
	public boolean isSuit(Suit match) {
		
		return this.getSuit().equals(match);
	}
	
	
	
	/*
	 * return image.
	 */
	public BufferedImage getImage() {
		
		//if (image != null)
		//	return image;
		
		BufferedImage image = null;
		try {
			final String file = "./art/" + toString() + ".png";
			// System.out.println(file);
			image = ImageIO.read(new File(file));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	
	
	/*
	 * return back image
	 */
	public static BufferedImage getBackImage() {
		
		if (backImage != null)
			return backImage;
		
		try {
			final String file = "./art/back.png";
			// System.out.println(file);
			backImage = ImageIO.read(new File(file));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return backImage;
	}
	
	
	
	/*
	 * set rules
	 */
	public void setRules(Rules rules) {
		
		this.rules = rules;
	}
	
}
