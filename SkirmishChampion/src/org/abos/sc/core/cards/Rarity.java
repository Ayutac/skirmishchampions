package org.abos.sc.core.cards;

import org.abos.sc.core.Valuable;
import org.abos.util.IllegalArgumentRangeException;
import org.abos.util.Name;
import org.abos.util.SaveString;
import org.abos.util.Utilities;

/**
 * The different rarities for cards.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.7
 * @see #COMMON
 * @see #RARE
 * @see #EPIC
 * @see #LEGENDARY
 */
public enum Rarity implements Name, SaveString, Valuable {

	/**
	 * Common rarity.
	 */
	COMMON("C"),
	
	/**
	 * Rare rarity.
	 */
	RARE("R"),
	
	/**
	 * Epic rarity.
	 */
	EPIC("E"),
	
	/**
	 * Legendary rarity.
	 */
	LEGENDARY("L");
	
	/**
	 * The display name of this class for named comparators.
	 */
	public static final String DISPLAY_NAME = "Rarity";
	
	/**
	 * The display name of the rarity.
	 */
	private final String displayName;
	
	/**
	 * Creates a new rarity enum entry with the given display name.
	 * @param displayName the display name of the rarity, not <code>null</code>
	 * @throws NullPointerException If <code>displayName</code> refers to <code>null</code>.
	 */
	private Rarity(String displayName) {
		Utilities.requireNonNull(displayName, "displayName");
		this.displayName = displayName;
	}
	
	/**
	 * Returns the display name of this difficulty. 
	 * @return the display name of this difficulty, non <code>null</code>
	 */
	@Override
	public final String getName() {
		return displayName;
	}
	
	/**
	 * Returns {@link #name()} but with only the first character being upper case.
	 * @return {@link #name()} but with only the first character being upper case, non <code>null</code>
	 */
	public final String getCapitalizedName() {
		return name().substring(0, 1).concat(name().substring(1).toLowerCase());
	}
	
	/**
	 * The default gold value of a card of this rarity. Equals ({@link Card.DEFAULT_COMBINATION_SIZE}+1)^({@link #ordinal()}).
	 */
	@Override
	public final int getValueGold() {
		int value = 1;
		for (int i = 0; i < ordinal(); i++)
			value *= (Card.DEFAULT_COMBINATION_SIZE+1);
		return value;
	}
	
	/**
	 * The default diamond value of a card of this rarity. Equals 0.
	 */
	@Override
	public final int getValueDiamonds() {
		return 0;
	}
	
	/**
	 * Saves the rarity to a string builder.
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString()
	 * @see #parse(String)
	 */
	@Override
	public final void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		s.append(getName());
	}
	
	/**
	 * Parses a string to a rarity. Allowed strings are the enum name of the rarities,
	 * their capitalized version, their display name or their index. Anything else will throw an exception. 
	 * @param s the string to parse
	 * @return the string as a rarity
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalArgumentRangeException If the string couldn't be parsed.
	 */
	public static Rarity parse(String s) {
		Utilities.requireNonNull(s, "s");
		for (Rarity rarity : values()) 
			if (s.equals(rarity.name()) || s.equals(rarity.getCapitalizedName()) || s.equals(rarity.getName()))
				return rarity;
		try {
			return values()[Integer.parseInt(s)];
		}
		catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentRangeException(ex);
		}
	}
	
}
