package org.abos.sc.core;

import org.abos.util.Name;
import org.abos.util.Utilities;

/**
 * An enumeration of the different difficulties complete with methods containing what they
 * entail.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
public enum Difficulty implements Name {
	
	/**
	 * Easiest difficulty, if you need your hand hold.
	 */
	EASIEST("Well Done"),
	
	/**
	 * Easy difficulty, great for beginners.
	 */
	EASY("Medium Well"),
	
	/**
	 * Medium difficulty for casual gamers.
	 */
	MEDIUM("Medium"),
	
	/**
	 * Hard difficulty for experts of the game.
	 */
	HARD("Medium Rare"),
	
	/**
	 * Hardest difficulty for those looking for an extra challenge.
	 */
	HARDEST("Rare");
	
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
	 * The display name of the difficulty.
	 */
	private final String displayName;
	
	/**
	 * Creates a new difficulty enum entry with the given display name.
	 * @param displayName the display name of the primary stat, not <code>null</code>
	 * @throws NullPointerException If <code>displayName</code> refers to <code>null</code>.
	 */
	private Difficulty(String displayName) {
		Utilities.requireNonNull(displayName, "displayName");
		this.displayName = displayName;
	}
	
	/**
	 * Returns the display name of this difficulty. 
	 * @return the display name of this difficulty, guaranteed to be non <code>null</code>
	 */
	@Override
	public String getName() {
		return displayName;
	}
	
	/**
	 * If challenge ratings should be shown on the stage frame, depending on the game difficulty.
	 * @return <code>true</code> if challenge ratings should be displayed on the stage selection frame for this difficulty, else <code>false</code>
	 */
	public final boolean showChallengeRatings() {
		return this.compareTo(MEDIUM) <= 0;
	}
	
	/**
	 * If the stats of a character should be shown on the battle screen, depending on the game difficulty.
	 * @return <code>true</code> if the stats of a character should be displayed on the battle screen for this difficulty, else <code>false</code>
	 */
	public final boolean showCharacterStats() {
		return this.compareTo(MEDIUM) <= 0;
	}
	
	/**
	 * If the health of a character should be shown on the battle screen, depending on the game difficulty.
	 * @return <code>true</code> if the health of a character should be displayed on the battle screen for this difficulty, else <code>false</code>
	 */
	public final boolean showCharacterHealth() {
		return this.compareTo(HARDEST) < 0;
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
	

	public static final int getChallengeRatingCap(int rating) {
		return (int)Math.floor(rating*STEAMROLL_FACTOR);
	}
	
	/**
	 * If the player should be stopped if their team is too strong for an encounter, depending on the game difficulty.
	 * @return <code>true</code> if the player should be stopped with this difficulty 
	 * if their team is too strong for an encounter, else <code>false</code>
	 * @see #STEAMROLL_FACTOR
	 */
	public final boolean stopSteamrolling() {
		return this.compareTo(EASIEST) > 0;
	}

}
