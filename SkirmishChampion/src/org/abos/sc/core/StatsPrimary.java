package org.abos.sc.core;

import org.abos.sc.core.cards.Rarity;
import org.abos.util.IllegalArgumentRangeException;
import org.abos.util.IllegalArgumentTypeException;
import org.abos.util.Name;
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
public enum StatsPrimary implements Name {
	
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
	@Override
	public final String getName() {
		return displayName;
	}
	
	/**
	 * Returns {@link #name()} but with only the first character being upper case.
	 * @return {@link #name()} but with only the first character being upper case.
	 */
	public final String getCapitalizedName() {
		return name().substring(0, 1).concat(name().substring(1).toLowerCase());
	}
	
	/**
	 * Parses a string to a primary stat. Allowed strings are the enum name of the stats,
	 * their capitalized version, their display name or their index. Anything else will throw an exception. 
	 * @param s the string to parse
	 * @return the string as a primary stat
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalArgumentRangeException If the string couldn't be parsed.
	 */
	public static StatsPrimary parse(String s) {
		Utilities.requireNonNull(s, "s");
		for (StatsPrimary stat : values()) 
			if (s.equals(stat.name()) || s.equals(stat.getCapitalizedName()) || s.equals(stat.getName()))
				return stat;
		try {
			return values()[Integer.parseInt(s)];
		}
		catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentRangeException(ex);
		}
	}

}
