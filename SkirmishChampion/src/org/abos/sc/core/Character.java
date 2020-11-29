package org.abos.sc.core;

import java.util.Arrays;

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
	 * 
	 * @see #getDamage(StatsSecondary)
	 */
	protected int[] damages;
	
	/**
	 * 
	 * @param base
	 * @throws NullPointerException If <code>base</code> refers to <code>null</code>.
	 */
	public Character(CharacterBase base) {
		super(base);
		damages = new int[StatsSecondary.SIZE]; // initializes with 0
	}
	
	/**
	 * Copy constructor
	 * @param c
	 * @throws NullPointerException If <code>c</code> refers to <code>null</code>.
	 */
	public Character(Character c) {
		super(c);
		assert c.damages.length == StatsSecondary.SIZE;
		damages = Arrays.copyOf(c.damages, c.damages.length);
	}
	
	public int getDamage(StatsSecondary type) {
		return damages[type.ordinal()];
	}
	
	public synchronized void dealDamage(int damage, StatsSecondary type) {
		this.damages[type.ordinal()] += damage; 
	}
	
	public int getCurrentHealth(StatsSecondary type) {
		return Math.max(0, getSecondaryStat(type) - damages[type.ordinal()]);
	}
	
	public StatsPrimary getAttackStat() {
		return getPreferredAttackStat();
	}
	
	@Override
	public int getAttackPower() {
		return getAttackPower(getAttackStat());
	}
	
	public StatsSecondary getDamageStat() {
		return getPreferredDamageStat();
	}
	
	public boolean isDefeated() {
		for (int i = 0; i < StatsSecondary.SIZE; i++)
			if (getSecondaryStat(SECONDARY_STATS[i]) <= damages[i])
				return true;
		return false;
	}
	
	public synchronized void restore() {
		for (int i = 0; i < damages.length; i++)
			damages[i] = 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(damages);
		return result;
	}
	
	/**
	 * Returns a deep copy of this character by calling the copy constructor.
	 * @see #Character(Character)
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Character(this);
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
		return true;
	}
	
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
	
	public String toBattleStatString() {
		return getName()+" "+healthToString();
	}

	@Override
	public String toString() {
		return getName();
	}
	
}
