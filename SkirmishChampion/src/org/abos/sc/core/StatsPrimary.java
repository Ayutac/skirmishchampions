package org.abos.sc.core;

import org.abos.util.Utilities;

/**
 * An enumeration to differ between the primary stats of a character.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see #STRENGTH
 * @see #DEXTERITY
 * @see #INTELLIGENCE
 * @see #WISDOM
 * @see #ROMANCE
 * @see #CHARISMA
 * @see #SPECIAL
 * @see #SPEED
 */
public enum StatsPrimary {
	
	/**
	 * For the strength stat of the character.
	 */
	STRENGTH("STR"), 

	/**
	 * For the dexterity stat of the character.
	 */
	DEXTERITY("DEX"), 
	
	/**
	 * For the intelligence stat of the character.
	 */
	INTELLIGENCE("INT"), 
	
	/**
	 * For the wisdom stat of the character.
	 */
	WISDOM("WIS"), 
	
	/**
	 * For the romance stat of the character.
	 */
	ROMANCE("ROM"), 
	
	/**
	 * For the charisma stat of the character.
	 */
	CHARISMA("CHA"),
	
	/**
	 * For the special stat of the character.
	 */
	SPECIAL("SPE"),
	
	/**
	 * For the speed stat of the character.
	 */
	SPEED("SPD");
	
	/**
	 * The display name of the primary stat.
	 */
	private final String displayName;
	
	/**
	 * The number of different primary stats.
	 */
	public static final int SIZE = 8;
	
	/**
	 * Creates a new primary stat enum entry with the given display name.
	 * @param displayName the display name of the primary stat, not <code>null</code>
	 * @throws NullPointerException If <code>displayName</code> refers to <code>null</code>.
	 */
	private StatsPrimary(String displayName) {
		Utilities.requireNonNull(displayName, "displayName");
		this.displayName = displayName;
	}
	
	/**
	 * Returns the display name of the primary stat.
	 * @return the display name of the primary stat, guaranteed to be non <code>null</code>
	 */
	public String getDisplayName() {
		return displayName;
	}

}
