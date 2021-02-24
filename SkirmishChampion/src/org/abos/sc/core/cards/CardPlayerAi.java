package org.abos.sc.core.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.abos.sc.core.ChallengeRatable;
import org.abos.sc.core.CharacterBase;
import org.abos.util.AbstractNamedComparator;

/**
 * A player AI for card games
 * @author Sebastian Koch
 * @version %I%
 * @since 0.7
 */
public class CardPlayerAi<T extends Card> extends AbstractCardPlayer<T> {
	
	protected final static ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
	
	protected int waitTime;
	
	protected int tieRounds;
	
	public CardPlayerAi(Collection<T> initialDeck, int waitTime, int tieRounds) {
		super(initialDeck);
		if (waitTime < 0)
			throw new IllegalArgumentException("waitTime cannot be negative!");
		this.tieRounds = tieRounds;
	}
	
	public int chooseImmediately(T card, List<AbstractNamedComparator<T>> comparators) {
		List<String> comparatorNames = new ArrayList<>(comparators.size());
		for (AbstractNamedComparator<T> comp : comparators)
			comparatorNames.add(comp.getName());
		if (card.getRarity() == Rarity.LEGENDARY && comparatorNames.contains(Rarity.DISPLAY_NAME))
			return comparatorNames.indexOf(Rarity.DISPLAY_NAME);
		if (!(card instanceof CharacterBase)) {
			return 0; // because we don't know how to handle it
		}
		CharacterBase character = (CharacterBase)card;
		
		
		if (comparatorNames.contains(ChallengeRatable.DISPLAY_NAME))
			return comparatorNames.indexOf(ChallengeRatable.DISPLAY_NAME);
		return 0; // because we are out of ideas of how to handle it
	}
	
	@Override
	public Future<Boolean> offerTie(int rounds) {
		return EXECUTOR.submit(() -> {
			Thread.sleep(waitTime);
			return tieRounds <= rounds;
		});
	}
	
	@Override
	public Future<Integer> choose(T card, List<AbstractNamedComparator<T>> comparators) {
		return EXECUTOR.submit(() -> {
			Thread.sleep(waitTime);
			return chooseImmediately(card, comparators);
		});
	}
	
}
