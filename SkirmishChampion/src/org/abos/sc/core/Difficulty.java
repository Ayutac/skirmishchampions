package org.abos.sc.core;

import org.abos.util.IllegalArgumentRangeException;
import org.abos.util.Name;
import org.abos.util.SaveString;
import org.abos.util.Utilities;

/**
 * An enumeration of the different difficulties complete with methods containing what they
 * entail.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
public enum Difficulty implements Name, SaveString {
	
	/**
	 * Easiest difficulty, if you need your hand hold.
	 */
	EASIEST("Well Done", Double.POSITIVE_INFINITY),
	
	/**
	 * Easy difficulty, great for beginners.
	 */
	EASY("Medium Well", 2.0),
	
	/**
	 * Medium difficulty for casual gamers.
	 */
	MEDIUM("Medium", 1.5),
	
	/**
	 * Hard difficulty for experts of the game.
	 */
	HARD("Medium Rare", 1.4),
	
	/**
	 * Hardest difficulty for those looking for an extra challenge.
	 */
	HARDEST("Rare", 1.4),
	
	/**
	 * Lunatic mode, not made to be beat.
	 */
	IMPOSSIBLE("A living cow", 1.0);
	
	/**
	 * The default difficulty.
	 */
	public static final Difficulty DEFAULT = MEDIUM;
	
	/**
	 * Returns the difficulty of the player if one is provided or the default difficulty if not.
	 * This method should be used where a player is optional but a difficulty must be provided.
	 * @param player the player to return the difficulty of, can be <code>null</code>
	 * @return the difficulty of the specified player or the default difficulty if <code>player</code> refers to <code>null</code>.
	 */
	public static final Difficulty of(Player player) {
		if (player == null)
			return DEFAULT;
		return player.getDifficulty();
	}
	
	/**
	 * The display name of the difficulty.
	 */
	private final String displayName;
	
	/**
	 * If stopping the player for a strong team is activated, they should be stopped 
	 * if the ratio between the player's team and the enemy encounter is greater or
	 * equal to this amount.
	 * @see #stopSteamrolling()
	 * @see #getChallengeRatingCap(int)
	 */
	private final double steamrollFactor;
	
	/**
	 * Creates a new difficulty enum entry with the given display name.
	 * @param displayName the display name of the primary stat, not <code>null</code>
	 * @param steamrollFactor the steamroll factor for this difficulty, must be positive
	 * @throws NullPointerException If <code>displayName</code> refers to <code>null</code>.
	 * @throws IllegalArgumentException If <code>steamrollFactor</code> is 0, Not a Number or negative.
	 */
	private Difficulty(String displayName, double steamrollFactor) {
		Utilities.requireNonNull(displayName, "displayName");
		if (Double.isNaN(steamrollFactor) || steamrollFactor <= 0)
			throw new IllegalArgumentException("steamrollFactor must be positive!");
		this.displayName = displayName;
		this.steamrollFactor = steamrollFactor;
	}
	
	/**
	 * Returns the display name of this difficulty. 
	 * @return the display name of this difficulty, guaranteed to be non <code>null</code>
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
	 * If the player should be stopped if their team is too strong for an encounter, depending on the game difficulty.
	 * @return <code>true</code> if the player should be stopped with this difficulty 
	 * if their team is too strong for an encounter, else <code>false</code>
	 */
	public final boolean stopSteamrolling() {
		return Double.POSITIVE_INFINITY != steamrollFactor;
	}
	
	/**
	 * Returns the specified rating times the steamroll factor to obtain the upper bound for the challenge rating.
	 * If the product exceeds {@link Integer} bounds the appropiate bound is returned instead.
	 * @param rating the rating to multiply with the steamroll factor
	 * @return the product of <code>rating</code> times the steamroll factor, capped within {@link Integer} bounds
	 * @see Integer#MAX_VALUE
	 * @see Integer#MIN_VALUE 
	 */
	public final int getChallengeRatingCap(int rating) {
		double cap = Math.floor(rating*steamrollFactor);
		if (cap >= Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		if (cap <= Integer.MIN_VALUE)
			return Integer.MIN_VALUE;
		return (int)cap;
	}
	
	/**
	 * Saves the difficulty to a string builder.
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString()
	 * @see #parse(String)
	 */
	@Override
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		s.append(name());
	}
	
	/**
	 * Parses a string to a difficulty. Allowed strings are the enum name of the difficulties,
	 * their capitalized version, their display name or their index. Anything else will throw an exception. 
	 * @param s the string to parse
	 * @return the string as a rarity
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalArgumentRangeException If the string couldn't be parsed.
	 */
	public static Difficulty parse(String s) {
		Utilities.requireNonNull(s, "s");
		for (Difficulty difficulty : values()) 
			if (s.equals(difficulty.name()) || s.equals(difficulty.getCapitalizedName()) || s.equals(difficulty.getName()))
				return difficulty;
		try {
			return values()[Integer.parseInt(s)];
		}
		catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentRangeException(ex);
		}
	}

}
