package org.abos.sc.core;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
public enum Difficulty {// implements Comparable<Difficulty>{
	
	EASY,
	
	MEDIUM,
	
	HARD;
	
	public final boolean showChallengeRatings() {
		return this.compareTo(MEDIUM) <= 0;
	}

}
