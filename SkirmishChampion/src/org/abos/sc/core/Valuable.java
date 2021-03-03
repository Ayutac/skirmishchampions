package org.abos.sc.core;

/**
 * An interface for something that can be bought or sold.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.7
 */
public interface Valuable {

	/**
	 * How much gold this is worth. Note that the value of this object is gold and diamonds combined.
	 * @return how much gold this is worth
	 * @see #getValueDiamonds()
	 */
	public int getValueGold();
	
	/**
	 * How much diamonds this is worth. Note that the value of this object is gold and diamonds combined.
	 * @return how much diamonds this is worth
	 * @see #getValueGold()
	 */
	public int getValueDiamonds();
	
}
