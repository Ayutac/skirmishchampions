package org.abos.sc.core.cards;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import org.abos.util.Utilities;

/**
 * A class to provide a few basic card player implementations.
 * @author Sebastian Koch
 * @version %I%
 * @param <T> the type of the cards being played with
 * @since 0.7
 */
public abstract class AbstractCardPlayer<T extends Card> implements QuartettPlayer<T> {

	/**
	 * The initial deck of this player.
	 * @see #getInitialDeck()
	 */
	protected Collection<T> initialDeck;
	
	/**
	 * The current stack of this player.
	 * @see #getCurrentStack()
	 */
	protected Queue<T> currentStack;
	
	/**
	 * Creates a new player with the specified initial deck. The current stack gets copied from that deck.
	 * @param initialDeck the initial deck of this player
	 * @throws NullPointerException If <code>initialDeck</code> or any of its entries refers to <code>null</code>.
	 */
	public AbstractCardPlayer(Collection<T> initialDeck) {
		Utilities.requireNonNull(initialDeck, "initialDeck");
		Utilities.requireNonNullEntries(initialDeck, "initialDeck");
		this.initialDeck = Collections.unmodifiableList(new ArrayList<>(initialDeck));
		this.currentStack = new ArrayDeque<>(initialDeck);
	}
	
	@SuppressWarnings("javadoc")
	public void shuffle() {
		List<T> list = new ArrayList<>(currentStack.size());
		list.addAll(currentStack);
		Collections.shuffle(list);
		currentStack.clear();
		currentStack.addAll(list);
	}
	
	@SuppressWarnings("javadoc")
	@Override
	public Collection<T> getInitialDeck() {
		return initialDeck;
	}
	
	@SuppressWarnings("javadoc")
	@Override
	public Queue<T> getCurrentStack() {
		return currentStack;
	}
	
}
