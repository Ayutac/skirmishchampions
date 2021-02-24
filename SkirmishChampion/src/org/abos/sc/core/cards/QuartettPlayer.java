package org.abos.sc.core.cards;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Future;

import org.abos.util.AbstractNamedComparator;

/**
 * A player for quartett.
 * @author Sebastian Koch
 * @version %I%
 * @param <T> the type of the cards being played with
 * @since 0.7
 */
public interface QuartettPlayer<T extends Card> {
	
	/**
	 * Returns the initial deck of this player by reference. This collection is not modifyable.
	 * @return the initial deck of this player, not <code>null</code>
	 */
	public Collection<T> getInitialDeck();
	
	/**
	 * Returns the current stack of this player by reference. This queue is modifyable, but <code>null</code> entries are not allowed.
	 * @return the current stack of this player 
	 */
	public Queue<T> getCurrentStack();
	
	/**
	 * Shuffles the current stack of this player.
	 */
	public void shuffle();

	public Future<Integer> offerTie(int rounds);
	
	public Future<Integer> choose(T card, List<AbstractNamedComparator<T>> comparators);
	
}
