package org.abos.sc.core.cards;

import org.abos.util.Name;
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
public enum Rarity implements Name {

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
	
}
