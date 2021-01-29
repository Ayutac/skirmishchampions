package org.abos.sc.core;

import java.util.Arrays;

import org.abos.util.IllegalArgumentRangeException;
import org.abos.util.IllegalArgumentTypeException;
import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.ParseException;
import org.abos.util.Utilities;

/**
 * Contains the attack pattern for a single character.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class BattleTactic implements Cloneable {
	/*
	 * Note that this class does NOT implement Iterable<Integer>.
	 * This is a design choice given that future changes to this class
	 * or subclasses may decide not to have a static attack order 
	 * but something else entirely.
	 */
	
	/**
	 * 
	 */
	public static final char INDEX_SEPARATOR = ',';

	/**
	 * The order in which to attack.
	 */
	protected int[] attackOrder;
	
	/**
	 * The current target index.
	 */
	protected int currentIndex = 0;
	
	/**
	 * Creates a new battle tactic with an attack order initialized to 0.
	 * Note that this constitutes an invalid battle tactic and needs
	 * to change to a valid one.
	 */
	private BattleTactic() {
		attackOrder = new int[BattleFormation.MAX_CHAR_NUMBER];
	}
	
	/**
	 * 
	 * @param col the column of the attacker
	 * @return
	 */
	public static BattleTactic createRowAssault(int col) {
		int[] alteratingCols = Utilities.createAlteratingLowerFirst(col,BattleFormation.COL_NUMBER);
		BattleTactic tactic = new BattleTactic();
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int i = 0; i  < alteratingCols.length; i++)
				tactic.attackOrder[row*BattleFormation.COL_NUMBER+i] = row*BattleFormation.COL_NUMBER+alteratingCols[i];
		return tactic;
	}
	
	/**
	 * 
	 * @param col the column of the attacker
	 * @return
	 */
	public static BattleTactic createColAssault(int col) {
		int[] alteratingCols = Utilities.createAlteratingLowerFirst(col,BattleFormation.COL_NUMBER);
		BattleTactic tactic = new BattleTactic();
		for (int i = 0; i < alteratingCols.length; i++)
			for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
				tactic.attackOrder[i*BattleFormation.ROW_NUMBER+row] = row*BattleFormation.COL_NUMBER+alteratingCols[i];
		return tactic;
	}
	
	public static BattleTactic createConcentratedAssault() {
		BattleTactic tactic = new BattleTactic();
		for (int i = 0; i  < BattleFormation.MAX_CHAR_NUMBER; i++)
			tactic.attackOrder[i] = i;
		return tactic;
	}

	public BattleTactic(int[] attackOrder) {
		Utilities.requireNonNull(attackOrder, "attackOrder");
		if (attackOrder.length != BattleFormation.MAX_CHAR_NUMBER)
			throw new IllegalArgumentException("Number of targets is "+attackOrder.length+" instead of maximum "+BattleFormation.MAX_CHAR_NUMBER+"!");
		if (!Utilities.checkPermutation(attackOrder))
			throw new IllegalArgumentException("attackOrder doesn't constitutes a permutation!");
		
		this.attackOrder = Arrays.copyOf(attackOrder, attackOrder.length);
	}
	
	public BattleTactic(BattleTactic tactic) {
		Utilities.requireNonNull(tactic, "tactic");
		attackOrder = Arrays.copyOf(tactic.attackOrder, tactic.attackOrder.length);
	}
	
	public int getCurrentTarget() {
		return attackOrder[currentIndex]; // throws AIOOBE
	}
	
	public int getCurrentTargetRow() {
		return attackOrder[currentIndex] / BattleFormation.COL_NUMBER; // throws AIOOBE
	}
	
	public int getCurrentTargetCol() {
		return attackOrder[currentIndex] % BattleFormation.COL_NUMBER; // throws AIOOBE
	}
	
	public boolean hasTarget() {
		return currentIndex < attackOrder.length && 0 <= currentIndex;
	}
	
	/**
	 * 
	 * @return <code>false</code> if there is no target left
	 */
	public boolean nextTarget() {
		currentIndex++;
		return hasTarget();
	}
	
	/**
	 * Resets the current target to the first target.
	 */
	public void reset() {
		currentIndex = 0;
	}
	
	@Override
	public Object clone() {
		return new BattleTactic(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(attackOrder);
		result = prime * result + currentIndex;
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
		BattleTactic other = (BattleTactic) obj;
		if (!Arrays.equals(attackOrder, other.attackOrder))
			return false;
		if (currentIndex != other.currentIndex)
			return false;
		return true;
	}
	
	public void toSaveString(StringBuilder s) {
		for (int i = 0; i+1 < attackOrder.length; i++) {
			s.append(attackOrder[i]);
			s.append(INDEX_SEPARATOR);
		}
		s.append(attackOrder[attackOrder.length-1]);
	}

	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	@Override
	public String toString() {
		return "BattleTactic [attackOrder=" + Arrays.toString(attackOrder) + "]";
	}

	/**
	 * Parses a string representation of a battle tactic into a proper tactic.
	 * The tactic is given by a list of exactly {@link BattleFormation#MAX_CHAR_NUMBER} 
	 * positions being a permutation of 1 to {@link BattleFormation#MAX_CHAR_NUMBER}, 
	 * separated by the {@value #INDEX_SEPARATOR} character without additional whitespaces. 
	 * A {@link ParseException} will be thrown if the string cannot be parsed correctly.
	 * @param s the string to parse
	 * @return a battle tactic matching the string
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalNumberOfArgumentsException If the number of positions isn't {@link BattleFormation#MAX_CHAR_NUMBER}.
	 * @throws IllegalArgumentTypeException If any position isn't a number.
	 * @throws IllegalArgumentRangeException If the {@link BattleFormation#MAX_CHAR_NUMBER} numeric positions
	 * don't constitute a permutation of the numbers from 1 to {@link BattleFormation#MAX_CHAR_NUMBER}.
	 */
	public static BattleTactic parse(String s) {
		Utilities.requireNonNull(s, "s");
		String[] split = s.split(String.valueOf(INDEX_SEPARATOR));
		if (split.length != BattleFormation.MAX_CHAR_NUMBER)
			throw new IllegalNumberOfArgumentsException("Number of targets is "+split.length+" instead of maximum "+BattleFormation.MAX_CHAR_NUMBER+"!");
		try {
			return new BattleTactic(Utilities.arrayToInt(split));
		}
		catch (NumberFormatException ex) {
			throw new IllegalArgumentTypeException(ex);
		}
		catch (IllegalArgumentException ex) {
			throw new IllegalArgumentRangeException(ex);
		}
	}
	
}
