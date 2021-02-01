package org.abos.sc.core;

import java.time.Duration;

import org.abos.util.IllegalArgumentRangeException;
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
	 * The companion's level.
	 * @see #getLevel()
	 */
	protected int level;
	
	/**
	 * 
	 * @param base
	 * @param level
	 * @throws NullPointerException If <code>base</code> refers to <code>null</code>.
	 */
	public Companion(CharacterBase base, int level, boolean healUp) {
		super(base);
		this.level = level;
		if (healUp)
			restore();
	}
	
	/**
	 * 
	 * @param base
	 * @param level
	 * @throws NullPointerException If <code>base</code> refers to <code>null</code>.
	 */
	public Companion(CharacterBase base, int level) {
		this(base, level, true);
	}
	
	/**
	 * 
	 * @param base
	 * @throws NullPointerException If <code>base</code> refers to <code>null</code>.
	 */
	public Companion(CharacterBase base) {
		this(base,1);
	}
	
	public int getLevel() {
		return level;
	}
	
	/**
	 * Increases the level of this character by 1.
	 */
	public void increaseLevel() {
		level++;
	}
	
	@Override
	public Object clone() {
		return new Companion(this, this.level);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + level;
		return result;
	}

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
		return true;
	}
	
	@Override
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		s.append(id);
		s.append(',');
		s.append(level);
	}
	
	/**
	 * {@inheritDoc} Level included.
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 */
	@Override
	protected void innerHintString(StringBuilder s) {
		super.innerHintString(s);
		s.append("<br>");
		s.append("Level: ");
		s.append(getLevel());
	}
	
	public static Companion parse(String s, Player player) {
		Utilities.requireNonNull(s, "s");
		String[] params = s.split(",");
		final int PARAM_NUMBER = 2;
		if (params.length != PARAM_NUMBER)
			throw new IllegalNumberOfArgumentsException(String.format("Companion has %d parameters instead of %d!", params.length, PARAM_NUMBER));
		CharacterBase base = CharacterBase.CHARACTERS.lookup(params[0]);
		if (base == null)
			throw new ParsedIdNotFoundException(String.format("Unknown character ID %s!", params[0]));
		int level = 0;
		try {
			level = Integer.valueOf(params[1]); // throws NFE
		}
		catch (NumberFormatException ex) {
			throw new IllegalArgumentRangeException(String.format("Level of %s is invalid: %s!", params[0], params[1]), ex);
		}
		Companion companion = new Companion(base,level);
		if (player != null) {
			if (player.getCompanions().containsId(companion.getId()))
				throw new ParsedIdFoundException("Companion "+companion.getId()+" already registered with this player!");
			player.getCompanions().add(companion);
		}
		return companion;
	}
	
}
