package org.abos.sc.core;

import org.abos.util.Name;
import org.abos.util.Utilities;

/**
 * An enumeration to differ between several predefined battle strategies and the custom ones.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public enum BattleStrategyType implements Name {

	/**
	 * For the strategy where the enemy is defeated row by row.
	 */
	ROW("Row Assault"),
	
	/**
	 * For the strategy where the enemy is defeated column by column.
	 */
	COL("Column Assault"),
	
	/**
	 * For the strategy where the combatants gang up on one enemy after another.
	 */
	CONCENTRATED("Concentrated Assault"),
	
	/**
	 * For customized assaults.
	 */
	CUSTOM("Customized Assault");
	
	/**
	 * The display name of the strategy.
	 */
	private final String displayName;
	
	/**
	 * Creates a new battle strategy enum entry with the given display name.
	 * @param displayName the display name of the strategy, not <code>null</code>
	 * @throws NullPointerException If <code>displayName</code> refers to <code>null</code>.
	 */
	private BattleStrategyType(String displayName) {
		Utilities.requireNonNull(displayName, "displayName");
		this.displayName = displayName;
	}
	
	/**
	 * Returns the display name of the strategy
	 * @return the display name of the strategy, guaranteed to be non <code>null</code>
	 */
	@Override
	public final String getName() {
		return displayName;
	}
	
}
