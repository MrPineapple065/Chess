import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * This <code>Tile</code> class represents a Tile
 * on the Chess Board.
 * 
 * @version 16 March 2020
 * @author MrPineapple070
 *
 */
class Tile extends JButton implements MouseListener, KeyListener {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 0x851FA7B9FE81A8EDL;
	
	/**
	 * An <code>Array</code> of <code>Color</code> that <code>Tile</code> could be.
	 */
	private static final Color[]	tileColors	= new Color[] {new Color(0xc89669), new Color(0x73463c)};
	
	/**
	 * The {@link #ChessBoardPanel} that holds <code>this</code>.
	 */
	private final ChessBoardPanel	boardPanel;
	
	/**
	 * The {@link #Color} of <code>Tile</code>.
	 */
	private final Color	tileColor;
	
	/**
	 * The column that <code>this</code> is in.
	 */
	private final int		column;
	
	/**
	 * The row that <code>this</code> is in.
	 */
	private final int		row;
	
	/**
	 * The {@link #Piece} on the <code>Tile</code>.</br>
	 * <code>null</code> if no <code>Piece</code> is on the <code>Tile</code>.
	 */
	private Piece	piece;
	
	/**
	 * Create <code>Tile</code> with all attributes defined.
	 * 
	 * @param b is the {@link ChessBoardPanel} holding <code>this</code>.
	 * @param color is the {@link Color} of <code>this</code>.
	 * @param col is the column that <code>this</code> is in.
	 * @param row is the row that <code>this</code> is in.
	 * 
	 * @throws IllegalArgumentException if  <code>b</code> or <code>color</code> are <code>null</code>.
	 * @throws IndexOutOfBoundsException if <code>Tile</code> trying to be created out of bounds.
	 * 
	 */
	public Tile(final ChessBoardPanel b, final int col, final int row) throws IllegalArgumentException, IndexOutOfBoundsException {
		super(null, null);
		
		if (b == null) {
			throw new IllegalArgumentException("Missing ChessBoardPanel.");
		}
		
		else {
			this.boardPanel	= b;
		}
		
		if ((col < 0 || col > 7) || (row < 0 || row > 7)) {
			throw new IndexOutOfBoundsException("Tile must exist on the board.");
		}
		
		else {
			this.column	= col;
			this.row	= row;
		}
		
		this.tileColor = (this.row % 2 == 0 && this.column % 2 == 0) || (this.row % 2 == 1 && this.column % 2 == 1) ? Tile.tileColors[0] : Tile.tileColors[1];
		
		//Set GUI Elements
		super.setFont(new Font("", Font.PLAIN, 40));	this.setBackground(this.tileColor);
		this.setHorizontalAlignment(JButton.CENTER);	this.setVerticalAlignment(JButton.CENTER);
		this.setFocusPainted(false);
		
		//Add Interactivity
		this.addKeyListener(this);	this.addMouseListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	/**
	 * Determine the {@link #Piece} on <code>Tile</code>
	 * 
	 * @return <code>{@link #piece}</code>
	 */
	public Piece getPiece() {
		return this.piece;
	}
	
	/**
	 * Determine the {@link #column} of <code>Tile</code>.
	 * 
	 * @return <code>{@link #column}</code>
	 */
	public int getColumn() {
		return this.column;
	}
	
	/**
	 * Determine the {@link #row} of <code>Tile</code>.
	 * 
	 * @return <code>{@link #row}</code>
	 */
	public int getRow() {
		return this.row;
	}
	
	/**
	 * Determine the {@link #tileColor} of <code>Tile</code>.
	 * 
	 * @return {@link #tileColor}
	 */
	public Color getTileColor() {
		return this.tileColor;
	}
	
	/**
	 * Set {@link #piece} to <code>piece</code>.
	 * 
	 * @param piece is the new <code>Piece</code>.
	 * 
	 * @return the new piece
	 */
	public Piece setPiece(Piece piece) {
		this.piece = piece;
		return this.piece;
	}
	
	/**
	 * Update GUI once a move has been completed.
	 * 
	 * @return <tt>true</tt> when updated.
	 */
	public boolean update() {
		if (this.piece != null) {
			this.setText(this.piece.toString());
			this.setForeground(this.piece.getPieceColor());
		}
		
		else {
			this.setText("");
			this.setForeground(null);
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardPanel == null) ? 0 : boardPanel.hashCode());
		result = prime * result + column;
		result = prime * result + ((piece == null) ? 0 : piece.hashCode());
		result = prime * result + row;
		result = prime * result + ((tileColor == null) ? 0 : tileColor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (boardPanel == null) {
			if (other.boardPanel != null)
				return false;
		} else if (!boardPanel.equals(other.boardPanel))
			return false;
		if (column != other.column)
			return false;
		if (piece == null) {
			if (other.piece != null)
				return false;
		} else if (!piece.equals(other.piece))
			return false;
		if (row != other.row)
			return false;
		if (tileColor == null) {
			if (other.tileColor != null)
				return false;
		} else if (!tileColor.equals(other.tileColor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String str = "";
		
		switch (this.column) {
		case 0:
			str += 'a';
			break;
		case 1:
			str += 'b';
			break;
		case 2:
			str += 'c';
			break;
		case 3:
			str += 'd';
			break;
		case 4:
			str += 'e';
			break;
		case 5:
			str += 'f';
			break;
		case 6:
			str += 'g';
			break;
		case 7:
			str += 'h';
			break;
		}
		
		return str + String.valueOf(8 - this.getRow());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		switch (e.getButton()) {
		
		/**Left Click*/
		case MouseEvent.BUTTON1:
			this.boardPanel.getBoard().movePiece(this);
			break;
			
		/**Middle Click*/
		case MouseEvent.BUTTON2:
			String tileText = this.getText();
			if (("".equals(tileText)) || ! ((this.getRow() + ", " + this.getColumn()).equals(tileText))) {
				this.setForeground(new Color(0x333333));
				this.setText(this.getRow() + ", " + this.getColumn());
			}
			
			else {
				if (this.getPiece() == null) {
					this.setText(null);
					this.setForeground(null);
				}
				
				else {
					this.setText(this.getPiece().toString());
					this.setForeground(this.getPiece().getPieceColor());
				}
			}
			break;
		
		/**Right Click*/
		case MouseEvent.BUTTON3:
			String tileText1 = this.getText();
			if (("".equals(tileText1)) || ! ((this.toString()).equals(tileText1))) {
				this.setForeground(new Color(0x333333));
				this.setText(this.toString());
			}
			
			else {
				if (this.getPiece() == null) {
					this.setText(null);
					this.setForeground(null);
				}
				
				else {
					this.setText(this.getPiece().toString());
					this.setForeground(this.getPiece().getPieceColor());
				}
			}
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.getModel().setPressed(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.getModel().setPressed(false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.setBorder(BorderFactory.createLineBorder(new Color(0x4492a6), 3));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.setBorder(null);		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()) {
		
		case KeyEvent.VK_ESCAPE:
			this.boardPanel.actionPerformed(null);
			break;
		
		case 'e':
			ChessBoard board = this.boardPanel.getBoard();
			if (board.getTiles()[1] == null) {
				board.resetTiles();
				JOptionPane.showMessageDialog(null, "Piece deselected", "", JOptionPane.PLAIN_MESSAGE, null);
			}
			break;
		
		case 'r':
			switch (JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "", JOptionPane.YES_NO_OPTION, JOptionPane.YES_OPTION, null)) {
			case JOptionPane.YES_OPTION:
				this.boardPanel.getBoard().reset();
				break;
			
			default:
				break;
			}
			break;
			
		case 'q':
			switch (JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null)) {
			
			case JOptionPane.YES_OPTION:
				System.exit(0);
				break;
				
			default:
				break;
			}			
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
}