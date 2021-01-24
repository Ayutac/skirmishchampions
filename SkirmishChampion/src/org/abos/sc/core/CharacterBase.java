package org.abos.sc.core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BinaryOperator;

import org.abos.util.Id;
import org.abos.util.IllegalArgumentRangeException;
import org.abos.util.IllegalArgumentTypeException;
import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.Name;
import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * Contains the basic information for characters. The information provided by this class is not supposed to change.
 * For information that can change, use {@link Character} or a subclass thereof.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see Character
 * @see Companion
 */
public class CharacterBase implements Cloneable, Id, Name {
	
	/**
	 * An array of the primary stats, should equal {@link StatsPrimary}<code>.values()</code>. Saved here to reduce overhead.
	 * @see StatsPrimary
	 */
	protected static final StatsPrimary[] PRIMARY_STATS = StatsPrimary.values();
	
	/**
	 * An array of the secondary stats, should equal {@link StatsSecondary}<code>.values()</code>. Saved here to reduce overhead.
	 * @see StatsSecondary
	 */
	protected static final StatsSecondary[] SECONDARY_STATS = StatsSecondary.values();
	
	/**
	 * Number of parameters per character line, for parsing purposes. Primary stats are counted as one.
	 * @see #parse(String)
	 */
	public static final int PARSE_PARAM_NUM = 6 + 1; 

	/**
	 * the separator char between primary stats for saving/parsing
	 * @see #toSaveString()
	 * @see #parse(String, boolean)
	 */
	public static final char PRIMARY_SEPARATOR = ',';
	
	/**
	 * the separator char between different affiliations for saving/parsing
	 * @see #toSaveString()
	 * @see #parse(String, boolean)
	 */
	public static final char AFFILIATION_SEPARATOR = ',';
	
	/**
	 * a registry for character bases
	 * @see #parse(String)
	 * @see #loadCharactersFromFile(Path)
	 */
	public static final Registry<CharacterBase> CHARACTERS = new Registry<>();
	
	/**
	 * The ID of the character in the game. Should be unique.
	 * @see #getId()
	 */
	protected final String id;
	
	/**
	 * The name of the character.
	 * @see #getName()
	 */
	protected final String name;
	
	/**
	 * The character's associated fandom.
	 * @see #getFandomId()
	 */
	protected final String fandomId;
	
	/**
	 * The character's affiliations.
	 * @see #getAffiliations()
	 * @see #getAffiliationString()
	 */
	protected final String[] affiliations;
	
	/**
	 * The primary stats of the character.
	 * @see #getPrimaryStat(StatsPrimary)
	 */
	protected final int[] primaryStats;
	
	/**
	 * The primary stat of the character preferred for attack damage calculation.
	 * @see #getPreferredAttackStat()
	 * @see #getAttackPower()
	 */
	protected final StatsPrimary preferredAttackStat;
	
	/**
	 * The secondary stat the character prefers to damage.
	 * @see #getSecondaryStat(StatsSecondary)
	 */
	protected final StatsSecondary preferredDamageStat;

	/**
	 * 
	 * @param id
	 * @param name
	 * @param fandomId
	 * @param affiliations
	 * @param primaryStats
	 * @param preferredAttackStat
	 * @param preferredDamageStat
	 * @param register
	 * @throws NullPointerException If any argument except <code>affiliations</code> refers to <code>null</code>.
	 * @throws IllegalArgumentException If the length of <code>primaryStats</code> doesn't match the number
	 * of the different primary stats, more specifically {@link StatsPrimary#SIZE}.
	 */
	public CharacterBase(String id, String name, String fandomId, String[] affiliations, int[] primaryStats,
			StatsPrimary preferredAttackStat, StatsSecondary preferredDamageStat, boolean register) {
		Utilities.requireNonNull(id, "id");
		Utilities.requireNonNull(name, "name");
		Utilities.requireNonNull(fandomId, "fandomId");
		Utilities.requireNonNull(primaryStats, "primaryStats");
		Utilities.requireNonNull(preferredAttackStat, "preferredAttackStat");
		Utilities.requireNonNull(preferredDamageStat, "preferredDamageStat");
		if (primaryStats.length != StatsPrimary.SIZE)
			throw new IllegalArgumentException("primaryStats has wrong number of arguments!");
		
		this.id = id;
		this.name = name;
		this.fandomId = fandomId;
		if (affiliations == null)
			this.affiliations = new String[0];
		else
			this.affiliations = Arrays.copyOf(affiliations, affiliations.length);
		this.primaryStats = Arrays.copyOf(primaryStats, primaryStats.length);
		this.preferredAttackStat = preferredAttackStat;
		this.preferredDamageStat = preferredDamageStat;
		if (register) {
			CHARACTERS.add(this);
		}
	}
	
	/**
	 * Copy constructor.
	 * @param c the character base to copy
	 * @throws NullPointerException if <code>c</code> refers to <code>null</code>.
	 */
	public CharacterBase(CharacterBase c) { 
		this(c.id, c.name, c.fandomId, c.affiliations, c.primaryStats, c.preferredAttackStat, c.preferredDamageStat, false);
	}
	
	/**
	 * Returns the ID of the character.
	 * @return the ID of the character
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Returns the name of the character.
	 * @return the name of the character
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the fandom ID of the character.
	 * @return the fandom ID of the character
	 */
	public String getFandomId() {
		return fandomId;
	}

	/**
	 * Returns a copy of the affiliations of this character.
	 * @return the ID of the character base
	 */
	public String[] getAffiliations() {
		return Arrays.copyOf(affiliations, affiliations.length);
	}
	
	/**
	 * Returns the value of the specified primary stat.
	 * @param primaryStat the primary stat of which to return the value
	 * @return the value of the specified primary stat
	 * @throws NullPointerException If <code>primaryStat</code> refers to <code>null</code>.
	 */
	public int getPrimaryStat(StatsPrimary primaryStat) {
		Utilities.requireNonNull(primaryStat, "primaryStat");
		return primaryStats[primaryStat.ordinal()];
	}
	
	/**
	 * Returns the value of the specified primary stat.
	 * @param index the index of the primary stat of which to return the value
	 * @return the value of the specified primary stat
	 * @throws ArrayIndexOutOfBoundsException If <code>index < 0</code> or <code>index</code> is at least the number of primary stats.
	 */
	public int getPrimaryStat(int index) {
		return primaryStats[index]; // throws AIOOBE
	}

	/**
	 * Returns the value of the specified secondary stat.
	 * @param secondaryStat the secondary stat of which to return the value
	 * @return the value of the specified secondary stat
	 * @throws NullPointerException If <code>secondaryStat</code> refers to <code>null</code>.
	 */
	public int getSecondaryStat(StatsSecondary secondaryStat) {
		Utilities.requireNonNull(secondaryStat, "secondaryStat");
		int speed = getPrimaryStat(StatsPrimary.SPEED);
		switch(secondaryStat) {
		case CONSTITUTION:
			return getPrimaryStat(StatsPrimary.STRENGTH)+getPrimaryStat(StatsPrimary.DEXTERITY)+speed;
		case MENTAL:
			return getPrimaryStat(StatsPrimary.INTELLIGENCE)+getPrimaryStat(StatsPrimary.WISDOM)+speed;
		case ELOQUENCE:
			return getPrimaryStat(StatsPrimary.ROMANCE)+getPrimaryStat(StatsPrimary.CHARISMA)+speed;
		default: // shouldn't happen
			assert false;
			throw new IllegalArgumentException("Unsupported secondary stat!");
		}
	}
	
	/**
	 * Returns the value of the specified secondary stat.
	 * @param index the index of the secondary stat of which to return the value
	 * @return the value of the specified secondary stat
	 * @throws ArrayIndexOutOfBoundsException If <code>index < 0</code> or <code>index</code> is at least the number of secondary stats.
	 */
	public int getSecondaryStat(int index) {
		return getSecondaryStat(SECONDARY_STATS[index]);
	}

	/**
	 * Returns the character's preferred primary stat to attack with.
	 * @return the character's preferred primary stat to attack with
	 * @see #getAttackPower()
	 */
	public StatsPrimary getPreferredAttackStat() {
		return preferredAttackStat;
	}

	/**
	 * Returns the character's preferred secondary stat to damage.
	 * @return the character's preferred secondary stat to damage
	 */
	public StatsSecondary getPreferredDamageStat() {
		return preferredDamageStat;
	}
	
	/**
	 * Returns the current attack power of the specified primary stat.
	 * @param type the primary stat to return the attack power of
	 * @return the current attack power of the specified primary stat
	 * @see #getPrimaryStat(StatsPrimary)
	 * @see #getAttackPower()
	 */
	public int getAttackPower(StatsPrimary type) {
		return getPrimaryStat(type) / 10;
	}
	
	/**
	 * Returns the current attack power of the preferred primary stat.
	 * @return the current attack power of the preferred primary stat
	 * @see #getPreferredAttackStat()
	 * @see #getAttackPower(StatsPrimary)
	 */
	public int getAttackPower() {
		return getAttackPower(preferredAttackStat);
	}
	
	
	/**
	 * Returns the current attack speed of the character.
	 * @return the current attack speed of the character
	 * @see #getPrimaryStat(StatsPrimary)
	 * @see #getAttackPower()
	 */
	public long getAttackSpeed() {
		// 50 seconds in milliseconds divided by speed as a multiple of 10ms
		return 10L*Math.round(5000d / getPrimaryStat(StatsPrimary.SPEED));
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new CharacterBase(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(affiliations);
		result = prime * result + ((preferredAttackStat == null) ? 0 : preferredAttackStat.hashCode());
		result = prime * result + ((preferredDamageStat == null) ? 0 : preferredDamageStat.hashCode());
		result = prime * result + ((fandomId == null) ? 0 : fandomId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(primaryStats);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharacterBase other = (CharacterBase) obj;
		if (!Arrays.equals(affiliations, other.affiliations))
			return false;
		if (preferredAttackStat != other.preferredAttackStat)
			return false;
		if (preferredDamageStat != other.preferredDamageStat)
			return false;
		if (fandomId == null) {
			if (other.fandomId != null)
				return false;
		} else if (!fandomId.equals(other.fandomId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(primaryStats, other.primaryStats))
			return false;
		return true;
	}
	
	/**
	 * Saves the character base to a string builder.
	 * @param s the string builder to append to
	 * @see #toSaveString()
	 */
	public void toSaveString(StringBuilder s) {
		s.append(id);
		s.append(';');
		s.append(name);
		s.append(';');
		s.append(fandomId);
		s.append(';');
		Utilities.arrayToString(affiliations, s, AFFILIATION_SEPARATOR);
		s.append(';');
		Utilities.arrayToString(primaryStats, s, PRIMARY_SEPARATOR);
		s.append(';');
		s.append(Integer.toString(preferredAttackStat.ordinal()));
		s.append(';');
		s.append(Integer.toString(preferredDamageStat.ordinal()));
	}
	
	/**
	 * Returns the character base as a string for saving purposes, i.e. in the form needed for {@link #parse(String)}.
	 * @return the character base as a string for saving purposes
	 */
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	/**
	 * Returns a string representation of the character base. This is identical to
	 * calling {@link #toSaveString()}.
	 * @return a string representation of the character base
	 */
	public String toString() {
		return toSaveString();
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static CharacterBase parse(String s, boolean register) {
		Utilities.requireNonNull(s, "s");
		String[] parts = s.split(";"); 
		if (parts.length != PARSE_PARAM_NUM)
			throw new IllegalNumberOfArgumentsException(String.format("Character \"%s\" to parse contained %d arguments instead of %d", s, parts.length, PARSE_PARAM_NUM));
		int[] primaryStats = Utilities.arrayToInt(parts[4].split(String.valueOf(PRIMARY_SEPARATOR)));
		if (primaryStats.length != PRIMARY_STATS.length)
			throw new IllegalNumberOfArgumentsException(String.format("Character \"%s\" to parse contained %d primary stats instead of %d", s, primaryStats.length, PRIMARY_STATS.length));
		StatsPrimary attackStat  = null;
		StatsSecondary damageStat = null;
		try {
			attackStat = PRIMARY_STATS[Integer.parseInt(parts[5])];
			damageStat = SECONDARY_STATS[Integer.parseInt(parts[6])];
		}
		catch (NumberFormatException ex) {
			throw new IllegalArgumentTypeException(ex);
		}
		catch (ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentRangeException(ex);
		}
		return new CharacterBase(parts[0], parts[1], parts[2], 
				parts[3].split(String.valueOf(AFFILIATION_SEPARATOR)), 
				primaryStats, attackStat, damageStat, register);
	}
	
	public static CharacterBase parse(String s) {
		return parse(s, true);
	}
	
	/**
	 * Iterates over a number of characters and applies a binary operation on all their stats seperately.
	 * @param chars The characters to interate over. <code>null</code> entries will be ignored.
	 * @param op the binary operation to apply
	 * @param startVals The start values for the binary operation. Length must match the corresponding number of stats. 
	 * If <code>chars</code> doesn't contain non <code>null</code> entries, a copy of this array will be returned.
	 * @param primary if <code>true</code> the operation will be done over the primary stats, else over the secondary stats 
	 * @return An integer array containing the end results of the operation. The order matches the order of the corresponding stats enum.
	 * @throws NullPointerException If <code>chars</code>, <code>op</code> or <code>startVals</code> refers to <code>null</code>.
	 * @throws IllegalArgumentException If <code>startsVals.length</code> doesn't match the corresponding number of stats.
	 * @see #getStatsMax(Iterable, boolean)
	 * @see #getStatsMin(Iterable, boolean)
	 * @see #getStatsAverage(Iterable, int, boolean)
	 */
	public static int[] getStatsEval(Iterable<? extends CharacterBase> chars, BinaryOperator<Integer> op, int[] startVals, boolean primary) {
		Utilities.requireNonNull(chars, "chars");
		Utilities.requireNonNull(op, "op");
		Utilities.requireNonNull(startVals, "startVals");
		if (primary) {
			if (startVals.length != StatsPrimary.SIZE)
				throw new IllegalArgumentException("startVals has wrong number of arguments, must match number of primary stats!");
		}
		else {
			if (startVals.length != StatsSecondary.SIZE)
				throw new IllegalArgumentException("startVals has wrong number of arguments, must match number of secondary stats!");
		}
		
		int[] result = Arrays.copyOf(startVals, startVals.length);
		for (CharacterBase base : chars) {
			if (base == null)
				continue;
			if (primary) {
				for (int i = 0; i < StatsPrimary.SIZE; i++)
					result[i] = op.apply(result[i], base.getPrimaryStat(i));
			}
			else {
				for (int i = 0; i < StatsSecondary.SIZE; i++)
					result[i] = op.apply(result[i], base.getSecondaryStat(i));
			}
		}
		return result;
	}
	
	/**
	 * Iterates over a number of characters and returns the maximum of all their stats seperately.
	 * @param chars The characters to interate over. <code>null</code> entries will be ignored.
	 * @param primary if <code>true</code> the operation will be done over the primary stats, else over the secondary stats 
	 * @return An integer array containing the maximum of all the characters' stats seperately. The order matches the order of the corresponding stats enum.
	 * If <code>chars</code> doesn't contain non <code>null</code> entries, an array full of {@link Integer#MIN_VALUE} will be returned.
	 * @throws NullPointerException If <code>chars</code> refers to <code>null</code>.
	 * @see #getStatsEval(Iterable, BinaryOperator, int[], boolean)
	 * @see #getStatsMin(Iterable, boolean)
	 * @see #getStatsAverage(Iterable, int, boolean)
	 */
	public static int[] getStatsMax(Iterable<? extends CharacterBase> chars, boolean primary) {
		int[] startVals = new int[primary ? StatsPrimary.SIZE : StatsSecondary.SIZE];
		Arrays.fill(startVals, Integer.MIN_VALUE);
		return getStatsEval(chars, (a, b) -> Math.max(a, b), startVals, primary);
	}
	
	/**
	 * Iterates over a number of characters and returns the minimum of all their stats seperately.
	 * @param chars The characters to interate over. <code>null</code> entries will be ignored.
	 * @param primary if <code>true</code> the operation will be done over the primary stats, else over the secondary stats 
	 * @return An integer array containing the minimum of all the characters' stats seperately. The order matches the order of the corresponding stats enum.
	 * If <code>chars</code> doesn't contain non <code>null</code> entries, an array full of {@link Integer#MAX_VALUE} will be returned.
	 * @throws NullPointerException If <code>chars</code> refers to <code>null</code>.
	 * @see #getStatsEval(Iterable, BinaryOperator, int[], boolean)
	 * @see #getStatsMax(Iterable, boolean)
	 * @see #getStatsAverage(Iterable, int, boolean)
	 */
	public static int[] getStatsMin(Iterable<? extends CharacterBase> chars, boolean primary) {
		int[] startVals = new int[primary ? StatsPrimary.SIZE : StatsSecondary.SIZE];
		Arrays.fill(startVals, Integer.MAX_VALUE);
		return getStatsEval(chars, (a, b) -> Math.min(a, b), startVals, primary);
	}
	
	/**
	 * Iterates over a number of characters and returns the average of all their stats seperately.
	 * @param chars The characters to interate over. <code>null</code> entries will be ignored.
	 * @param size The number to divide the sum of the characters' stats by. Should be the number of non <code>null</code> entries in <code>chars</code>.
	 * @param primary if <code>true</code> the operation will be done over the primary stats, else over the secondary stats 
	 * @return An integer array containing the average of all the characters' stats seperately. The order matches the order of the corresponding stats enum.
	 * If <code>chars</code> doesn't contain non <code>null</code> entries, an array full of <code>0</code> will be returned.
	 * @throws NullPointerException If <code>chars</code> refers to <code>null</code>.
	 * @throws IllegalArgumentException If <code>size</code> is smaller than or equal to <code>0</code>.
	 * @see #getStatsEval(Iterable, BinaryOperator, int[], boolean)
	 * @see #getStatsMax(Iterable, boolean)
	 * @see #getStatsMin(Iterable, boolean)
	 */
	// size is provided so we can simply make use of getStatsEval
	public static int[] getStatsAverage(Iterable<? extends CharacterBase> chars, int size, boolean primary) {
		if (size <= 0)
			throw new IllegalArgumentException("size must be positive!");
		int statsSize = primary ? StatsPrimary.SIZE : StatsSecondary.SIZE;
		int[] startVals = new int[statsSize];
		// Arrays.fill(startVals, 0); // done automatically
		startVals = getStatsEval(chars, (a, b) -> a + b, startVals, primary);
		for (int i = 0; i < statsSize; i++) {
			startVals[i] /= size;
		}
		return startVals;
	}
	
	public static void linkFandomsToCharacters() {
		for (CharacterBase character : CHARACTERS)
			FandomBase.FANDOMS.lookup(character.getFandomId()).addCharacter(character);
	}
	
	public static <T extends CharacterBase> Comparator<T> createPrimaryComparator(StatsPrimary type) {
		return new Comparator<T>() {
			@Override public int compare(T o1, T o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				if (o1.getPrimaryStat(type) == o2.getPrimaryStat(type))
					return 0;
				if (o1.getPrimaryStat(type) < o2.getPrimaryStat(type))
					return -1;
				return 1;
			}
		};
	}
	
	public static <T extends CharacterBase> Comparator<T> createSecondaryComparator(StatsSecondary type) {
		return new Comparator<T>() {
			@Override public int compare(T o1, T o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				if (o1.getSecondaryStat(type) == o2.getSecondaryStat(type))
					return 0;
				if (o1.getSecondaryStat(type) < o2.getSecondaryStat(type))
					return -1;
				return 1;
			}
		};
	}

}
