package org.abos.sc.core;

import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.Utilities;

/**
 * Contains a battle formation together with an accompanying strategy.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class BattleEncounter implements Cloneable, ChallengeRatable {
	
	/**
	 * The separator char between the formation and strategy of an encounter in string form.
	 * @see #FIELD_SEPARATOR_REGEX
	 * @see #toSaveString()
	 */
	public static final char FIELD_SEPARATOR = '|';
	
	/**
	 * A regex compatible String version of {@link #FIELD_SEPARATOR}.
	 * @see #parse(String)
	 */
	public static final String FIELD_SEPARATOR_REGEX = "\\|";

	/**
	 * The underlying formation.
	 * @see #getFormation()
	 */
	protected BattleFormation formation;
	
	/**
	 * The underlying strategy.
	 * @see #getStrategy()
	 */
	protected BattleStrategy strategy;

	/**
	 * Creates a new battle encounter from a given formation and strategy. Note that the formation and
	 * strategy are copied by reference.
	 * @param formation the formation for this encounter
	 * @param strategy the strategy for this encounter
	 * @throws NullPointerException If <code>formation</code> or <code>strategy</code> refers to <code>null</code>.
	 */
	public BattleEncounter(BattleFormation formation, BattleStrategy strategy) {
		Utilities.requireNonNull(formation, "formation");
		Utilities.requireNonNull(strategy, "strategy");
		this.formation = formation;
		this.strategy = strategy;
	}
	
	/**
	 * Returns the underlying battle formation.
	 * @return the underlying battle formation
	 */
	public BattleFormation getFormation() {
		return formation;
	}
	
	/**
	 * Returns the underlying battle strategy.
	 * @return the underlying battle strategy
	 */
	public BattleStrategy getStrategy() {
		return strategy;
	}
	
	/**
	 * Returns the number of non <code>null</code> characters in the underlying formation.
	 * @return the number of non <code>null</code> characters in this encounter
	 * @see BattleFormation#getSize()
	 */
	public int getSize() {
		return formation.getSize();
	}
	
	/** 
	 * Returns <code>true</code> if the entire underlying formation is defeated. 
	 * @return <code>true</code> if this encounter is defeated, else <code>false</code>
	 */
	public boolean isDefeated() {
		return formation.isDefeated();
	}

	/**
	 * Returns the character at the specified position.
	 * @param row the row of the character
	 * @param col the column of the character
	 * @return the character at the specified position, may be <code>null</code>
	 * @throws ArrayIndexOutOfBoundsException If <code>row</code> or <code>col</code> is out of bounds.
	 */
	public Character getCharacter(int row, int col) {
		return formation.getCharacter(row, col);
	}
	
	/**
	 * Returns the tactic at the specified position.
	 * @param row the row of the tactic
	 * @param col the column of the tactic
	 * @return the tactic at the specified position, never <code>null</code>
	 * @throws ArrayIndexOutOfBoundsException If <code>row</code> or <code>col</code> is out of bounds.
	 */
	public BattleTactic getTactic(int row, int col) {
		return strategy.getTactic(row, col);
	}

	/**
	 * Returns the challenge rating of this encounter.
	 * @return the challenge rating of this encounter
	 */
	@Override
	public int getChallengeRating() {
		return formation.getChallengeRating();
	}

	/**
	 * Returns a hash code value for this encounter, computed based on the hash code values
	 * of the underlying formation and strategy. Two equal encounters will return the same hash code value.
	 * @return a hash code value for this encounter
	 * @see #equals(Object)
	 * @see BattleFormation#hashCode()
	 * @see BattleStrategy#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formation == null) ? 0 : formation.hashCode());
		result = prime * result + ((strategy == null) ? 0 : strategy.hashCode());
		return result;
	}

	/**
	 * Tests if another object is equal to this encounter. Two encounters are equal if and only if
	 * their underlying formation and strategy are equal.
	 * @return <code>true</code> if the other object is an encounter equal to this one, else <code>false</code>
	 * @see #hashCode()
	 * @see BattleFormation#equals(Object)
	 * @see BattleStrategy#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BattleEncounter other = (BattleEncounter) obj;
		if (formation == null) {
			if (other.formation != null)
				return false;
		} else if (!formation.equals(other.formation))
			return false;
		if (strategy == null) {
			if (other.strategy != null)
				return false;
		} else if (!strategy.equals(other.strategy))
			return false;
		return true;
	}
	
	/**
	 * Returns a deep clone of this encounter.
	 * @return a deep clone of this encounter
	 * @see BattleFormation#clone()
	 * @see BattleStrategy#clone()
	 */
	@Override
	public Object clone() {
		return new BattleEncounter((BattleFormation)formation.clone(), (BattleStrategy)strategy.clone());
	}
	
	/**
	 * Saves the battle encounter to a string builder.
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString()
	 * @see #parse(String)
	 */
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		// if changed, also change the parse function and the documentation of it
		formation.toSaveString(s);
		s.append(FIELD_SEPARATOR);
		strategy.toSaveString(s);
	}
	
	/**
	 * Returns the battle encounter as a string for saving purposes, i.e. in the form needed for {@link #parse(String)}.
	 * @return the battle encounter as a string for saving purposes
	 * @see #toSaveString(StringBuilder)
	 * @see #parse(String)
	 */
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	/**
	 * Returns a string representation of the battle encounter. This is identical to
	 * calling {@link #toSaveString()}.
	 * @return a string representation of the battle encounter
	 */
	@Override
	public String toString() {
		return toSaveString();
	}
	
	/**
	 * Parses a string representation of a battle encounter into a object.
	 * The format is "<code>formation{@value #FIELD_SEPARATOR}strategy</code>".
	 * @param s the string to parse
	 * @return a battle encounter matching the string
	 */
	public static BattleEncounter parse(String s) {
		Utilities.requireNonNull(s, "s");
		String[] split = s.split(FIELD_SEPARATOR_REGEX);
		if (split.length != 2)
			throw new IllegalNumberOfArgumentsException("s must consist of a BattleFormation and a BattleStrategy separated by "+FIELD_SEPARATOR+"!");
		return new BattleEncounter(BattleFormation.parse(split[0]), BattleStrategy.parse(split[1]));
	}
	
}
