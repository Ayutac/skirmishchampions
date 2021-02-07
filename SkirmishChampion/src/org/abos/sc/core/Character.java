package org.abos.sc.core;

import java.util.Arrays;

import org.abos.util.Utilities;

/**
 * This subclass of {@link CharacterBase} saves additional information of a character that can change, e.g. the damage they have taken.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see CharacterBase
 * @see Companion
 */
public class Character extends CharacterBase {
	
	/**
	 * Stores the damages done to this character.
	 * @see #getDamage(StatsSecondary)
	 */
	protected int[] damages;
	
	/**
	 * A flag to remember if the character had been defeated.
	 * @see #isDefeated()
	 */
	protected boolean defeated = false;
	
	/**
	 * Store the challenge rating internally.
	 * @see #calculateChallengeRating()
	 * @see #getChallengeRating()
	 */
	protected Integer challengeRating = null;
	
	/**
	 * Creates a new character instance from a given base.
	 * @param base the base of this character
	 * @throws NullPointerException If <code>base</code> refers to <code>null</code>.
	 */
	public Character(CharacterBase base) {
		super(base);
		damages = new int[StatsSecondary.SIZE]; // initializes with 0
	}
	
	/**
	 * Copy constructor.
	 * @param c the character to copy
	 * @throws NullPointerException If <code>c</code> refers to <code>null</code>.
	 */
	public Character(Character c) {
		super(c);
		assert c.damages.length == StatsSecondary.SIZE;
		damages = Arrays.copyOf(c.damages, c.damages.length);
		defeated = c.defeated;
	}
	
	/**
	 * Returns the damage of this character in the specified secondary stat.
	 * @param type the secondary stat of the character to look up the damage for
	 * @return the damage in the specified secondary stat
	 * @throws NullPointerException If <code>type</code> refers to <code>null</code>.
	 * @see #dealDamage(int, StatsSecondary)
	 * @see #getCurrentHealth(StatsSecondary)
	 * @see #restore()
	 */
	public int getDamage(StatsSecondary type) {
		Utilities.requireNonNull(type, "type");
		return damages[type.ordinal()];
	}
	
	/**
	 * Deals the specified amount of damage to this character.
	 * @param damage The amount of damage to inflict to this character. Is allowed to be negative, which will heal the character.
	 * @param type the secondary stat of the character to damage 
	 * @throws NullPointerException If <code>type</code> refers to <code>null</code>.
	 * @see #getDamage(StatsSecondary)
	 */
	public synchronized void dealDamage(int damage, StatsSecondary type) {
		Utilities.requireNonNull(type, "type");
		this.damages[type.ordinal()] += damage; 
	}
	
	/**
	 * Returns the current health of this character in the specified secondary stat.
	 * @param type the secondary stat of the character to look up the health for
	 * @return the health in the specified secondary stat
	 * @throws NullPointerException If <code>type</code> refers to <code>null</code>.
	 * @see #getDamage(StatsSecondary)
	 * @see #restore()
	 */
	public int getCurrentHealth(StatsSecondary type) {
		Utilities.requireNonNull(type, "type");
		return Math.max(0, getSecondaryStat(type) - damages[type.ordinal()]);
	}
	
	/**
	 * Returns the current attack power of the attack stat.
	 * @return the current attack power of the attack stat
	 * @see #getAttackStat()
	 * @see #getAttackPower(StatsPrimary)
	 */
	@Override
	public int getAttackPower() {
		return getAttackPower(getAttackStat());
	}
	
	/**
	 * Returns <code>true</code> if the defeated flag of this character is set to <code>true</code> or
	 * if the damage in any stat exceeds the respective stat. In the latter case, the defeated flag
	 * is set to <code>true</code>.
	 * @return <code>true</code> if the character is defeated, else <code>false</code>
	 * @see #getDamage(StatsSecondary)
	 * @see #restore()
	 */
	public boolean isDefeated() {
		if (defeated)
			return true;
		for (int i = 0; i < StatsSecondary.SIZE; i++)
			if (getSecondaryStat(SECONDARY_STATS[i]) <= damages[i]) {
				defeated = true;
				return true;
			}
		return false;
	}
	
	/**
	 * Sets the damages of this character to 0 and sets the defeated flag to <code>false</code>.
	 * @see #getDamage(StatsSecondary)
	 * @see #isDefeated()
	 */
	public synchronized void restore() {
		for (int i = 0; i < damages.length; i++)
			damages[i] = 0;
		defeated = false;
	}
	
	/**
	 * Returns the challenge rating of this character.
	 * @return the challenge rating of this character
	 */
	@Override
	public int getChallengeRating() {
		if (challengeRating == null) 
			challengeRating = calculateChallengeRating();
		return challengeRating;
	}
	
	/**
	 * Returns a deep copy of this character by calling the copy constructor.
	 * @see #Character(Character)
	 */
	@Override
	public Object clone() {
		return new Character(this);
	}
	
	/**
	 * Generates a hash code for this instance, taking into account all fields. It's guarantied that
	 * the hash codes of two character are equal if the characters are equal. This message
	 * calls {@link CharacterBase#hashCode()}.
	 * @return a hash code for this character
	 * @see #equals(Object)
	 * @see CharacterBase#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(damages);
		result = prime * result + (defeated ? 1231 : 1237);
		return result;
	}

	/**
	 * Checks if the specified object is the same as this character, i.e. the other object
	 * must be or inherit from {@link Character}, be the same class as the object this method is called from
	 * and all its <code>Character</code> fields must be the same as the fields of this character
	 * to return <code>true</code>. The underlying array of damages is compared by values, not reference, as well.
	 * This method calls {@link CharacterBase#equals(Object)}.
	 * If the other object is equal to this character, then their hash codes return the same number, if called on <code>Character</code>.
	 * @param obj the object to check
	 * @return <code>true</code> if this character is equal to the object, else <code>false</code>.
	 * @see #hashCode()
	 * @see CharacterBase#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Character other = (Character) obj;
		// note that the challengeRating is just saved for memory purposes and would get 
		// usually calculated, so it is not part of the comparison
		if (!Arrays.equals(damages, other.damages))
			return false;
		if (defeated != other.defeated)
			return false;
		return true;
	}

	/**
	 * Puts this character's health into a string of the form "<code>[CONS/MENT/ELOQ]</code>" with
	 * the corresponding current health instead of the stat names, for example "<code>[50/32/27]</code>".
	 * @return this character's health as a string
	 * @see #getCurrentHealth(StatsSecondary)
	 */
	public String healthToString() {
		StringBuilder s = new StringBuilder();
		s.append('[');
		for (int i = 0; i+1 < StatsSecondary.SIZE; i++) {
			s.append(getCurrentHealth(SECONDARY_STATS[i]));
			s.append('/');
		}
		s.append(getCurrentHealth(SECONDARY_STATS[StatsSecondary.SIZE-1]));
		s.append(']');
		return s.toString();
	}
	
	/**
	 * Returns this character's name together with their health string, separated by a space, for example
	 * "<code>John Doe [50/32/27]</code>".
	 * @return this character's name and health
	 * @see #getName()
	 * @see #healthToString()
	 */
	public String toBattleStatString() {
		return getName()+" "+healthToString();
	}
	
	/**
	 * Writes the text within the HTML tags of {@link #toHintString()} in the provided string builder.
	 * @param s the string builder to append the inner html to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 */
	protected void innerHintString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		for (StatsPrimary ps : PRIMARY_STATS) {
			if (ps == getAttackStat())
				s.append("<u>");
			s.append(ps.getName());
			s.append(": ");
			s.append(getPrimaryStat(ps));
			if (ps == getAttackStat())
				s.append("</u>");
			s.append("<br>");
		}
		for (StatsSecondary ss : SECONDARY_STATS) {
			if (ss == getDamageStat())
				s.append("<u>");
			s.append(ss.getName());
			s.append(": ");
			s.append(getSecondaryStat(ss));
			if (ss == getDamageStat())
				s.append("</u>");
			s.append("<br>");
		}
		s.append("CR: ");
		s.append(getChallengeRating());
	}
	
	/**
	 * Creates a string listening some of this character's properties one under the other,
	 * @return a string with this characters's properties in HTML
	 * @see #innerHintString(StringBuilder)
	 */
	public String toHintString() {
		StringBuilder s = new StringBuilder("<html>");
		innerHintString(s);
		s.append("</html>");
		return s.toString();
	}

	/**
	 * Returns the name of this character for displaying purposes. This is identical to calling {@link #getName()}.
	 * @return the name of this character
	 */
	@Override
	public String toString() {
		return getName();
	}
	
}
