/*
 * @author Eric M Evans
 * 
 * For when connection fails to connect
 */
package network;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CannotConnectDialog extends JFrame {
	
	public CannotConnectDialog(String address) {
		
		this.setTitle("Durak - Error");
		this.setSize(600, 275);
		this.setLocation(200, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setSize(this.getWidth() - 50, this.getHeight() - 150);
		panel.setLocation(25, 50);
		panel.setLayout(new GridLayout(1, 1, 10, 10));
		this.add(panel);
		
		JLabel label = new JLabel("Cannot Connect To \"" + address + "\"");
		panel.add(label);
		
		this.setVisible(true);
	}
	
}
