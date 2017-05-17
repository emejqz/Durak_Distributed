package driver;
/*
 * @author Eric M Evans
 * 
 * Main model for game.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;

import model.CardTable;
import model.TooManyPlayersException;
import network.EstablishAllConnections;
import view.GUI;

public class Durak extends Observable {
	
	public static void main(String[] args) {
		
		Durak game = new Durak();
		game.checkForUpdates();
	}
	
	
	
	private CardTable						cardTable;
	private int								players;
	private int								id;
	private ArrayList<ObjectInputStream>	inputStreams;
	private ArrayList<ObjectOutputStream>	outputStreams;
	
	
	
	/*
	 * create new card table and start GUI
	 */
	private Durak() {
		
		EstablishAllConnections eac = null;
		try {
			eac = new EstablishAllConnections();
		}
		catch (IOException | InterruptedException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		this.players = eac.getNumPlayers();
		this.id = eac.getID();
		this.inputStreams = eac.getInputStreams();
		this.outputStreams = eac.getOutputStreams();
		
		GUI gui = new GUI(this, players, id);
		this.addObserver(gui);
		
		if (id == 0) {
			try {
				this.cardTable = new CardTable(players);
			}
			catch (TooManyPlayersException e) {
				System.out.println(e.getReason());
				System.exit(1);
			}
			this.update();
		}
		
		checkForUpdates();
	}
	
	
	
	/*
	 * getter
	 */
	public CardTable getCardTable() {
		
		return this.cardTable;
	}
	
	
	
	/*
	 * send update to all GUI observers
	 */
	public synchronized void update() {
		
		updateObservers();
		
		updateOthers();
	}
	
	
	
	/*
	 * send updates to peers
	 */
	private void updateOthers() {
		
		try {
			for (ObjectOutputStream out : outputStreams) {
				out.reset();
				out.writeObject(cardTable);
				out.flush();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * update GUI
	 */
	private void updateObservers() {
		
		this.setChanged();
		this.notifyObservers();
	}
	
	
	
	/*
	 * check for network updates a.k.a. new CardTable instances
	 */
	private void checkForUpdates() {
		
		for (ObjectInputStream in : inputStreams) {
			UpdateChecker uc = new UpdateChecker(in, this);
			uc.start();
		}
	}
	
	
	
	public void setCardTableAndNotify(CardTable cardTable) {
		
		this.cardTable = cardTable;
		updateObservers();
	}
}
