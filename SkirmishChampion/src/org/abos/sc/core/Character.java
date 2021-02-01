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
	 * Returns the primary stat of this character to attack with.
	 * @return the primary stat of this character to attack with
	 * @see #getPreferredAttackStat()
	 */
	public StatsPrimary getAttackStat() {
		return getPreferredAttackStat();
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
	 * Returns the character's secondary stat to damage.
	 * @return the character's secondary stat to damage
	 * @see #getPreferredDamageStat()
	 */
	public StatsSecondary getDamageStat() {
		return getPreferredDamageStat();
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
	 * Returns a deep copy of this character by calling the copy constructor.
	 * @see #Character(Character)
	 */
	@Override
	public Object clone() {
		return new Character(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(damages);
		result = prime * result + (defeated ? 1231 : 1237);
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
		Character other = (Character) obj;
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
	 * Returns the name of this character for displaying purposes. This is identical to calling {@link #getName()}.
	 * @return the name of this character
	 */
	@Override
	public String toString() {
		return getName();
	}
	
}
