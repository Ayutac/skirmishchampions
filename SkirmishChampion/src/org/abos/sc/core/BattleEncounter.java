package org.abos.sc.core;

import org.abos.util.Utilities;

/**
 * Contains a battle formation together with an accompanying strategy.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class BattleEncounter {
	
	public static final char FIELD_SEPARATOR = '|';
	
	public static final String FIELD_SEPARATOR_REGEX = "\\|";

	protected BattleFormation formation;
	
	protected BattleStrategy strategy;

	public BattleEncounter(BattleFormation formation, BattleStrategy strategy) {
		Utilities.requireNonNull(formation, "formation");
		Utilities.requireNonNull(strategy, "strategy");
		this.formation = formation;
		this.strategy = strategy;
	}
	
	public BattleFormation getFormation() {
		return formation;
	}
	
	public BattleStrategy getStrategy() {
		return strategy;
	}
	
	public int getSize() {
		return formation.getSize();
	}
	
	public boolean isDefeated() {
		return formation.isDefeated();
	}
	
	public Character getCharacter(int row, int col) {
		return formation.getCharacter(row, col);
	}
	
	public BattleTactic getTactic(int row, int col) {
		return strategy.getTactic(row, col);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formation == null) ? 0 : formation.hashCode());
		result = prime * result + ((strategy == null) ? 0 : strategy.hashCode());
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
	
	public void toSaveString(StringBuilder s) {
		formation.toSaveString(s);
		s.append(FIELD_SEPARATOR);
		strategy.toSaveString(s);
	}
	
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	@Override
	public String toString() {
		return "BattleEncounter [formation=" + formation + ", strategy=" + strategy + "]";
	}
	
	public static BattleEncounter parse(String s) {
		String[] split = s.split(FIELD_SEPARATOR_REGEX); // throws NPE
		if (split.length != 2)
			throw new IllegalArgumentException("s must consist of a BattleFormation and a BattleStrategy separated by "+FIELD_SEPARATOR+"!");
		return new BattleEncounter(BattleFormation.parse(split[0]), BattleStrategy.parse(split[1]));
	}
	
}
