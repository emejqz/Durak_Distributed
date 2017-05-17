/*
 * @author Eric M Evans
 * 
 * Establishing server-client connection in waiting room stage.
 * Then establishing peer to peer connections, all-to-all.
 */
package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Vector;

public class EstablishAllConnections extends Observable {
	
	private Choice							choice			= null;
	private int								id;
	private int								numPlayers		= 0;
	private String							serversAddr		= null;
	private boolean							ready			= false;
	
	private Vector<String>					addresses		= new Vector<>();
	
	private ArrayList<ObjectOutputStream>	outputStreams	= new ArrayList<>();
	private ArrayList<ObjectInputStream>	inputStreams	= new ArrayList<>();
	private ServerSocket					serverSocket	= null;
	private static final int				BASE_PORT		= 10000;
	
	
	
	public EstablishAllConnections()
	        throws IOException, InterruptedException, ClassNotFoundException {
		
		new StartingRoom(this);
		while (choice == null)
			Thread.sleep(100);
		
		if (choice == Choice.JOIN_TABLE) {
			// System.out.println("join");
			new JoinTableDialog(this);
			while (serversAddr == null)
				Thread.sleep(100);
			this.clientConnectTo(serversAddr);
		}
		else {
			id = 0;
			numPlayers = 1;
			// System.out.println("new");
			setUpServerAndOrganizeNewClients();
		}
		
		// Establish Peer-to-Peer Connections
		if (id == 0)
			introduceClients();
		else
			meetPeers();
		
	}
	
	
	
	public ArrayList<ObjectOutputStream> getOutputStreams() {
		
		return this.outputStreams;
	}
	
	
	
	public ArrayList<ObjectInputStream> getInputStreams() {
		
		return this.inputStreams;
	}
	
	
	
	/*
	 * set by StartingRoom.start()
	 */
	public void setChoice(Choice choice) {
		
		this.choice = choice;
	}
	
	
	
	/*
	 * server only. invoked by choosing to create new table
	 */
	private void setUpServerAndOrganizeNewClients()
	        throws IOException, InterruptedException {
		
		this.serverSocket = new ServerSocket(BASE_PORT);
		
		this.addObserver(new WaitingRoom(this));
		
		// System.out.println("go go gadget");
		
		Thread connectEm = new GetNewClients(this);
		connectEm.start();
		
		// wait until table master (server) click start
		while (!ready)
			Thread.sleep(1000);
		
		// Update all clients of new total of players and ready status
		updateAllClientsAndSelf();
		
		// System.out.println(this.addresses);
		// System.out.println("Server is ready");
		
		// Send all clients all IPs
		for (ObjectOutputStream out : this.outputStreams)
			out.writeObject(addresses);
	}
	
	
	
	/*
	 * server only. send player count and ready status to clients and observers.
	 * can be invoked from GetNewClients
	 */
	void updateAllClientsAndSelf() throws IOException {
		
		for (ObjectOutputStream out : this.outputStreams) {
			out.reset();
			out.writeObject(this.numPlayers);
			out.writeObject(this.ready);
			out.flush();
		}
		
		this.setChanged();
		this.notifyObservers();
	}
	
	
	
	/*
	 * client only. invoked by choosing to join existing table
	 */
	@SuppressWarnings({"unchecked", "resource"})
	public void clientConnectTo(String address)
	        throws IOException, ClassNotFoundException {
		
		/*
		 * Establish Server Connection
		 */
		Socket server = null;
		try {
			server = new Socket(address, BASE_PORT);
		}
		catch (ConnectException | UnknownHostException e) {
			new CannotConnectDialog(address);
			return;
		}
		
		ObjectOutputStream toServer = new ObjectOutputStream(
		        server.getOutputStream());
		this.addOutputStream(toServer);
		
		ObjectInputStream fromServer = new ObjectInputStream(
		        server.getInputStream());
		this.addInputStream(fromServer);
		
		this.id = (Integer) fromServer.readObject();
		// System.out.println("I'm: " + id);
		
		// Send my IP
		toServer.reset();
		toServer.writeObject(InetAddress.getLocalHost().getHostAddress());
		toServer.flush();
		
		this.addObserver(new WaitingRoom(this));
		
		while (!this.ready) {
			this.numPlayers = (Integer) fromServer.readObject();
			this.ready = (Boolean) fromServer.readObject();
			// System.out.println("NumPlayers: " + this.numPlayers);
			this.setChanged();
			this.notifyObservers();
		}
		
		// System.out.println("Client is ready");
		
		// Get All Server's Clients' IPs
		this.addresses = (Vector<String>) fromServer.readObject();
	}
	
	
	
	/*
	 * server only. Organize the connecting of each client to each client.
	 */
	private void introduceClients() throws IOException, ClassNotFoundException {
		
		for (int i = 0; i < this.getOutputStreams().size(); i++) {
			this.getOutputStreams().get(i).reset();
			this.getOutputStreams().get(i).writeObject(true); // signal server
			this.getOutputStreams().get(i).flush();// phase
			
			//System.out.println("wait for response " + (i + 1));
			this.getInputStreams().get(i).readObject(); // for synchronization
			//System.out.println("got response " + (i + 1));
			String addrI = this.addresses.get(i);
			
			for (int j = i + 1; j < this.getOutputStreams().size(); j++) {
				ObjectOutputStream toJ = this.getOutputStreams().get(j);
				ObjectInputStream fromJ = this.getInputStreams().get(j);
				//System.out.println("client start " + (j + 1));
				toJ.reset();
				toJ.writeObject(false); // signal client phase
				toJ.flush();
				fromJ.readObject();// for synchronization
				toJ.reset();
				toJ.writeObject(addrI); // send i's IP
				toJ.flush();
				fromJ.readObject();// for synchronization
				toJ.reset();
				toJ.writeObject(BASE_PORT + i + 1); // send i's port
				toJ.flush();
				fromJ.readObject();// for synchronization
				//System.out.println("client done " + (j + 1));
			}
		}
	}
	
	
	
	/*
	 * client only. Listen for server to tell who to connect with.
	 */
	private void meetPeers() throws IOException, ClassNotFoundException {
		
		this.serverSocket = new ServerSocket(BASE_PORT + id);
		ObjectInputStream fromOGServer = this.getInputStreams().get(0);
		ObjectOutputStream toOGServer = this.getOutputStreams().get(0);
		//System.out.println("go -- " + id);
		while (true) {
			if ((Boolean) fromOGServer.readObject() == true) {
				toOGServer.reset();
				toOGServer.writeObject(null); // for synchronization
				toOGServer.flush();
				for (int i = id + 1; i < this.numPlayers; i++) {
					//System.out.println("About to accept " + i);
					Socket peer = this.serverSocket.accept();
					ObjectOutputStream out = new ObjectOutputStream(
					        peer.getOutputStream());
					out.reset();
					out.writeObject(null); // for sync'n
					out.flush();
					//System.out.println("Just accepted " + i);
					this.addInputStream(
					        new ObjectInputStream(peer.getInputStream()));
					this.addOutputStream(out);
					//System.out.println("server " + i);
				}
				break; // once a peer is a server, it has met everyone
			}
			else {
				toOGServer.reset();
				toOGServer.writeObject(null); // for synchronization
				toOGServer.flush();
				final String addr = (String) fromOGServer.readObject();
				toOGServer.reset();
				toOGServer.writeObject(null); // for synchronization
				toOGServer.flush();
				final int port = (Integer) fromOGServer.readObject();
				toOGServer.reset();
				toOGServer.writeObject(null); // for synchronization
				toOGServer.flush();
				//System.out.println("About to Request @ " + port);
				@SuppressWarnings("resource")
				Socket peer = new Socket(addr, port);
				ObjectInputStream in = new ObjectInputStream(
				        peer.getInputStream());
				in.readObject(); // for sync'n
				//System.out.println("Just Connected");
				this.addInputStream(in);
				this.addOutputStream(
				        new ObjectOutputStream(peer.getOutputStream()));
				//System.out.println("client");
			}
		}
		//System.out.println("done");
	}
	
	
	
	/*
	 * for client-side connection
	 */
	public void setServersAddr(String addr) {
		
		this.serversAddr = addr;
	}
	
	
	
	public int getNumPlayers() {
		
		return this.numPlayers;
	}
	
	
	
	public int getID() {
		
		return id;
	}
	
	
	
	/*
	 * server only. stop establishing connections and play game.
	 * invoked in WaitingRoom.
	 */
	public void ready() {
		
		if(this.numPlayers < 3)
			return;
		
		ready = true;
	}
	
	
	
	public boolean isReady() {
		
		return this.ready;
	}
	
	
	
	/*
	 * server only. invoked by GetNewClients
	 */
	public void oneMorePlayer() {
		
		numPlayers++;
	}
	
	
	
	/*
	 * server only. add to a list of client addresses.
	 */
	public void addClientAddress(String addr) {
		
		addresses.add(addr);
	}
	
	
	
	/*
	 * getter, for accepting new connections
	 */
	public ServerSocket getServerSocket() {
		
		return this.serverSocket;
	}
	
	
	
	/*
	 * add new ObjectOutputStream to outputStreams
	 */
	public void addOutputStream(ObjectOutputStream out) {
		
		this.outputStreams.add(out);
	}
	
	
	
	/*
	 * add new ObjectInputStream to inputStreams
	 */
	public void addInputStream(ObjectInputStream in) {
		
		this.inputStreams.add(in);
	}
	
}
