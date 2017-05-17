/*
 * @author Eric M Evans
 * 
 * For getting response of whether to start a new table or join an existing
 * table
 */

//package network;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StartingRoom extends JFrame {
	
	private EstablishAllConnections waitingRoom;
	
	
	
	public StartingRoom(EstablishAllConnections waitingRoom) {
		
		this.waitingRoom = waitingRoom;
		
		this.setTitle("Durak - Start New Game");
		this.setSize(600, 275);
		this.setLocation(200, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setSize(this.getWidth() - 50, this.getHeight() - 150);
		panel.setLocation(25, 50);
		panel.setLayout(new GridLayout(1, 0, 10, 10));
		this.add(panel);
		
		JButton newTable = new JButton("Start New Table");
		// newTable.setSize(200, 200);
		newTable.addActionListener(new NewTableListener());
		panel.add(newTable);
		
		JButton joinTable = new JButton("Join An Existing Table");
		joinTable.addActionListener(new JoinTableListener());
		// joinTable.setSize(200, 100);
		panel.add(joinTable);
		
		this.setVisible(true);
	}
	
	
	
	private void setAndClose(Choice ch) {
		
		waitingRoom.setChoice(ch);
		
		setVisible(false);
		dispose();
	}
	
	
	
	private class NewTableListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			setAndClose(Choice.NEW_TABLE);
		}
		
	}
	

	private class JoinTableListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			setAndClose(Choice.JOIN_TABLE);
		}
		
	}
}
