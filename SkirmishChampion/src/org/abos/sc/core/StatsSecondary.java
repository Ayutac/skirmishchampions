package org.abos.sc.core;

import org.abos.util.IllegalArgumentRangeException;
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
	CONSTITUTION("CONS", "attacked"),
	
	/**
	 * For the mental stat of the character.
	 */
	MENTAL("MENT", "debated"),
	
	/**
	 * For the eloquence stat of the character.
	 */
	ELOQUENCE("ELOQ", "charmed");
	
	/**
	 * The display name of the secondary stat.
	 */
	private final String displayName;
	
	/**
	 * The verb to communicate an attack.
	 */
	private final String attackVerb;
	
	/**
	 * The number of different secondary stats.
	 */
	public static final int SIZE = 3;
	
	/**
	 * Creates a new secondary stat enum entry with the given display name.
	 * @param displayName the display name of the secondary stat, not <code>null</code>
	 * @param attackVerb the attack verb of the secondary stat, not <code>null</code>
	 * @throws NullPointerException If <code>displayName</code> or <code>attackVerb</code> refers to <code>null</code>.
	 */
	private StatsSecondary(String displayName, String attackVerb) {
		Utilities.requireNonNull(displayName, "displayName");
		Utilities.requireNonNull(attackVerb, "attackVerb");
		this.displayName = displayName;
		this.attackVerb = attackVerb;
	}
	
	/**
	 * Returns the display name of the secondary stat.
	 * @return the display name of the secondary stat, guaranteed to be non <code>null</code>
	 */
	@Override
	public final String getName() {
		return displayName;
	}
	
	/**
	 * Returns the attack verb of the secondary stat in past tense.
	 * @return the attack verb of the secondary stat, guaranteed to be non <code>null</code>
	 */
	public final String getAttackVerb() {
		return attackVerb;
	}
	
	/**
	 * Returns {@link #name()} but with only the first character being upper case.
	 * @return {@link #name()} but with only the first character being upper case.
	 */
	public final String getCapitalizedName() {
		return name().substring(0, 1).concat(name().substring(1).toLowerCase());
	}
	
	/**
	 * Parses a string to a secondary stat. Allowed strings are the enum name of the stats,
	 * their capitalized version, their display name or their index. Anything else will throw an exception. 
	 * @param s the string to parse
	 * @return the string as a secondary stat
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalArgumentRangeException If the string couldn't be parsed.
	 */
	public static StatsSecondary parse(String s) {
		Utilities.requireNonNull(s, "s");
		for (StatsSecondary stat : values()) 
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
