package org.abos.sc.core;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
public enum Difficulty {
	
	EASY,
	
	MEDIUM,
	
	HARD;
	
	public static final Difficulty of(Player player) {
		if (player == null)
			return MEDIUM;
		return player.getDifficulty();
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
