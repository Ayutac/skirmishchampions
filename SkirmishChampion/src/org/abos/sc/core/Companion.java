package org.abos.sc.core;

import java.time.Duration;

import org.abos.util.IllegalArgumentTypeException;
import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.ParsedIdFoundException;
import org.abos.util.ParsedIdNotFoundException;
import org.abos.util.Utilities;

/**
 * This subclass of {@link Character} saves additional information of a character that is only relevant as
 * a collectable for the player, e.g. their level.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see CharacterBase
 * @see Character
 */
public class Companion extends Character {
	
	/**
	 * How long level ups take. <code>LEVEL_UP_DURATION[level]</code> returns the time needed for a companion 
	 * to go from <code>level</code> to level <code>level+1</code>. Since companions usually start at level 1,
	 * <code>LEVEL_UP_DURATION[0]</code> is set to {@link Duration#ZERO}.
	 */
	public static final Duration[] LEVEL_UP_DURATION = new Duration[]
			{Duration.ZERO, Duration.ofHours(1L), Duration.ofDays(1L), Duration.ofDays(3L), Duration.ofDays(7L)};
	
	/**
	 * The level of this companion.
	 * @see #getLevel()
	 */
	protected int level;
	
	/**
	 * The extra points of this companion.
	 * @see #getExtraPoints
	 */
	protected int extraPoints;
	
	/**
	 * Creates a new companion from a specified character base with the given level and heals the companion
	 * if <code>healUp</code> was set to <code>true</code>. 
	 * @param base the base for this companion
	 * @param level the level of this companion
	 * @param extraPoints the extra points of this companion
	 * @param healUp If set to <code>true</code>, {@link #restore()} will be called on this companion. Will only have an effect if <code>base</code> was a damaged character or subclass thereof.
	 * @throws NullPointerException If <code>base</code> refers to <code>null</code>.
	 */
	public Companion(CharacterBase base, int level, int extraPoints, boolean healUp) {
		super(base);
		this.level = level;
		this.extraPoints = extraPoints;
		if (healUp)
			restore();
	}
	
	/**
	 * Creates a new companion from a specified character base with the given level and heals the companion if needed.
	 * @param base the base for this companion
	 * @param level the level of this companion.
	 * @param extraPoints the extra points of this companion
	 * @throws NullPointerException If <code>base</code> refers to <code>null</code>.
	 */
	public Companion(CharacterBase base, int level, int extraPoints) {
		this(base, level, extraPoints, true);
	}
	
	/**
	 * Creates a new companion from a specified character base at level 1 and heals the companion if needed.
	 * @param base the base for this companion
	 * @throws NullPointerException If <code>base</code> refers to <code>null</code>.
	 */
	public Companion(CharacterBase base) {
		this(base, 1, 0);
	}
	
	/**
	 * Copy constructor.
	 * @param companion the companion to copy
	 * @throws NullPointerException If <code>companion</code> refers to <code>null</code>.
	 */
	public Companion(Companion companion) {
		this(companion, companion.level, companion.extraPoints, false);
	}
	
	/**
	 * Returns the level of this companion.
	 * @return the level of this companion
	 * @see #increaseLevel()
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Increases the level of this character by 1.
	 * @see #getLevel()
	 */
	public void increaseLevel() {
		level++;
	}
	
	/**
	 * Returns the extra points of this companion.
	 * @return the extra points of this companion
	 * @see #addExtraPoints(int)
	 */
	public int getExtraPoints() {
		return extraPoints;
	}
	
	/**
	 * Adds extra points to this companion.
	 * @param amount the amount of extra points to add
	 * @see #getExtraPoints()
	 */
	public void addExtraPoints(int amount) {
		extraPoints += amount;
	}
	
	/**
	 * Returns a deep copy of this companion by calling the copy constructor.
	 * @see #Companion(Companion)
	 */
	@Override
	public Object clone() {
		return new Companion(this);
	}

	/**
	 * Generates a hash code for this instance, taking into account all fields. It's guarantied that
	 * the hash codes of two companions are equal if the companions are equal. This message
	 * calls {@link Character#hashCode()}.
	 * @return a hash code for this companion
	 * @see #equals(Object)
	 * @see Character#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + level;
		result = prime * result + extraPoints;
		return result;
	}

	/**
	 * Checks if the specified object is the same as this companion, i.e. the other object
	 * must be or inherit from {@link Companion}, be the same class as the object this method is called from
	 * and all its <code>Companion</code> fields must be the same as the fields of this companion
	 * to return <code>true</code>. This method calls {@link Character#equals(Object)}.
	 * If the other object is equal to this character, then their hash codes return the same number, if called on <code>Character</code>.
	 * @param obj the object to check
	 * @return <code>true</code> if this companion is equal to the object, else <code>false</code>.
	 * @see #hashCode()
	 * @see Character#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Companion other = (Companion) obj;
		if (level != other.level)
			return false;
		if (extraPoints != other.extraPoints)
			return false;
		return true;
	}
	
	/**
	 * Saves the companion to a string builder. Note that this method does <b>not</b> call
	 * {@link CharacterBase#toSaveString(StringBuilder)}, for the character base's properties
	 * should be loaded before and {@link Companion#parse(String, Player)} calls on that load.
	 * So this method only saves some additional fields important for the player's progress. 
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString()
	 * @see #parse(String, Player)
	 */
	@Override
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		// if changed, also change the parse function and the documentation of it
		s.append(id);
		s.append(',');
		s.append(level);
		s.append(',');
		s.append(extraPoints);
	}
	
	/**
	 * Returns a string representation of this companion. Note that this method does <b>not</b> call
	 * {@link CharacterBase#toSaveString(StringBuilder)}, for the character base's properties
	 * should be loaded before and {@link Companion#parse(String, Player)} calls on that load.
	 * So this method only saves some additional fields important for the player's progress. 
	 * @returns a string representation of this companion 
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString(StringBuilder)
	 * @see #parse(String, Player)
	 */
	// this override is only done to correct the JavaDoc, since the functionality is changed fundamentally
	@Override
	public String toSaveString() {
		return super.toSaveString(); // which in turn calls this class's toSaveString(StringBuilder)
	}
	
	/**
	 * {@inheritDoc} Level included, but not extra points.
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 */
	@Override
	protected void innerHintString(StringBuilder s) {
		super.innerHintString(s);
		s.append("<br>");
		s.append("Level: ");
		s.append(getLevel());
	}
	
	/**
	 * Parses a string representation of a companion into a object.
	 * The format is "<code>ID,level,extraPoints</code>".
	 * @param s the string to parse
	 * @param player the player this companion should be added to, if any.
	 * @return a companion matching the string
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalNumberOfArgumentsException If the number of arguments in the string separated by <code>,</code> is wrong.
	 * @throws ParsedIdNotFoundException If the parsed ID cannot be found in {@link CharacterBase#CHARACTERS}.
	 * @throws IllegalArgumentTypeException If the level or extra points is not a number.
	 * @throws ParsedIdFoundException If <code>player</code> is not <code>null</code> and the string is parsed successfully, but the ID is already registered in {@link Player#getCompanions()}.
	 * @see #CharacterBase(String, String, String, String[], int[], StatsPrimary, StatsSecondary, boolean)
	 * @see #toSaveString()
	 */
	public static Companion parse(String s, Player player) {
		Utilities.requireNonNull(s, "s");
		String[] params = s.split(",");
		final int PARAM_NUMBER = 3;
		// compatibility for version 0.6
		if (params.length != PARAM_NUMBER && params.length + 1 != PARAM_NUMBER)
			throw new IllegalNumberOfArgumentsException(String.format("Companion has %d parameters instead of %d!", params.length, PARAM_NUMBER));
		CharacterBase base = CharacterBase.CHARACTERS.lookup(params[0]);
		if (base == null)
			throw new ParsedIdNotFoundException(String.format("Unknown character ID %s!", params[0]));
		int level = 0;
		try {
			level = Integer.valueOf(params[1]); // throws NFE
		}
		catch (NumberFormatException ex) {
			throw new IllegalArgumentTypeException(String.format("Level of %s is invalid: %s!", params[0], params[1]), ex);
		}
		int xp = 0;
		// compatibility for version 0.6
		if (params.length == PARAM_NUMBER)
			try {
				xp = Integer.valueOf(params[2]); // throws NFE
			}
			catch (NumberFormatException ex) {
				throw new IllegalArgumentTypeException(String.format("Extra points of %s are invalid: %s!", params[0], params[2]), ex);
			}
		Companion companion = new Companion(base, level, xp);
		if (player != null) {
			if (player.getCompanions().containsId(companion.getId()))
				throw new ParsedIdFoundException("Companion "+companion.getId()+" already registered with this player!");
			player.getCompanions().add(companion);
		}
		return companion;
	}
	
}
