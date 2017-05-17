/*
 * @author Eric M Evans
 * 
 * Waiting room for all other players to join table
 */

package network;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class WaitingRoom extends JFrame implements Observer {
	
	private EstablishAllConnections	estconn;
	private JButton					ready;
	private JLabel					total;
	
	
	
	public WaitingRoom(EstablishAllConnections establishConnection) {
		this.estconn = establishConnection;
		
		this.setTitle("Durak - Waiting Room");
		this.setSize(600, 275);
		this.setLocation(200, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setSize(this.getWidth() - 50, this.getHeight() - 150);
		panel.setLocation(25, 50);
		panel.setLayout(new GridLayout(3, 1, 10, 10));
		this.add(panel);
		
		JLabel label = new JLabel("Player " + (estconn.getID() + 1));
		panel.add(label);
		
		total = new JLabel("Currently " + estconn.getNumPlayers()
		        + " Players Have Joined This Table");
		panel.add(total);
		
		if (estconn.getID() == 0) {
			ready = new JButton("Start Game");
			ready.addActionListener(new StartListener());
			panel.add(ready);
			label.setText("Player 1 -- Table Master");
		}
		else {
			JLabel waiting = new JLabel("Waiting on Table Master...");
			panel.add(waiting);
		}
		
		this.setVisible(true);
	}
	
	
	
	private class StartListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			// System.out.println("Server Clicked Ready");
			estconn.ready();
		}
		
	}
	
	
	
	@Override
	public void update(Observable arg0, Object arg1) {
		
		total.setText("Currently " + estconn.getNumPlayers()
		        + " Players Have Joined This Table");
		
		if (estconn.isReady()) {
			setVisible(false);
			dispose();
		}
	}
}
