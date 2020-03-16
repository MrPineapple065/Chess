import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * The <code>ChessBoardPanel</code> class represents a
 * the chess board along with its pieces and coordinates. </br>
 * It also allows the user to interact with the board.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 *
 */
public class ChessBoardPanel extends JPanel implements ActionListener {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 0x966E0D65A8861CA5L;

	/**
	 * An <code>Array</code> holding all <code>Player</code> in the game.
	 */
	private Player[]	players				= new Player[2];
	
	/**
	 * An <code>Array</code> holding all <code>JLabel</code> for all <code>Player</code> in game. </br>
	 * Used for GUI.
	 */
	private JLabel[]	playerLabel			= new JLabel[2];
	
	/**
	 * The actual <code>ChessBoard</code>.
	 */
	private ChessBoard	board;
	
	/**
	 * Creates a <code>ChessBoardPanel</code> with <code>Player[] p</code>
	 * @throws	IllegalArgumentException if <code>p</code> contains
	 * 			more than two instances of <code>Player</code>.
	 * @param p the <code>Players</code> playing
	 */
	public ChessBoardPanel(Player[] p) throws IllegalArgumentException {
		/**Set default GUI Elements*/
		super();
		setLayout(new GridLayout(10, 9));
		
		UIManager.put("OptionPane.messageFont",	new Font("", Font.PLAIN, 30));
		UIManager.put("OptionPane.buttonFont",	new Font("", Font.PLAIN, 20));
		UIManager.put("Button.font",			new Font("Arial", Font.PLAIN, 20));
		UIManager.put("Label.font",				new Font("", Font.PLAIN, 30));
		
		/**Set other attributes*/
		
		if (p.length > 2) {
			throw new IllegalArgumentException("The number of Players exceedes the expected range");
		}
		
		else {
			this.players = p;
		}
		
		this.board = new ChessBoard(this, this.players);
		
		/**Creates other GUI elements*/
		this.createLabels();
		this.createTiles();
	}
	
	
	/**
	 * @return <code>{@link #board}</code>.
	 */
	public ChessBoard getBoard() {
		return this.board;
	}
	
	/**
	 * Create <code>JLabel</code> to add to the <code>ChessBoardPanel</code>.
	 */
	private void createLabels() {
		this.playerLabel[0] = new JLabel(this.players[0].getName(), JLabel.CENTER);
		this.playerLabel[0].setForeground(this.players[0].getPlayerColor());
		this.playerLabel[1] = new JLabel(this.players[1].getName(), JLabel.CENTER);
		this.playerLabel[1].setForeground(this.players[1].getPlayerColor());
		
		/**
		 * add empty space
		 */
		for (int i = 0; i < 3; i ++) {
			add(new JLabel(""));
		}
		
		/**
		 * Display all <code>Player</code>
		 */
		add(playerLabel[0]);
		add(new JLabel("vs", JLabel.CENTER));
		add(playerLabel[1]);
		
		/**
		 * add empty space
		 */
		for (int i = 0; i < 3; i ++) {
			add(new JLabel("", JLabel.CENTER));
		}
	}
	
	/**
	 *  Create <code>Tile</code> to add to <code>ChessBoardPanel</code>.
	 */
	private void createTiles() {
		char[]	columnDictation	= new char[]{'a','b','c','d','e','f','g','h'};
		int		num				= columnDictation.length;
		
		for (Tile[] row : this.board.getBoard()) {
			add(new JLabel(String.valueOf(num), JLabel.CENTER));
			num--;
			for (Tile tile : row) {
				this.add(tile);
			}
		}
		
		JButton menuButton = new JButton("Menu");
		menuButton.addActionListener(this);
		menuButton.setOpaque(false);
		menuButton.setContentAreaFilled(false);
		
		add(menuButton);
		for (char c : columnDictation) {
			add(new JLabel(String.valueOf(c), JLabel.CENTER));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/**Determine which option is chosen.*/
		switch (JOptionPane.showOptionDialog(null, "Pick an option", "Menu", JOptionPane.DEFAULT_OPTION , JOptionPane.PLAIN_MESSAGE, null, new String[] {"Scores", "Reset", "Quit", "Resign", "Controls"}, 0)) {
		
		/**Display the scores of each <code>Player</code>.*/
		case 0:
			JOptionPane.showMessageDialog(null, this.players[0].getName() + ": " + this.players[0].getScore() + "\n" +
			this.players[1].getName() + ": " + this.players[1].getScore(),
			"Scores", JOptionPane.PLAIN_MESSAGE, null);
			break;
		
		/**Reset the <code>Board</code>.*/
		case 1:
			switch (JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null)) {
			
			case JOptionPane.YES_OPTION:
				this.board.reset();
				JOptionPane.showMessageDialog(null, "Board has been Reset", "", JOptionPane.PLAIN_MESSAGE, null);
				break;
				
			default:
				break;
			}
			break;
			
		case 2:
			switch (JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null)) {
			
			case JOptionPane.YES_OPTION:
				System.exit(0);
				break;
				
			default:
				break;
			}
			break;
			
		/**<code>Player</code> forfeits the match.*/
		case 3:
			if (!this.board.getGameOver()) {
				switch(JOptionPane.showConfirmDialog(null, this.players[this.board.getCurrentPlayer()].getName() + ", are you sure you want to resign?", "", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null)) {
				
				case JOptionPane.YES_OPTION: 
					this.board.setGameOver(true);
					JOptionPane.showMessageDialog(null, this.players[this.board.getNextPlayer()].getName() + " wins!");
					break;
				
				default:
					break;
				}
			}
			
			else {
				this.board.removeTileListeners();
				JOptionPane.showMessageDialog(null, "The Game is Over!", "Game Over!", JOptionPane.PLAIN_MESSAGE, null);
			}
			break;
		
		/**Display the controles of the game*/
		case 4:
			JTextArea jta = new JTextArea("Escape:\tPause\ne:\tdeselect piece\nr:\tReset\nq:\tQuit");
			jta.setOpaque(false); jta.setEditable(false);
			jta.setFont(new Font("Arial", Font.PLAIN, 20));
			JOptionPane.showMessageDialog(null, jta, "Controls", JOptionPane.PLAIN_MESSAGE, null);
			break;
		
		default:
			break;
		}
	}
}