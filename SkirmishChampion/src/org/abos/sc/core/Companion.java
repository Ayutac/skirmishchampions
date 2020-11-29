package org.abos.sc.core;

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
	public Companion(CharacterBase base, int level) {
		super(base);
		this.level = level;
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
	public Object clone() throws CloneNotSupportedException {
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
	
	public static Companion parse(String s, Player player) {
		Utilities.requireNonNull(s, "s");
		String[] params = s.split(",");
		final int PARAM_NUMBER = 2;
		if (params.length != PARAM_NUMBER)
			throw new IllegalNumberOfArgumentsException(String.format("Companion has %d parameters instead of %d!", params.length, PARAM_NUMBER));
		CharacterBase base = CharacterBase.CHARACTERS.lookup(params[0]);
		if (base == null)
			throw new IllegalArgumentRangeException(String.format("Unknown character ID %s!", params[0]));
		int level = 0;
		try {
			level = Integer.valueOf(params[1]);
		}
		catch (NumberFormatException ex) {
			throw new IllegalArgumentRangeException(String.format("Level of %s is invalid: %s!", params[0], params[1]), ex);
		}
		Companion companion = new Companion(base,level);
		if (player != null)
			player.getCompanions().add(companion);
		return companion;
	}
	
}
