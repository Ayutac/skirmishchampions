package org.abos.sc.core;

import org.abos.util.Name;
import org.abos.util.Utilities;

/**
 * An enumeration to differ between the secondary stats of a character.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see #CONSTITUTION
 * @see #MENTAL
 * @see #ELOQUENCE
 */
public enum StatsSecondary implements Name {
	/*
	 * If you change this enumeration be aware that at least
	 * CharacterBae#getSecondaryStat() must be changed as well.
	 */
	
	/**
	 * For the constitution stat of the character.
	 */
	CONSTITUTION("CONS"),
	
	/**
	 * For the mental stat of the character.
	 */
	MENTAL("MENT"),
	
	/**
	 * For the eloquence stat of the character.
	 */
	ELOQUENCE("ELOQ");
	
	/**
	 * The display name of the secondary stat.
	 */
	private final String displayName;
	
	/**
	 * The number of different secondary stats.
	 */
	public static final int SIZE = 3;
	
	/**
	 * Creates a new secondary stat enum entry with the given display name.
	 * @param displayName the display name of the secondary stat, not <code>null</code>
	 * @throws NullPointerException If <code>displayName</code> refers to <code>null</code>.
	 */
	private StatsSecondary(String displayName) {
		Utilities.requireNonNull(displayName, "displayName");
		this.displayName = displayName;
	}
	
	/**
	 * Returns the display name of the secondary stat.
	 * @return the display name of the secondary stat, guaranteed to be non <code>null</code>
	 */
	@Override
	public String getName() {
		return displayName;
	}
	
	/**
	 * Returns {@link #name()} but with only the first character being upper case.
	 * @return {@link #name()} but with only the first character being upper case.
	 */
	public String getCapitalizedName() {
		return name().substring(0, 1).concat(name().substring(1).toLowerCase());
	}

}
