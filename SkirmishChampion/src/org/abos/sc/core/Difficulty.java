package org.abos.sc.core;

import org.abos.util.Name;

/**
 * An enumeration of the different difficulties complete with methods containing what they
 * entail.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
public enum Difficulty implements Name {
	
	/**
	 * Easy difficulty, great for beginners.
	 */
	EASY,
	
	/**
	 * Medium difficulty for casual gamers.
	 */
	MEDIUM,
	
	/**
	 * Hard difficulty for those looking for an extra challenge.
	 */
	HARD;
	
	/**
	 * Returns the difficulty of the player if one is provided or the default difficulty if not.
	 * This method should be used where a player is optional but a difficulty must be provided.
	 * @param player the player to return the difficulty of, can be <code>null</code>
	 * @return the difficulty of the specified player or the default difficulty if <code>player</code> refers to <code>null</code>.
	 */
	public static final Difficulty of(Player player) {
		if (player == null)
			return MEDIUM; // default difficulty
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
	
	/**
	 * If challenge ratings should be shown, depending on the game difficulty.
	 * @return <code>true</code> if challenge ratings should be displayed for this difficulty, else <code>false</code>
	 */
	public final boolean showChallengeRatings() {
		return this.compareTo(MEDIUM) <= 0;
	}
	
	/**
	 * If warning the player for a weak team is activated, the warning should be 
	 * issued if the difference between the player's team and the enemy encounter is smaller or
	 * equal to this amount.
	 * @see #warnWeakTeam() 
	 */
	public static final int WEAK_TRESHOLD = -50;
	
	/**
	 * If the player should be warned if their team is probably too weak for an encounter, depending on the game difficulty.
	 * @return <code>true</code> if the player should be warned with this difficulty 
	 * if their team is probably too weak for an encounter, else <code>false</code>
	 * @see #WEAK_TRESHOLD
	 */
	public final boolean warnWeakTeam() {
		return this.compareTo(EASY) <= 0;
	}
	
	/**
	 * If stopping the player for a strong team is activated, they should be stopped 
	 * if the ratio between the player's team and the enemy encounter is greater or
	 * equal to this amount.
	 * @see #stopSteamrolling()
	 */
	public static final double STEAMROLL_FACTOR = 1.5;
	
	/**
	 * If the player should be stopped if their team is too strong for an encounter, depending on the game difficulty.
	 * @return <code>true</code> if the player should be stopped with this difficulty 
	 * if their team is too strong for an encounter, else <code>false</code>
	 * @see #STEAMROLL_FACTOR
	 */
	public final boolean stopSteamrolling() {
		return this.compareTo(EASY) > 0;
	}

}
