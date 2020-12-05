package org.abos.sc.core;

/**
 * An enumeration for battle outcomes.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see #WON
 * @see #LOST
 * @see #TIE
 */
public enum BattleConclusion {
	
	/**
	 * Indicates that the battle was won.
	 */
	WON,
	
	/**
	 * Indicates that the battle was lost.
	 */
	LOST,
	
	/**
	 * Indicates that a tie occured.
	 */
	TIE;

}
