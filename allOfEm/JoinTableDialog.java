/*
 * @author Eric Evans
 * 
 * For Joining existing table by IP/ known address name
 */

//package network;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class JoinTableDialog extends JFrame {
	
	private EstablishAllConnections estconn;
	
	
	
	public JoinTableDialog(EstablishAllConnections ec) {
		
		this.estconn = ec;
		
		this.setTitle("Durak - Join Table");
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
		
		JLabel label = new JLabel("Enter Table Master's Address");
		panel.add(label);
		
		JTextField addr = new JTextField();
		addr.addActionListener(new JoinListener(addr));
		panel.add(addr);
		
		JButton go = new JButton("Join");
		go.addActionListener(new JoinListener(addr));
		panel.add(go);
		
		this.setVisible(true);
	}
	
	
	
	private class JoinListener implements ActionListener {
		
		private JTextField addr;



		public JoinListener(JTextField addr) {
			this.addr = addr;
		}
		
		
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(addr.getText().equals(""))
				return;
			
			estconn.setServersAddr(addr.getText());
			
			setVisible(false);
			dispose();			
		}
		
	}
	
}
