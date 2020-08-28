import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * <p>The {@code ChessBoardPanel} class represents a
 * the chess board along with its pieces and coordinates. </p>
 * <p>It also allows the user to interact with the board.</p>
 * 
 * @version 21 March 2020
 * @since 21 March 2020
 * @author MrPineapple065
 */
public final class ChessBoardPanel extends JPanel implements ActionListener {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 0x966E0D65A8861CA5L;
	
	/**
	 * Standard {@link Font}
	 */
	private static final Font standardFont = new Font("", Font.PLAIN, 20);
	
	/**
	 * The actual {@link ChessBoard}.
	 */
	private ChessBoard board;
	
	/**
	 * An {@code Array} holding all {@link JLabel} for all {@link Player} in game. <br>
	 * Used for GUI.
	 */
	private JLabel[] playerLabel = new JLabel[2];
	
	/**
	 * An {@code Array} holding all {@link Player} in the game.
	 */
	private Player[] players = new Player[2];
	
	/**
	 * Creates a {@code ChessBoardPanel} with {@code p}.
	 * 
	 * @param p is the {@code Array} of {@code Player} playing.
	 * 
	 * @throws IllegalArgumentException if {@code p} contains more than <b>two</b> instances of {@link Player}.
	 */
	public ChessBoardPanel(Player[] p) throws IllegalArgumentException {
		super();
		if (p.length != 2) throw new IllegalArgumentException("The number of Players exceedes the expected range");
		this.players = p;
		this.board = new ChessBoard(this, this.players);
		
		//Set Default GUI Elements
		setLayout(new GridLayout(10, 9));
		
		UIManager.put("OptionPane.messageFont",	standardFont);
		UIManager.put("OptionPane.buttonFont",	standardFont);
		UIManager.put("Button.font",			new Font("Arial", Font.PLAIN, 18));
		UIManager.put("Label.font",				standardFont);
		UIManager.put("Label.background", 		null);
		UIManager.put("Label.foreground",		Color.BLACK);
		
		//Creates other GUI elements
		this.createLabels();
		this.createTiles();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//Determine which option is chosen.
		switch (JOptionPane.showOptionDialog(null, "Pick an option", "Menu", JOptionPane.DEFAULT_OPTION , JOptionPane.PLAIN_MESSAGE, null, new String[] {"Scores", "Reset", "Quit", "Resign", "Controls"}, 0)) {
		case 0:
			this.scoresOption();
			return;
		case 1:
			this.resetOption();
			return;
		case 2:
			this.quitOption();
			return;
		case 3:
			this.resignOption();
			return;
		case 4:
			this.controlsOption();
			return;
		default:
			return;
		}
	}
	
	/**
	 * Display the controls of the game.
	 */
	public void controlsOption() {
		JTextArea jta = new JTextArea("Escape:\tPause\ne:\tdeselect piece\ns:\tScores\nr:\tReset\nq:\tQuit\nf:\tResign\nc:\tControls");
		jta.setOpaque(false); jta.setEditable(false);
		jta.setFont(new Font("Arial", Font.PLAIN, 20));
		JOptionPane.showMessageDialog(null, jta, "Controls", JOptionPane.PLAIN_MESSAGE, null);
	}
	
	/**
	 * Create {@link JLabel} to add to the {@link ChessBoardPanel}.
	 */
	private void createLabels() {
		this.playerLabel[0] = new JLabel(this.players[0].getName(), JLabel.CENTER);
		this.playerLabel[1] = new JLabel(this.players[1].getName(), JLabel.CENTER);
		
		//Empty Space
		for (int i = 0; i < 3; i ++) {
			add(new JLabel(""));
		}
		
		//Display all {@link Player}
		add(playerLabel[0]);
		add(new JLabel("vs", JLabel.CENTER));
		add(playerLabel[1]);
		
		//Empty space
		for (int i = 0; i < 3; i ++) {
			add(new JLabel("", JLabel.CENTER));
		}
	}
	
	/**
	 *  Adds {@link Tile} to this.
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
	public boolean equals(Object obj) {
		if (this == obj)									return true;
		if (!(obj instanceof ChessBoardPanel))				return false;
		ChessBoardPanel other = (ChessBoardPanel)obj;
		if (board == null) if (other.board != null)			return false;
		else if (!board.equals(other.board))				return false;
		if (!Arrays.equals(playerLabel, other.playerLabel))	return false;
		if (!Arrays.equals(players, other.players))			return false;
		return true;
	}
	
	/**
	 * @return {@link #board}
	 */
	public ChessBoard getBoard() {
		return this.board;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + Arrays.hashCode(playerLabel);
		result = prime * result + Arrays.hashCode(players);
		return result;
	}
	
	/**
	 * Quit the game.
	 */
	public void quitOption() {
		switch (JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null)) {
		case JOptionPane.YES_OPTION:
			System.exit(0);
		default:
			return;
		}
	}
	
	/**
	 * Reset {@link #board}.
	 */
	public void resetOption() {
		switch (JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null)) {
		case JOptionPane.YES_OPTION:
			this.board.reset();
			JOptionPane.showMessageDialog(null, "Board has been Reset", "", JOptionPane.PLAIN_MESSAGE, null);
		default:
			return;
		}
	}
	
	/**
	 * A {@link Player} has resigned.
	 */
	public void resignOption() {
		if (!this.board.getGameOver())
			switch(JOptionPane.showConfirmDialog(null, this.board.getCurrentPlayer().getName() + ", are you sure you want to resign?", "", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null)) {
			case JOptionPane.YES_OPTION: 
				this.board.setGameOver(true);
				JOptionPane.showMessageDialog(null, this.board.getNextPlayer().getName() + " wins!");
			default:
				return;
			}
		JOptionPane.showMessageDialog(null, "The Game is Over!", "Game Over!", JOptionPane.PLAIN_MESSAGE, null);
	}
	
	/**
	 * Display the current scores of the {@link Player}
	 */
	public void scoresOption() {
		JOptionPane.showMessageDialog(null, String.format("%s%n%s", this.players[0].toString(), this.players[1].toString()), "Scores", JOptionPane.PLAIN_MESSAGE, null);
	}


	@Override
	public String toString() {
		return "ChessBoardPanel [players=" + Arrays.toString(players) + ", playerLabel=" + Arrays.toString(playerLabel)
				+ ", board=" + board + "]";
	}
}