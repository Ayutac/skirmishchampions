package org.abos.sc.core.cards;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.abos.util.AbstractNamedComparator;
import org.abos.util.Utilities;

/**
 * 
 * @author Sebastian Koch
 * @version %I%
 * @since 0.7
 */
public class Quartett<T extends Card> extends Thread {

	protected List<QuartettPlayer<T>> players;
	
	protected Collection<AbstractNamedComparator<T>> rules;
	
	/**
	 * Creates a new quartett game with the specified players and rules.
	 * @param players The players for this game, must be non empty and not contain <code>null</code>. The entries are copied by reference, the list is not.
	 * @param rules The rules for this game, must be non empty and not contain <code>null</code>. The entries are copied by reference, the iterable is not.
	 * @throws NullPointerException If any parameter or entry refers to <code>null</code>.
	 * @throws IllegalArgumentException If any parameter contains duplicate entries.
	 */
	public Quartett(List<QuartettPlayer<T>> players, Iterable<AbstractNamedComparator<T>> rules) {
		super("Quartett created "+Instant.now().toString());
		Utilities.requireNonNull(players, "players");
		Utilities.requireNonNullEntries(players, "players");
		Utilities.requireNonNull(rules, "rules");
		Utilities.requireNonNullEntries(rules, "rules");
		Utilities.requireDifferentEntries(players, "players");
		Utilities.requireDifferentEntries(rules, "rules");
		
		this.players = new ArrayList<>(players);
		this.rules = new LinkedList<>();
		for (AbstractNamedComparator<T> rule : rules)
			this.rules.add(rule);
	}
	
	@Override
	public void run() {
		
	}
	
}
