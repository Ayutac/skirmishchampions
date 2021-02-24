package org.abos.sc.core.cards;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.abos.util.AbstractNamedComparator;
import org.abos.util.Utilities;

/**
 * 
 * @author Sebastian Koch
 * @version %I%
 * @param <T> the type of the cards being played with
 * @since 0.7
 */
public class Quartett<T extends Card> extends Thread {

	protected final List<QuartettPlayer<T>> players;
	
	protected final List<AbstractNamedComparator<T>> rules;
	
	protected final List<QuartettPlayer<T>> activePlayers;
	
	protected QuartettPlayer<T> startPlayer;
	
	protected final List<T> tiePile = new LinkedList<>();
	
	protected boolean gameAborted = false;
	
	/**
	 * Creates a new quartett game with the specified players and rules.
	 * @param players The players for this game, must be non empty and not contain <code>null</code>. The entries are copied by reference, the list is not.
	 * @param rules The rules for this game, must be non empty and not contain <code>null</code>. The entries are copied by reference, the iterable is not.
	 * @throws NullPointerException If any parameter or entry refers to <code>null</code>.
	 * @throws IllegalArgumentException If any parameter contains duplicate entries OR if there is no player with a non empty stack.
	 */
	public Quartett(List<QuartettPlayer<T>> players, Collection<AbstractNamedComparator<T>> rules) {
		super("Quartett created "+Instant.now().toString());
		Utilities.requireNonNull(players, "players");
		Utilities.requireNonNullEntries(players, "players");
		Utilities.requireNonNull(rules, "rules");
		Utilities.requireNonNullEntries(rules, "rules");
		Utilities.requireDifferentEntries(players, "players");
		Utilities.requireDifferentEntries(rules, "rules");
		
		this.players = new ArrayList<>(players);
		this.activePlayers = new LinkedList<>(players);
		removeDefeatedPlayers();
		if (activePlayers.isEmpty())
			throw new IllegalArgumentException("There must be at least one player with at least one card!");
		startPlayer = activePlayers.get(0);
		this.rules = new ArrayList<>(rules);
	}
	
	public void removeDefeatedPlayers() {
		activePlayers.removeIf(player -> player.getCurrentStack().isEmpty());
	}
	
	public boolean gameEnded() {
		return activePlayers.size() <= 1;
	}
	
	public boolean gameAborted() {
		return gameAborted;
	}
	
	public boolean gameOver() {
		return gameEnded() || gameAborted();
	}
	
	public void round() throws InterruptedException, ExecutionException {
		// start player decides on category
		Future<Integer> choice = startPlayer.choose(startPlayer.getCurrentStack().peek(), rules);
		AbstractNamedComparator<T> comparator = rules.get(choice.get());
		assert activePlayers.size() > 1;
		// take next card of each player
		List<T> cards = new ArrayList<>(activePlayers.size());
		for (QuartettPlayer<T> player : activePlayers)
			cards.add(player.getCurrentStack().poll());
		// find out the winner of this round
		T currentMax = cards.get(0);
		assert currentMax != null;
		boolean tie = false;
		T comparison;
		int maxIndex = 0, comparisonResult;
		for (int index = 1; index < activePlayers.size(); index++) {
			comparison = cards.get(index);
			assert comparison != null;
			comparisonResult = comparator.compare(currentMax, comparison);
			if (comparisonResult < 0) {
				currentMax = comparison;
				maxIndex = index;
				tie = false;
			}
			else if (comparisonResult == 0)
				tie = true;
			// else the current max stays max, nothing changes
		}
		// give the cards to the winner or to the tie pile
		if (tie) {
			tiePile.addAll(cards);
		}
		else {
			startPlayer = activePlayers.get(maxIndex);
			startPlayer.getCurrentStack().addAll(cards);
		}
		removeDefeatedPlayers();
	}
	
	@Override
	public void run() {
		if (!gameEnded())
			activePlayers.forEach(p -> p.shuffle());
		try {
			while (!gameEnded()) {
				round();
			}
		}
		catch (InterruptedException | ExecutionException ex) {
			gameAborted = true;
		}
	}
	
}
