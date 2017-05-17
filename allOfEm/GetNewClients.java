/*
 * @author Eric M Evans
 * 
 * For establishing new client connections and sending updates.
 * Terminated when 'ready' in EstablishAllConnections is true.
 */
//package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

//import model.Rules;

public class GetNewClients extends Thread {
	
	private EstablishAllConnections			eac;
	
	
	
	public GetNewClients(EstablishAllConnections eac) {
		
		this.eac = eac;
	}
	
	
	
	@Override
	public void run() {
		
		try {
			while (eac.getNumPlayers() < Rules.getMaxPlayers()) {
				Socket connection = eac.getServerSocket().accept();
				
				ObjectOutputStream outputToClient = new ObjectOutputStream(
				        connection.getOutputStream());
				eac.addOutputStream(outputToClient);
				
				ObjectInputStream inputFromClient = new ObjectInputStream(
				        connection.getInputStream());
				eac.addInputStream(inputFromClient);
				
				eac.oneMorePlayer();
				
				outputToClient.reset();
				outputToClient.writeObject(eac.getNumPlayers() - 1);
				// System.out.println("id sent to client");
				outputToClient.flush();
				
				// get Client's IP
				String addr = (String) inputFromClient.readObject();
				// System.out.println(addr);
				eac.addClientAddress(addr);
				
				// Update all clients of new total of players and ready status
				eac.updateAllClientsAndSelf();
			}
			eac.ready(); // game has 8 (MAX) players
		}
		// thrown when no more connection need establishing, invoked by eac
		catch (SocketException e) {
			return;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
