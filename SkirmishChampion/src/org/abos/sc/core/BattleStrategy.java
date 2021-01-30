package org.abos.sc.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.ParsedIdNotFoundException;
import org.abos.util.Utilities;

/**
 * Contains the attack order for a complete formation
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class BattleStrategy implements Iterable<BattleTactic>, Cloneable {
	
	/**
	 * The separator character for different tactics
	 * Should differ from {@link BattleTactic#INDEX_SEPARATOR}.
	 */
	public static final char TACTIC_SEPARATOR = '/';
	
	protected class BattleStrategyIterator implements Iterator<BattleTactic> {
		
		/**
		 * The current row of the iterator.
		 */
		protected int row = 0;
		
		/**
		 * The current column of the iterator. 
		 * Note that this might not always be a valid column for a lookup.
		 */
		protected int col = 0;
		
		@Override
		public BattleTactic next() {
			if (!hasNext())
				throw new NoSuchElementException("No more tactics in this strategy!");
			if (col == BattleFormation.COL_NUMBER) {
				col = 0;
				row++;
			}
			return battleTactics[row][col++];
		}
		
		@Override
		public boolean hasNext() {
			return row+1 < BattleFormation.ROW_NUMBER && col < BattleFormation.COL_NUMBER;
		}
	}
	
	/**
	 * The type of the battle strategy.
	 * @see BattleStrategyType
	 */
	protected final BattleStrategyType type;

	protected BattleTactic[][] battleTactics;
	
	protected BattleStrategy(BattleStrategyType type) {
		if (type == null)
			type = BattleStrategyType.CUSTOM;
		battleTactics = new BattleTactic[BattleFormation.ROW_NUMBER][BattleFormation.COL_NUMBER];
		this.type = type;
	}
	
	public static BattleStrategy createRowAssault() {
		BattleStrategy strategy = new BattleStrategy(BattleStrategyType.ROW);
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) 
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				strategy.battleTactics[row][col] = BattleTactic.createRowAssault(col);
		return strategy;
	}
	
	public static BattleStrategy createColAssault() {
		BattleStrategy strategy = new BattleStrategy(BattleStrategyType.COL);
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) 
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				strategy.battleTactics[row][col] = BattleTactic.createColAssault(col);
		return strategy;
	}
	
	public static BattleStrategy createConcentratedAssault() {
		BattleStrategy strategy = new BattleStrategy(BattleStrategyType.CONCENTRATED);
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) 
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				strategy.battleTactics[row][col] = BattleTactic.createConcentratedAssault();
		return strategy;
	}
	
	public static BattleStrategy createStrategy(BattleStrategyType type) {
		if (type == null)
			throw new NullPointerException("type must be specified!");
		switch(type) {
		case ROW: return createRowAssault();
		case COL: return createColAssault();
		case CONCENTRATED: return createConcentratedAssault();
		case CUSTOM: throw new IllegalArgumentException("Use the constructor for a custom strategy!");
		default: throw new IllegalArgumentException("Strategy type is not supported!");
		}
	}
	
	public BattleStrategy(BattleTactic[][] battleTactics) {
		Utilities.requireNonNull(battleTactics, "battleTactics");
		if (battleTactics.length != BattleFormation.ROW_NUMBER)
			throw new IllegalArgumentException("Tactics must be given for all "+BattleFormation.ROW_NUMBER+" rows and not for "+battleTactics.length+"!");
		if (battleTactics[0].length != BattleFormation.COL_NUMBER)
			throw new IllegalArgumentException("Tactics must be given for all "+BattleFormation.COL_NUMBER+" columns and not for "+battleTactics[0].length+"!");
		for (int i = 1; i < BattleFormation.ROW_NUMBER; i++)
			if (battleTactics[i].length != battleTactics[0].length)
				throw new IllegalArgumentException("Each column must have same number of tactics, but column "+i+" doesn't!");

		type = BattleStrategyType.CUSTOM;
		battleTactics = new BattleTactic[BattleFormation.ROW_NUMBER][BattleFormation.COL_NUMBER];
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) 
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				this.battleTactics[row][col] = new BattleTactic(battleTactics[row][col]); // throws NPE
	}
	
	@Override
	public Iterator<BattleTactic> iterator() {
		return new BattleStrategyIterator();
	}
	
	/**
	 * Returns the tactic at the specified position.
	 * @param row the row of the tactic
	 * @param col the column of the tactic
	 * @return the tactic at the specified position, never <code>null</code>
	 * @throws ArrayIndexOutOfBoundsException If <code>row</code> or <code>col</code> is out of bounds.
	 */
	public BattleTactic getTactic(int row, int col) {
		return battleTactics[row][col];
	}
	
	public void reset() {
		for (BattleTactic tactic : this)
			tactic.reset();
	}


	/**
	 * Returns a hash code value for this strategy.
	 * @return a hash code value for this strategy
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(battleTactics);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		BattleStrategy other = (BattleStrategy) obj;
		if (!Arrays.deepEquals(battleTactics, other.battleTactics))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	@Override
	public Object clone() {
		if (!BattleStrategyType.CUSTOM.equals(type))
			return createStrategy(type);
		return new BattleStrategy(battleTactics);
	}
	
	public void toSaveString(StringBuilder s) {
		if (!type.equals(BattleStrategyType.CUSTOM)) {
			s.append(type.name());
			return;
		}
		Iterator<BattleTactic> it = iterator();
		it.next().toSaveString(s);
		while(it.hasNext()) {
			s.append(TACTIC_SEPARATOR);
			it.next().toSaveString();
		}
	}

	public String toSaveString() {
		if (!type.equals(BattleStrategyType.CUSTOM))
			return type.name();
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	@Override
	public String toString() {
		return "BattleStrategy [type=" + type + ", battleTactics=" + Arrays.deepToString(battleTactics) + "]";
	}
	
	public static BattleStrategy parse(String s) {
		Utilities.requireNonNull(s, "s");
		if (s.isEmpty())
			throw new ParsedIdNotFoundException("Unkown strategy identifier !");
		if (!java.lang.Character.isDigit(s.charAt(0))) {
			try {
				return createStrategy(BattleStrategyType.valueOf(s)); // throws IAE
			}
			catch (IllegalArgumentException ex) {
				throw new ParsedIdNotFoundException("Unkown strategy identifier "+s+"!", ex);
			}
		}
		BattleTactic[][] tactics = new BattleTactic[BattleFormation.ROW_NUMBER][BattleFormation.COL_NUMBER];
		String[] split = s.split(String.valueOf(TACTIC_SEPARATOR));
		if (split.length != BattleFormation.MAX_CHAR_NUMBER)
			throw new IllegalNumberOfArgumentsException("Number of targets is "+split.length+" instead of maximum "+BattleFormation.MAX_CHAR_NUMBER+"!");
		for (int row = 0; row < tactics.length; row++)
			for (int col = 0; col < tactics[0].length; col++)
				tactics[row][col] = BattleTactic.parse(split[row*BattleFormation.COL_NUMBER+col]);
		return new BattleStrategy(tactics);
	}
	
}
