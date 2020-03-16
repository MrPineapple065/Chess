import java.awt.Color;

/**
 * This <code>Knight</code> class represents a
 * Knight in the game of chess.
 * 
 * @version 16 March 2020
 * @author MrPineapple
 *
 */
public class Knight extends Piece {
	/**
	 * The amount of points that <code>Knight</code> is worth.
	 */
	private static final int	VALUE	= 3;
	
	/**
	 * Creates a <code>Knight</code> that is <code>color</code>.
	 * 
	 * @param color is the color of <code>Knight</code>
	 * @throws IllegalClassFormatException if <code>color</code> is <code>null</code>.
	 */
	public Knight(Color color) throws IllegalArgumentException {
		super(color);
	}
	
	/**
	 * Determine the value of <code>Knight</code>.
	 * 
	 * @return <b><i>VALUE</i></b>
	 */
	public static int getValue() {
		return VALUE;
	}
	
	/**
	 * Determine if move <code>Knight</code> makes from <code>tiles[0]</code> to <code>tiles[1]</code> is legal.
	 * <code>Knight</code> can <i>only</i> in an <i>"L"</i> shape.
	 * 
	 * @param tiles are the original and new positions of <code>Knight</code>.
	 * 
	 * @return  <code>true</code> if move is legal. </br>
	 * 			<code>false</code> is move is illegal.
	 */
	public static boolean getLegal(Tile[] tiles) throws IllegalArgumentException {
		if (tiles == null) {
			throw new IllegalArgumentException("Knight must move.");
		}
		
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		
		//Vertical L
		if ((Math.abs(newX - oldX) == 1) && (Math.abs(newY - oldY) == 2)) {
			return true;
		}
		
		//Horizontal L
		else if ((Math.abs(newX - oldX) == 2) && (Math.abs(newY - oldY) == 1)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determine all <code>Tile</code> that the <code>Knight</code>
	 * will take on its journey from <code>tiles[0]</code> to <code>tiles[1]</code>. </br>
	 * Since <code>Knight</code> can jump over pieces it <b>cannot collide with anything</b>.
	 * 
	 * @return	an <code>Array</code> of <code>Tile<code> that the <code>Knight</code>
	 *			will take on its journey from <code>tiles[0]</code> to <code>tiles[1]</code>.
	 */
	public static Tile[] setTilesCollide() {
		return new Tile[0];
	}
	
	/**
	 * @return <code>String</code> representation of <code>Knight</code>.
	 */
	@Override
	public String toString() {
		if (super.getPieceColor().equals(Piece.WHITE)) {
			return "\u2658";
		}
		
		return "\u265E";
	}
}