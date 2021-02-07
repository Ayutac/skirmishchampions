package org.abos.sc.core;

import org.abos.util.Name;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
public enum Difficulty implements Name {
	
	EASY,
	
	MEDIUM,
	
	HARD;
	
	public static final Difficulty of(Player player) {
		if (player == null)
			return MEDIUM;
		return player.getDifficulty();
	}
	
	/**
	 * Returns the display name of this difficulty. 
	 * @return the display name of this difficulty
	 */
	@Override
	public String getName() {
		// #name() but with only the first character being upper case
		return name().substring(0, 1).concat(name().substring(1).toLowerCase());
	}
	
	public final boolean showChallengeRatings() {
		return this.compareTo(MEDIUM) <= 0;
	}
	
	public static final int WEAK_TRESHOLD = -50;
	
	public final boolean warnWeakTeam() {
		return this.compareTo(EASY) <= 0;
	}
	
	public static final double STEAMROLL_FACTOR = 1.5;
	
	public final boolean stopSteamrolling() {
		return this.compareTo(EASY) > 0;
	}

}
