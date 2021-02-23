package org.abos.sc.core.cards;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.Future;

import org.abos.util.AbstractNamedComparator;

/**
 * A player for quartett.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.7
 */
public interface QuartettPlayer<T extends Card> {
	
	public Collection<T> getInitialDeck();
	
	public Queue<T> getCurrentStack();

	public Future<Integer> offerTie(int rounds);
	
	public Future<Integer> choose(T card, Iterable<AbstractNamedComparator<T>> comparators);
	
}
