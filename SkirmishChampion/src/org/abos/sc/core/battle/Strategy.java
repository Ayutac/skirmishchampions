package org.abos.sc.core.battle;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.ParsedIdNotFoundException;
import org.abos.util.SaveString;
import org.abos.util.Utilities;

/**
 * Contains the attack order for a complete formation
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Strategy implements Iterable<Tactic>, Cloneable, SaveString {
	
	/**
	 * The separator character for different tactics
	 * Should differ from {@link Tactic#INDEX_SEPARATOR}.
	 */
	public static final char TACTIC_SEPARATOR = '/';
	
	protected class BattleStrategyIterator implements Iterator<Tactic> {
		
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
		public Tactic next() {
			if (!hasNext())
				throw new NoSuchElementException("No more tactics in this strategy!");
			if (col == Formation.COL_NUMBER) {
				col = 0;
				row++;
			}
			return battleTactics[row][col++];
		}
		
		@Override
		public boolean hasNext() {
			return row+1 < Formation.ROW_NUMBER && col < Formation.COL_NUMBER;
		}
	}
	
	/**
	 * The type of the battle strategy.
	 * @see StrategyType
	 */
	protected final StrategyType type;

	protected Tactic[][] battleTactics;
	
	protected Strategy(StrategyType type) {
		if (type == null)
			type = StrategyType.CUSTOM;
		battleTactics = new Tactic[Formation.ROW_NUMBER][Formation.COL_NUMBER];
		this.type = type;
	}
	
	public static Strategy createRowAssault() {
		Strategy strategy = new Strategy(StrategyType.ROW);
		for (int row = 0; row < Formation.ROW_NUMBER; row++) 
			for (int col = 0; col < Formation.COL_NUMBER; col++)
				strategy.battleTactics[row][col] = Tactic.createRowAssault(col);
		return strategy;
	}
	
	public static Strategy createColAssault() {
		Strategy strategy = new Strategy(StrategyType.COL);
		for (int row = 0; row < Formation.ROW_NUMBER; row++) 
			for (int col = 0; col < Formation.COL_NUMBER; col++)
				strategy.battleTactics[row][col] = Tactic.createColAssault(col);
		return strategy;
	}
	
	public static Strategy createConcentratedAssault() {
		Strategy strategy = new Strategy(StrategyType.CONCENTRATED);
		for (int row = 0; row < Formation.ROW_NUMBER; row++) 
			for (int col = 0; col < Formation.COL_NUMBER; col++)
				strategy.battleTactics[row][col] = Tactic.createConcentratedAssault();
		return strategy;
	}
	
	public static Strategy createStrategy(StrategyType type) {
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
	
	public Strategy(Tactic[][] battleTactics) {
		Utilities.requireNonNull(battleTactics, "battleTactics");
		if (battleTactics.length != Formation.ROW_NUMBER)
			throw new IllegalArgumentException("Tactics must be given for all "+Formation.ROW_NUMBER+" rows and not for "+battleTactics.length+"!");
		if (battleTactics[0].length != Formation.COL_NUMBER)
			throw new IllegalArgumentException("Tactics must be given for all "+Formation.COL_NUMBER+" columns and not for "+battleTactics[0].length+"!");
		for (int i = 1; i < Formation.ROW_NUMBER; i++)
			if (battleTactics[i].length != battleTactics[0].length)
				throw new IllegalArgumentException("Each column must have same number of tactics, but column "+i+" doesn't!");

		type = StrategyType.CUSTOM;
		battleTactics = new Tactic[Formation.ROW_NUMBER][Formation.COL_NUMBER];
		for (int row = 0; row < Formation.ROW_NUMBER; row++) 
			for (int col = 0; col < Formation.COL_NUMBER; col++)
				this.battleTactics[row][col] = new Tactic(battleTactics[row][col]); // throws NPE
	}
	
	@Override
	public Iterator<Tactic> iterator() {
		return new BattleStrategyIterator();
	}
	
	/**
	 * Returns the tactic at the specified position.
	 * @param row the row of the tactic
	 * @param col the column of the tactic
	 * @return the tactic at the specified position, never <code>null</code>
	 * @throws ArrayIndexOutOfBoundsException If <code>row</code> or <code>col</code> is out of bounds.
	 */
	public Tactic getTactic(int row, int col) {
		return battleTactics[row][col];
	}
	
	public void reset() {
		for (Tactic tactic : this)
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
		Strategy other = (Strategy) obj;
		if (!Arrays.deepEquals(battleTactics, other.battleTactics))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	@Override
	public Object clone() {
		if (!StrategyType.CUSTOM.equals(type))
			return createStrategy(type);
		return new Strategy(battleTactics);
	}
	
	@Override
	public void toSaveString(StringBuilder s) {
		if (!type.equals(StrategyType.CUSTOM)) {
			s.append(type.name());
			return;
		}
		Iterator<Tactic> it = iterator();
		it.next().toSaveString(s);
		while(it.hasNext()) {
			s.append(TACTIC_SEPARATOR);
			it.next().toSaveString();
		}
	}

	@Override
	public String toString() {
		return "BattleStrategy [type=" + type + ", battleTactics=" + Arrays.deepToString(battleTactics) + "]";
	}
	
	public static Strategy parse(String s) {
		Utilities.requireNonNull(s, "s");
		if (s.isEmpty())
			throw new ParsedIdNotFoundException("Unkown strategy identifier !");
		if (!java.lang.Character.isDigit(s.charAt(0))) {
			try {
				return createStrategy(StrategyType.valueOf(s)); // throws IAE
			}
			catch (IllegalArgumentException ex) {
				throw new ParsedIdNotFoundException("Unkown strategy identifier "+s+"!", ex);
			}
		}
		Tactic[][] tactics = new Tactic[Formation.ROW_NUMBER][Formation.COL_NUMBER];
		String[] split = s.split(String.valueOf(TACTIC_SEPARATOR));
		if (split.length != Formation.MAX_CHAR_NUMBER)
			throw new IllegalNumberOfArgumentsException("Number of targets is "+split.length+" instead of maximum "+Formation.MAX_CHAR_NUMBER+"!");
		for (int row = 0; row < tactics.length; row++)
			for (int col = 0; col < tactics[0].length; col++)
				tactics[row][col] = Tactic.parse(split[row*Formation.COL_NUMBER+col]);
		return new Strategy(tactics);
	}
	
}
