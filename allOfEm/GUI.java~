package view;
/*
 * @author Eric M Evans
 * 
 * GUI for Durak card game
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import driver.Durak;
import model.Card;
import model.CardList;
import model.CardTable;
import model.InvalidAttackException;
import model.InvalidDefenseException;
import model.InvalidPassException;

@SuppressWarnings("serial")
public class GUI extends JFrame implements Observer {
	
	private Canvas	canvas;
	private int		players;
	private int		id;
	private Durak	game;
	
	private boolean	defenseCardChosen	= false;
	private Card	defenseCard;
	
	
	
	public GUI(Durak game, int players, int id) {
		
		this.players = players;
		this.id = id;
		this.game = game;
		
		this.setTitle("Durak - Player: " + (id + 1) + "/" + players);
		this.setSize(1100, 700);
		this.setLocation(50 * id, 50 * id);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.setVisible(true);
		
		this.canvas = new Canvas();
		this.canvas.setSize(this.getWidth() - 40 - 5,
		        this.getHeight() - 40 - 30);
		this.canvas.setLocation(20, 20);
		this.canvas.setVisible(true);
		this.canvas.setBackground(Color.ORANGE);
		this.add(canvas);
		this.addKeyListener(new PlayKeyListener());
		
	}
	
	
	
	@Override
	public void update(Observable arg0, Object arg1) {
		
		canvas.repaint();
	}
	
	
	
	/*
	 * Canvas (JPanel) for Drawing Cards on table
	 */
	private class Canvas extends JPanel {
		
		private String message;
		
		
		
		public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			        RenderingHints.VALUE_ANTIALIAS_ON);
			
			final int cardHeight = 145;
			final int cardWidth = 100;
			final int adjWidth = this.getWidth() - 2 * cardWidth;
			
			final CardTable table = game.getCardTable();
			
			if (table == null)
				return;
			
			/*
			 * Draw Hand Cards
			 */
			final CardList myHand = table.getHand(id);
			final int yHand = this.getHeight() - cardHeight - 10;
			final int xHand = adjWidth / (myHand.size() + 1);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Helvetica", Font.BOLD, 20));
			for (int i = 0; i < myHand.size(); i++) {
				int myX = (i + 1) * xHand + (cardWidth / 2);
				Card card = myHand.get(i);
				BufferedImage img = card.getImage();
				g2.drawImage(img, myX, yHand, cardWidth, cardHeight, null,
				        null);
				
				if (!GUI.this.defenseCardChosen) {
					String key = DurakKeys.getAttackKeyName(i);
					g2.drawString(key, myX + 5, yHand - 5);
				}
				else if (card.equals(GUI.this.defenseCard)) {
					// System.out.println("selected");
					g2.drawString("Selected", myX + 5, yHand - 5);
				}
				
			}
			
			/*
			 * Draw Players Cards Count
			 */
			BufferedImage back = Card.getBackImage();
			final int smallCardWidth = (int) (cardWidth / 2.5);
			final int smallCardHeight = (int) (cardHeight / 2.5);
			for (int i = 0; i < players; i++) {
				final int myY = 50 + i * smallCardHeight + i * 10;
				g2.drawString("P" + (i + 1), this.getWidth() - 85,
				        myY + smallCardWidth / 2);
				
				if (table.isDefender(i))
					g2.drawString("Def", this.getWidth() - 90,
					        myY + smallCardWidth + 5);
				else if (table.isAttacker(i))
					g2.drawString("Att", this.getWidth() - 90,
					        myY + smallCardWidth + 5);
				else if (table.hasFinished(i))
					g2.drawString("Fin", this.getWidth() - 90,
					        myY + smallCardWidth + 5);
				
				g2.drawImage(back, this.getWidth() - 50, myY, smallCardWidth,
				        smallCardHeight, null, null);
				g2.drawString("" + table.getHand(i).size(),
				        this.getWidth() - 35, myY + smallCardHeight / 2 + 5);
			}
			
			/*
			 * Draw End of Game Messages
			 */
			if (table.isOver()) {
				String msg = "Game Over";
				if (!table.hasFinished(id))
					msg += " -- You Are The Durak!";
				g2.drawString(msg, 300, 150);
				return;
			}
			
			/*
			 * Draw Message
			 */
			if (message != null) {
				g2.drawString(message, 300, 150);
				message = null;
			}
			
			/*
			 * Draw Attack and Defense Cards
			 */
			final CardList attackCards = table.getAttacks();
			final int yAttack = this.getHeight() / 2 - cardHeight + 20;
			final int xAttack = adjWidth / (attackCards.size() + 1);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Helvetica", Font.BOLD, 20));
			for (int i = 0; i < attackCards.size(); i++) {
				int myX = (i + 1) * xAttack + (cardWidth / 2);
				Card cardAtk = attackCards.get(i);
				BufferedImage imgAtk = cardAtk.getImage();
				g2.drawImage(imgAtk, myX, yAttack, cardWidth, cardHeight, null,
				        null);
				if (table.hasDefense(cardAtk)) {
					Card cardDef = table.getDefense(cardAtk);
					BufferedImage imgDef = cardDef.getImage();
					g2.drawImage(imgDef, myX, yAttack + 40, cardWidth,
					        cardHeight, null, null);
				}
				String key = DurakKeys.getDefendKeyName(i);
				g2.drawString(key, myX + 5, yAttack - 5);
			}
			
			/*
			 * Draw Trump Card
			 */
			if (table.talonHasTrump()) {
				BufferedImage trump = table.getTrump().getImage();
				g2.drawImage(trump, 10, 10, cardWidth, cardHeight, null, null);
			}
			
			/*
			 * Draw Talon Back Card
			 */
			if (table.talonHas2PlusCards()) {
				BufferedImage back2 = Card.getBackImage();
				g2.drawImage(back2, 10 + (cardWidth / 5), 10, cardWidth,
				        cardHeight, null, null);
			}
			
			/*
			 * Draw Defender / Attacker Label and Key Instructions
			 * 
			 * Check for Defender first b/c in the event that multiple passes
			 * lead to the original attacker becoming a defender
			 */
			if (table.isDefender(id)) {
				g2.drawString("Defender", 300, 50);
				g2.drawString("  [ENTER] : pass, after selecting card", 300,
				        85);
				g2.drawString("  [SPACE] : end battle", 300, 110);
			}
			else if (table.isAttacker(id)) {
				g2.drawString("Attacker", 300, 50);
			}
			
		}
		
		
		
		public void drawMessage(String str) {
			
			this.message = str;
			this.repaint();
		}
	}
	
	
	
	/*
	 * Key Listener for playing cards
	 */
	private class PlayKeyListener implements KeyListener {
		
		@Override
		public void keyReleased(KeyEvent arg0) {
			
			final int keyCode = arg0.getKeyCode();
			
			// attacking new card from hand
			if (!game.getCardTable().isDefender(id)) {
				if (keyCode == KeyEvent.VK_SPACE)
					canvas.drawMessage("Only Defender Can End Battle");
				else
					this.attackingMoves(keyCode);
			}
			// picking defense card, passing, or playing defense card
			else {
				if (keyCode == KeyEvent.VK_SPACE)
					this.endBattle();
				else if (!GUI.this.defenseCardChosen) {
					GUI.this.defenseCardChosen = this
					        .chooseDefenseCard(keyCode);
					canvas.repaint();
				}
				else {
					if (keyCode == KeyEvent.VK_ENTER)
						this.pass();
					else
						this.defendAttackCard(keyCode);
					GUI.this.defenseCardChosen = false;
					GUI.this.defenseCard = null;
					canvas.repaint();
				}
			}
			
		}
		
		
		
		/*
		 * Passing with defenseCard with [ENTER]
		 */
		private void pass() {
			
			try {
				game.getCardTable().pass(id, defenseCard);
				game.update();
			}
			catch (InvalidPassException e) {
				canvas.drawMessage(e.getReason());
			}
		}
		
		
		
		/*
		 * Ending Battle with [SPACE]
		 */
		private void endBattle() {
			
			int netCards = game.getCardTable().endBattle(id);
			if (netCards < 0)
				canvas.drawMessage(
				        "You Won! You Defeated " + -netCards + " Cards.");
			else if (netCards > 0)
				canvas.drawMessage(
				        "You Lost. You Picked Up " + netCards + " Cards.");
			else {
				canvas.drawMessage("You Cannot End Battle Before First Blood");
				return;
			}
			game.update();
			defenseCardChosen = false;
		}
		
		
		
		/*
		 * Use defenseCard in attack, unsuccessful or otherwise
		 */
		private void defendAttackCard(int keyCode) {
			
			int battleIndex;
			
			try {
				battleIndex = DurakKeys.getDefendIndexForKeyEventCode(keyCode);
			}
			catch (NullPointerException e) {
				return;
			}
			
			if (battleIndex > game.getCardTable().getAttacks().size() - 1)
				return;
			
			final Card attackCard = game.getCardTable().getAttacks()
			        .get(battleIndex);
			// System.out.println("Defend Against " + attackCard);
			
			try {
				game.getCardTable().defend(id, defenseCard, attackCard);
				game.update();
			}
			catch (InvalidDefenseException e) {
				canvas.drawMessage(e.getReason());
			}
		}
		
		
		
		/*
		 * For choosing a defense card from defender's hand
		 */
		private boolean chooseDefenseCard(int keyCode) {
			
			int handIndex;
			
			try {
				handIndex = DurakKeys.getAttackIndexForKeyEventCode(keyCode);
			}
			catch (NullPointerException e) {
				return false;
			}
			
			if (handIndex > game.getCardTable().getHand(id).size() - 1)
				return false;
			
			GUI.this.defenseCard = game.getCardTable().getHand(id)
			        .get(handIndex);
			
			// System.out.println("Defend with " + defenseCard);
			return true;
		}
		
		
		
		/*
		 * For attacking cards or defending a particular attack card
		 */
		private void attackingMoves(int keyCode) {
			
			int handIndex;
			
			try {
				handIndex = DurakKeys.getAttackIndexForKeyEventCode(keyCode);
			}
			catch (NullPointerException e) {
				return;
			}
			
			if (handIndex > game.getCardTable().getHand(id).size() - 1)
				return;
			
			final Card card = game.getCardTable().getHand(id).get(handIndex);
			
			try {
				game.getCardTable().attack(id, card);
				// System.out.println(game.getCardTable().getHand(id).size());
				game.update();
			}
			catch (InvalidAttackException e) {
				canvas.drawMessage(e.getReason());
			}
		}
		
		
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			
		}
		
		
		
		@Override
		public void keyTyped(KeyEvent arg0) {
			
		}
		
	}
	
}
