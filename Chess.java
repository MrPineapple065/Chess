import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The <code>Chess</code> class initiates most
 * of the logic for the game of Chess.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 *
 */
public class Chess {
	public static void main(String[] args) {
		Player[]	players	= new Player[2];
		
		for (int i = 0; i < players.length; i ++) {
			String name = JOptionPane.showInputDialog(null, "Player " + (i + 1) + ", enter your name.", "", JOptionPane.PLAIN_MESSAGE);
			
			if (name == null) {
				System.exit(0);
			}
			
			players[i] = new Player(name , Player.playerColors[i]);
		}
		
		JFrame frame = new JFrame("Chess");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.add(new ChessBoardPanel(players));
		frame.pack();
		frame.setSize(720, 800);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
		        switch (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Careful!", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null)) {

		        case JOptionPane.YES_OPTION:
		            System.exit(0);
		            break;
		        
		        default:
		        	break;
		        }
		    }
		});
	}
}