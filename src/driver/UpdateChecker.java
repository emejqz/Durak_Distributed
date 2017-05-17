/*
 * @author Eric M Evans
 * 
 * A Thread for checking for incoming CardTable updates. One instance per
 * ObjectInputStream.
 */
package driver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

import model.CardTable;

public class UpdateChecker extends Thread {
	
	private Durak				game;
	private ObjectInputStream	in;
	
	
	
	public UpdateChecker(ObjectInputStream in, Durak durak) {
		this.in = in;
		this.game = durak;
	}
	
	
	
	@Override
	public void run() {
		
		try {
			while (true) {
				synchronized (in) {
					CardTable ct = (CardTable) in.readObject();
					game.setCardTableAndNotify(ct);
				}
			}
		}
		catch (SocketException e) {
			System.out.println("A Player Quit, So Game is Over");
			System.exit(0);
		}
		catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
	}
}
